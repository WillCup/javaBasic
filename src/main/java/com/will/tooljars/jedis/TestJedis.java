package com.will.tooljars.jedis;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.mongodb.util.ThreadPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;

/**
 * http://www.blogways.net/blog/2013/06/02/jedis-demo.html 
 * 
 * 测试redis的操作10000个用户队列，每个队列放10个值，
 * 	单线程：
 *  	使用普通jedis对象，整体插入使用了140.418 seconds；
 * 		使用管道中调用事务，耗时139.798 seconds；
 * 		仅使用事务操作，耗时仅8.795 seconds；
 * 		从这里面查找第5234个，遍历其中的2到8位索引数据，耗时33 millseconds。
 * 
 * 		过程中redis耗用资源情况 —— 还算稳定了，4G内存，双核
		processor	: 1
		vendor_id	: GenuineIntel
		cpu family	: 6
		model		: 13
		model name	: QEMU Virtual CPU version (cpu64-rhel6)
		stepping	: 3
		cpu MHz		: 1995.187
		cache size	: 4096 KB
		fpu		: yes
		fpu_exception	: yes
		cpuid level	: 4
		wp		: yes
		flags		: fpu de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pse36 clflush mmx fxsr sse sse2 syscall nx lm unfair_spinlock pni cx16 hypervisor lahf_lm
		bogomips	: 3990.37
		clflush size	: 64
		cache_alignment	: 64
		address sizes	: 46 bits physical, 48 bits virtual
	300个并发线程，每个线程插入10条：
 * 		仅使用事务操作，耗时仅0.414 seconds；
 * 		从这里面查找第164个，遍历其中的2到8位索引数据，耗时33 millseconds。
 * 		验证插入结果，没有问题。
 * 
 * @author Will
 * @created at 2014-8-12 下午4:43:33
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJedis {

    private static Jedis jedis;
    private static ShardedJedis sharding;
    private static ShardedJedisPool pool;
    /**
     * 
     * 1->获取Jedis实例需要从JedisPool中获取；
	 * 2->用完Jedis实例需要返还给JedisPool；
	 * 3->如果Jedis在使用过程中出错，则也需要还给JedisPool；
	 * 
     */
    private static JedisPool jedisPool;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        List<JedisShardInfo> shards = Arrays.asList(
                new JedisShardInfo("10.0.1.62",6379),
                new JedisShardInfo("10.0.1.62",6379)); //使用相同的ip:port,仅作测试


        jedis = new Jedis("10.0.1.62", 6379, 60 * 1000); 
        sharding = new ShardedJedis(shards);
        pool = new ShardedJedisPool(new JedisPoolConfig(), shards);
        
        JedisPoolConfig poolCfg = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        poolCfg.setMaxActive(500);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        poolCfg.setMaxIdle(10);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        poolCfg.setMaxWait(1000 * 100);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        poolCfg.setTestWhileIdle(true);
//        poolCfg.setWhenExhaustedAction(JedisPoolConfig.WHEN_EXHAUSTED_BLOCK);
        jedisPool = new JedisPool(poolCfg, "10.0.1.62", 6379);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jedis.disconnect();
        sharding.disconnect();
        pool.destroy();
    }

   /**
     * 一、普通同步方式 
     * 
     *      每次set之后都可以返回结果，标记是否成功
     */
    @Test
    public void test1Normal() {
        long start = System.currentTimeMillis();
        // 单线程
//        for (int i = 0; i < 10000; i++) {
//            for (int j = 0; j < 10 ; j++) {
//                jedis.lpush("key-" + i, "value-" + i + "-" + j);
//            }
//        }
        // 300线程并发
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        for (int i = 0; i < 300; i++) {
        	threadPool.execute(new JedisThread(i)); 
        }
//        try {
//			threadPool.awaitTermination(60 * 2, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
        long end = System.currentTimeMillis();
        System.out.println("Simple SET: " + ((end - start)/1000.0) + " seconds");
    }

    /**
     * 二、事务方式(Transactions)
     * 
         * redis的事务很简单，他主要目的是保障，一个client发起的事务中的命令可以连续的执行，而中间不会插入其他client的命令。
         * 我们调用jedis.watch(…)方法来监控key，如果调用后key值发生变化，则整个事务会执行失败。
         * 另外，事务中某个操作失败，并不会回滚其他操作。 这一点需要注意。还有，我们可以使用discard()方法来取消事务。
     */
    @Test
    public void test2Trans() {
        long start = System.currentTimeMillis();
        Transaction tx = jedis.multi();
//        for (int i = 0; i < 100000; i++) {
//            tx.set("t" + i, "t" + i);
//        }
        //System.out.println(tx.get("t1000").get());
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10 ; j++) {
            	tx.lpush("key-" + i, "value-" + i + "-" + j);
            }
        }

        List<Object> results = tx.exec();
        long end = System.currentTimeMillis();
        System.out.println("Transaction SET: " + ((end - start)/1000.0) + " seconds");
    }

    /**
     * 三、管道(Pipelining)
     *      
     *      有时，我们需要采用异步方式，一次发送多个指令，不同步等待其返回结果。这样可以取得非常好的执行效率。
     *      
     *//*
    @Test
    public void test3Pipelined() {
        Pipeline pipeline = jedis.pipelined();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("p" + i, "p" + i);
        }
        //System.out.println(pipeline.get("p1000").get());
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined SET: " + ((end - start)/1000.0) + " seconds");
    }

    *//**
     * 四、管道中调用事务
     * 
     *      就Jedis提供的方法而言，是可以做到在管道中使用事务。
     *      但是经测试（见本文后续部分），发现其效率和单独使用事务差不多，甚至还略微差点。
     */
    @Test
    public void test4combPipelineTrans() {
        long start = System.currentTimeMillis();
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
//        for (int i = 0; i < 100000; i++) {
//            pipeline.set("" + i, "" + i);
//        }
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10 ; j++) {
            	pipeline.lpush("key-" + i, "value-" + i + "-" + j);
            }
        }
        pipeline.exec();
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined transaction: " + ((end - start)/1000.0) + " seconds");
    }

    /**
     * 五、分布式直连同步调用
     * 
     *      这个是分布式直接连接，并且是同步调用，每步执行都返回执行结果。类似地，还有异步管道调用。
     *//*
    @Test
    public void test5shardNormal() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = sharding.set("sn" + i, "n" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Simple@Sharing SET: " + ((end - start)/1000.0) + " seconds");
    }

    *//**
     * 六、分布式直连异步调用
     * 
     *      
     *//*
    @Test
    public void test6shardpipelined() {
        ShardedJedisPipeline pipeline = sharding.pipelined();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("sp" + i, "p" + i);
        }
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined@Sharing SET: " + ((end - start)/1000.0) + " seconds");
    }

    *//**
     * 七、分布式连接池同步调用
     * 
     *      如果，你的分布式调用代码是运行在线程中，那么上面两个直连调用方式就不合适了，因为直连方式是非线程安全的，这个时候，你就必须选择连接池调用。
     *//*
    @Test
    public void test7shardSimplePool() {
        ShardedJedis one = pool.getResource();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = one.set("spn" + i, "n" + i);
        }
        long end = System.currentTimeMillis();
        pool.returnResource(one);
        System.out.println("Simple@Pool SET: " + ((end - start)/1000.0) + " seconds");
    }

    *//**
     * 八、分布式连接池异步调用
     * 
     *      上面是同步方式，当然还有异步方式。
     *//*
    @Test
    public void test8shardPipelinedPool() {
        ShardedJedis one = pool.getResource();

        ShardedJedisPipeline pipeline = one.pipelined();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("sppn" + i, "n" + i);
        }
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        pool.returnResource(one);
        System.out.println("Pipelined@Pool SET: " + ((end - start)/1000.0) + " seconds");
        
    }*/
    
    
    /**
    * 9、select the one key out of 100 000 keys from redis server
    */
   @Test
   public void test9Select() {
       
	   long start = System.currentTimeMillis();
       List<String> lrange = jedis.lrange("key-5334", 2, 8);
       if (lrange == null || lrange.size() == 0) {
    	   System.out.println("No key found");
       }
       for (String str : lrange) {
    	   System.out.println(str);
       }
       long end = System.currentTimeMillis();
       System.out.println("Simple GET: " + ((end - start)) + " millseconds");
       
   }
   
   /**
    * 10、select the one key out of 100 000 keys from redis server
    */
   @Test
   public void test10Select() {
       
	   long start = System.currentTimeMillis();
	   for (int i = 0; i < 300; i++) {
		   List<String> lrange = jedis.lrange("key--" + i, 0, -1);
		   if (lrange.size() != 10) {
			   System.out.println("error happens : " + lrange.size());
		   }
	   }
       long end = System.currentTimeMillis();
       System.out.println("Simple GET: " + ((end - start)) + " millseconds");
       
   }
   
   private class JedisThread implements Runnable {
	    private int num;
	    
		public JedisThread(int num) {
			super();
			this.num = num;
		}

		@Override
		public void run() {
			Jedis innerJedis = jedisPool.getResource();
			try {
				Transaction tx = innerJedis.multi();
				for (int i = 0; i < 10; i++) {
					tx.lpush("key--" + num, "value-" + num + "-" + i);
				}
				List<Object> results = tx.exec();
			} catch (Exception e) {
				e.printStackTrace();
				//在instance出错时，必须调用returnBrokenResource返还给pool，否则下次通过getResource得到的instance的缓冲区可能还存在数据，出现问题！
				jedisPool.returnBrokenResource(innerJedis);
			} finally {
				if (innerJedis != null) {
					jedisPool.returnResource(innerJedis);
				}
			}
		}
	   
   }
}
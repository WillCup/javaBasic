package com.will.tooljars.jedis;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Memcached完全基于分布式集群，而Redis是Master-Slave，如果想把Reids，做成集群模式，无外乎多做几套Master-Slave，
 * 每套Master-Slave完成各自的容灾处理，通过Client工具，完成一致性哈希。（PS:Memcached是在Server端完成Sharding，
 * Redis只能依靠各个Client做Sharding。可能会在Redis 3.0系列支持Server端Sharding。）
 * shared一致性哈希采用以下方案： Redis服务器节点划分：将每台服务器节点采用hash算法划分为160个虚拟节点(可以配置划分权重)
 * 将划分虚拟节点采用TreeMap存储 对每个Redis服务器的物理连接采用LinkedHashMap存储 对Key or KeyTag
 * 采用同样的hash算法
 * ，然后从TreeMap获取大于等于键hash值得节点，取最邻近节点存储；当key的hash值大于虚拟节点hash值得最大值时，存入第一个虚拟节点
 * sharded采用的hash算法：MD5 和 MurmurHash两种；默认采用64位的MurmurHash算法；有兴趣的可以研究下
 * 
 * @author Will
 * @created at 2014-8-12 下午5:12:51
 */
public class JedisDemo {
    private static JedisPool pool;
    private static ResourceBundle bundle;
    private static JedisPoolConfig config;

    static {
        // Locale currentLocale = Locale.getDefault();
        // Locale local = new Locale("zh", "CN");
        bundle = ResourceBundle.getBundle("jedisPool.properties");
        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[jedisPool.properties] is not found!");
        }
        config = new JedisPoolConfig();
        config.setMaxActive(Integer.valueOf(bundle
                .getString("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(bundle
                .getString("redis.pool.maxIdle")));
        config.setMaxWait(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle
                .getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle
                .getString("redis.pool.testOnReturn")));
        pool = new JedisPool(config, bundle.getString("redis.ip"),
                Integer.valueOf(bundle.getString("redis.port")));
    }

    public void normalWay() {
        Jedis redis = new Jedis("192.168.161.130", 6379);// 连接redis
        // redis.auth("redis");// 验证密码,如果需要验证的话
        System.out.println("This is info : " + redis.info());
        // STRING 操作
        // SET key value将字符串值value关联到key。
        redis.set("name", "wangjun1");
        redis.set("id", "123456");
        redis.set("address", "guangzhou");
        // SETEX key seconds value将值value关联到key，并将key的生存时间设为seconds(以秒为单位)。
        redis.setex("foo", 5, "haha");
        // MSET key value [key value ...]同时设置一个或多个key-value对。
        redis.mset("haha", "111", "xixi", "222");
        // redis.flushAll();清空所有的key
        System.out.println(redis.dbSize());// dbSize是多少个key的个数
        // APPEND key value如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
        redis.append("foo", "00");// 如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
        // GET key 返回key所关联的字符串值
        redis.get("foo");
        // MGET key [key ...] 返回所有(一个或多个)给定key的值
        List list = redis.mget("haha", "xixi");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public static void main(String[] args) {
        JedisDemo t1 = new JedisDemo();
        t1.normalWay();
        t1.poolWay();
    }

    public void poolWay() {
        // 从池中获取一个Jedis对象
        Jedis jedis = pool.getResource();
        String keys = "name";
        // 删数据
        jedis.del(keys);
        // 存数据
        jedis.set(keys, "snowolf");
        // 取数据
        String value = jedis.get(keys);
        System.out.println(value);
        // 释放对象池
        pool.returnResource(jedis);
    }

    /**
     * 通过以下方式，向redis进行set操作的key-value，会通过hash而均匀的分配到pool里的redis机器中。
     */
    public void distributedWay() {
        // 保留前面的JedisPoolConfig，新增两个Redis的IP（redis1.ip，redis2.ip），完成两个JedisShardInfo实例，并将其丢进List中
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(
                bundle.getString("redis1.ip"), Integer.valueOf(bundle
                        .getString("redis.port")));
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(
                bundle.getString("redis2.ip"), Integer.valueOf(bundle
                        .getString("redis.port")));
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
        list.add(jedisShardInfo2);

        // 初始化ShardedJedisPool代替JedisPoo
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, list);

        // 改由ShardedJedis，获取Jedis对象：
        getObjFromSharedJedis(shardedJedisPool);
    }

    private void getObjFromSharedJedis(ShardedJedisPool shardedJedisPool) {
        // 从池中获取一个Jedis对象
        ShardedJedis jedis = shardedJedisPool.getResource();
        String keys = "name";
        String value = "snowolf";
        // 删数据
        jedis.del(keys);
        // 存数据
        jedis.set(keys, value);
        // 取数据
        String v = jedis.get(keys);
        System.out.println(v);
        // 释放对象池
        shardedJedisPool.returnResource(jedis);
    }
}
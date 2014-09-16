package com.will.tooljars.memcache;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

public class TestMemcached {

    public static void main(String[] args) {

        BasicConfigurator.configure();

        // 缓存服务器地址，多台服务器则以逗号隔开，11211为memcached使用的端口号

        String[] servers = { "192.168.161.130:11211" };

        // 得到一个链接池对象并进行一些初始化工作

        SockIOPool pool = SockIOPool.getInstance();

        pool.setServers(servers);

        pool.setFailover(true);

        pool.setInitConn(10);

        pool.setMinConn(5);

        pool.setMaxConn(250);

        // pool.setMaintSleep( 30 );

        pool.setNagle(false);

        pool.setSocketTO(3000);

        pool.setAliveCheck(true);

        pool.initialize();

        MemCachedClient mcc = new MemCachedClient();
        
        Map<String, Map<String, String>> statsItems = mcc.stats();
//        Map<String, Map<String, String>> statsItems = mcc.statsItems();
        Iterator<Entry<String, Map<String, String>>> iterator = statsItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Map<String, String>> next = iterator.next();
            System.out.println(" Outter key : " + next.getKey());
            Iterator<Entry<String, String>> iterator2 = next.getValue().entrySet().iterator();
            while (iterator2.hasNext()) {
                Entry<String, String> next2 = iterator2.next();
                System.out.println("\t inner key is: " + next2.getKey() + " , inner value is :" + next2.getValue());
            }
        }
        
        // turn off most memcached client logging:

        // Logger.getLogger( MemCachedClient.class.getName() ).setLevel(
        // com.schooner.MemCached.Logger. );

        // 以下是数据写入和取出操作例子

//        for (int i = 0; i < 10; i++) {
//
//            boolean success = mcc.set("" + i, "Hello!");
//
//            String result = (String) mcc.get("" + i);
//
//            System.out.println(String.format("set( %d ): %s", i, success));
//
//            System.out.println(String.format("get( %d ): %s", i, result));
//
//        }
//
//        System.out.println("\n\t — sleeping –\n");
//
//        try {
//            Thread.sleep(10000);
//        } catch (Exception ex) {
//        }
//
//        for (int i = 0; i < 10; i++) {
//
//            boolean success = mcc.set("" + i, "Hello!");
//
//            String result = (String) mcc.get("" + i);
//
//            System.out.println(String.format("set( %d ): %s", i, success));
//
//            System.out.println(String.format("get( %d ): %s", i, result));
//
//        }

    }
}
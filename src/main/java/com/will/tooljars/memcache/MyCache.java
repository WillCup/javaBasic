package com.will.tooljars.memcache;  
  
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
  
public class MyCache {  
    public static void main(String[] args) {  
        MemCachedClient client=new MemCachedClient();  
        String [] addr ={"192.168.161.130:11211"};  
        Integer [] weights = {3};  
        SockIOPool pool = SockIOPool.getInstance();  
        pool.setServers(addr);  
        pool.setWeights(weights);  
        pool.setInitConn(5);  
        pool.setMinConn(5);  
        pool.setMaxConn(200);  
        pool.setMaxIdle(1000*30*30);  
        pool.setMaintSleep(30);  
        pool.setNagle(false);  
        pool.setSocketTO(30);  
        pool.setSocketConnectTO(0);  
        pool.initialize();  
          
//      String [] s  =pool.getServers();  
//        client.setCompressEnable(true);  
//        client.setCompressThreshold(1000*1024);  
          
//      将数据放入缓存  
        client.set("test2","test2");  
        
        MemCachedClient mcc = new MemCachedClient();

        Map<String, Map<String, String>> statsItems = mcc.statsItems();
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
          
//      将数据放入缓存,并设置失效时间  
        Date date=new Date(2000000);  
        client.set("test1","test1", date);  
          
//      删除缓存数据  
//      client.delete("test1");  
          
//      获取缓存数据  
        String str =(String)client.get("test1");  
        System.out.println(str);  
    }  
}  
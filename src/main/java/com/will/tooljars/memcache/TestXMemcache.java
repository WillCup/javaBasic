/*package com.will.tooljars.memcache;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
 
public class TestXMemcache {
    public static void main(String[] args) {
       MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil
              .getAddresses("192.168.161.130:11211"));
       MemcachedClient memcachedClient;
       try {
           memcachedClient = builder.build();
 
           memcachedClient.set("hello", 0, "Hello,xmemcached");
           String value = memcachedClient.get("hello");
           System.out.println("hello=" + value);
           memcachedClient.delete("hello");
           value = memcachedClient.get("hello");
           System.out.println("hello=" + value);
           // close memcached client
           memcachedClient.shutdown();
       } catch (MemcachedException e) {
           System.err.println("MemcachedClient operation fail");
           e.printStackTrace();
       } catch (TimeoutException e) {
           System.err.println("MemcachedClient operation timeout");
           e.printStackTrace();
       } catch (InterruptedException e) {
           // ignore
       }catch (IOException e) {
           System.err.println("Shutdown MemcachedClient fail");
           e.printStackTrace();
       }
    }
}*/
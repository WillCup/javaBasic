package com.will.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-8-3
 */
public class ChangeWhenRunning {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Runnable> runners = new HashMap<String, Runnable>();
        Runner runner = new Runner();
        runners.put("1", runner);
        for (Runnable r : runners.values()) {
            new Thread(r).start();
        }
        Thread.sleep(5000);
        runner.setMsg("This message comes from will.");
    }
    
    static class Runner implements Runnable {
        String msg = "default msg";
        
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("I am runing -- " + msg);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
        
    }
}

package com.will.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                
                public void run() {
                    while (true) {

                        System.out.println("I am -- " + Thread.currentThread().getName());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.setName("Thread>>" + i);
            pool.submit(thread);
        }
    }
}

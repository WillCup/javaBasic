package com.will.concurrent;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheculeThreadPoolTest {
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors
                .newScheduledThreadPool(Runtime.getRuntime()
                        .availableProcessors());
        System.out.println(Runtime.getRuntime().availableProcessors());
        int initialDelay = 0;
        for (int i = 0; i < 4; i++) {
            pool.scheduleWithFixedDelay((new Runners("Thead-" + i)), initialDelay, getDelay(),
                    TimeUnit.SECONDS);
            System.out.println("pool status : " + pool.toString());
            initialDelay += 1;
        }
    }

    private static long getDelay() {
        return 10;
    }

    static class Runners implements Runnable {
        private String name;

        public Runners(String name) {
            super();
            this.name = name;
        }

        long counter;

        @Override
        public void run() {
            System.out.println(name + " ..........." + counter + new Date());
            counter++;
        }

    }
}

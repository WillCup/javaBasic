package com.will.concurrent;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * 每10秒钟响一次，到一个小时，就停止了。
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-8-3
 */
class BeeperControl {
    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);

    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() {
                System.out.println("beep");
            }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(
                beeper, 10, 10, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() {
                beeperHandle.cancel(true);
            }
        }, 60 * 60, SECONDS);
    }
    
    public static void main(String[] args) {
        BeeperControl bc = new BeeperControl();
        bc.beepForAnHour();
    }
}
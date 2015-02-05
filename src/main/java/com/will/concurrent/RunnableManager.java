package com.will.concurrent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnableManager {
   
    
    public static void main(String[] args) {
        RunnerManager manager = new RunnerManager();
        manager.start();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            Runner runner = new Runner();
            pool.execute(runner);
        }
    }
}

class RunnerWrapper {
    private Thread process;
    private long delay;
    private long time;

    public RunnerWrapper(Thread process, long time, long delay) {
        super();
        this.process = process;
        this.delay = delay;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Thread getProcess() {
        return process;
    }

    public void setProcess(Thread process) {
        this.process = process;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}

class Runner implements Runnable {
    private boolean initialed;
    @Override
    public void run() {
        while(true) {
            if (!initialed) {
                RunnerManager.register(Thread.currentThread(), System.currentTimeMillis(), 1000 * 10);
                initialed = true;
            }
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                
            }
        }
    }
}

class RunnerManager extends Thread {
    private static Set<RunnerWrapper> runners = new HashSet<RunnerWrapper>();
    
    public static void register(Thread runner, long time, long delay) {
        runners.add(new RunnerWrapper(runner, time, delay));
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                long currentTime = System.currentTimeMillis();
                for (RunnerWrapper runner : runners) {
                    if (runner.getDelay() < currentTime - runner.getTime()) {
                        System.out.println("Going to kill " + runner);
                        runner.getProcess().interrupt();
                    }
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

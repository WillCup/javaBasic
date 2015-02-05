package com.will.thread;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpUtils;

import com.will.exception.ExceptionCollector;

/**
 * 场景：
 * 	一些运行着的任务如果出现问题，想办法剔除掉。并且重启所有池里的任务。
 * 
 * @author will
 * 
 * @2014-12-16
 */
public class CodeRunnableManager implements Runnable {

    public Set<TestCodeTask> runners = new CopyOnWriteArraySet<TestCodeTask>();
    
    ExecutorService pool = Executors.newCachedThreadPool();

    public void registerCodeRunnable(TestCodeTask process) {
        runners.add(process);
    }

    public CodeRunnableManager(Set<TestCodeTask> runners) {
        super();
        this.runners = runners;
        executeTasks(runners);
    }

    private void executeTasks(Set<TestCodeTask> runners) {
        for (TestCodeTask task : runners) {
            pool.execute(task);
            System.out.println(task.getClass().getSimpleName() + " has been started");
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                long current = System.currentTimeMillis();
                for (TestCodeTask wrapper : runners) {
                    System.out.println("\t current is " + current);
                    System.out.println("\t LastExecTime is " + wrapper.getLastExecTime());
                    System.out.println("\t " + wrapper.getInterval() * 5 * 1000);
                    if (wrapper.getLastExecTime() != 0 && current - wrapper.getLastExecTime() > wrapper.getInterval() * 5 * 1000) {
                        wrapper.interrupt();
                        if (wrapper.getFiles() != null){
                            for (File file : wrapper.getFiles()) {
                                file.delete();
                            }
                        }
                        System.out.println("THread is timeout : " + wrapper.getName());
                        System.out.println("Going to shutdown the thread pool " + pool.toString());
                        pool.shutdown();
//                        List<Runnable> shutdownNow = pool.shutdownNow();
//                        for (Runnable run : shutdownNow) {
//                            System.out.println(run + " going to be shutdown");
//                        }
//                        while (pool.awaitTermination(1, TimeUnit.SECONDS)) {
//                        while (pool.isShutdown()) {
                        while (pool.isTerminated()) {
                            System.out.println("The thread pool has been shutdown " + new Date());
                            executeTasks(runners);
                            Thread.sleep(200);
                        }
                    }
                }
            } catch (Exception e1) {
                System.out.println("Error happens when we trying to interrupt and restart a code task ");
                e1.printStackTrace();
                ExceptionCollector.registerException(e1);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
    public static void main(String[] args) {
        Set<TestCodeTask> tasks = new HashSet<TestCodeTask>();
        
        TestCodeTask task = new TestCodeTask();
        task.setInterval(1);
        task.setName("task-1");
        tasks.add(task);
        
        
        TestCodeTask task1 = new TestCodeTask();
        task1.setInterval(2);
        task.setName("task-2");
        tasks.add(task1);
        
        CodeRunnableManager codeManager = new CodeRunnableManager(tasks);
        new Thread(codeManager).start();
    }
    
}

class TestCodeTask extends Thread {
    private long lastExecTime = 0;
    protected long interval = 10000;
    
    public long getLastExecTime() {
        return lastExecTime;
    }

    public void setLastExecTime(long lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
    
    public File[] getFiles() {
        return null;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            lastExecTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " is running -> " + new Date());
            try {
                Thread.sleep(getInterval() * 6 * 1000);
                System.out.println(Thread.currentThread().getName() + " after sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}


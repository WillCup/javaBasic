package com.will.concurrent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.util.BlockingArrayQueue;
/**
 * 要写一个调度器，能够根据每个task自身不同到频率，对其进行调度。考虑使用ScheduledExecutorService
 * 只能按照一个频率进行周期性调度，放弃之；
 * 
 * 参考JKB对于监控任务的调度经验：把上次调度的时间存在DB里面，每隔一定时间使用DBTaskFetcher去DB里面查询已经到了或者超过监控频率的task，执行这些Task。
 * 引用存储的上次调度时间的方法，将上次调度时间直接存在Task里面，这些Task呢，就放在缓存里面。
 * 每次执行调度之前，遍历缓存中的所有Task，如果这个task的等待时间已经不小于其调度频率，调度之。
 * 这样的好处就是：方便更新task，按时调度task。至于以上提到的每隔一定时间，可以看做David写出来的slot。
 * 以前David的思想是每个时间槽尽量放最少的任务，以免出现多个task同时被调度。
 * 现在暂时搁置不理，优先Task队列的易维护性。
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-10-13
 */
public class ThreadPoolTest implements Runnable {
    private static final long TIME_SLOT = 1000;

    public static void main(String[] args) throws Exception{
//                simpleScheduleOne();

//        classicPoolAndQueue();
      
        new Thread(new ThreadPoolTest()).start();
    }

    private static void classicPoolAndQueue() throws InterruptedException,
            ExecutionException {
        int corePoolSize = 1;
        int maximumPoolSize = 10;
        long keepAliveTime = 10;
        TimeUnit milliseconds = TimeUnit.SECONDS;
        
        int capacity = 10;
        int growBy = 2;
        int maxCapacity = 14;
        BlockingQueue<Runnable> runnableTaskQueue = new BlockingArrayQueue<Runnable>(capacity, growBy, maxCapacity);
        ThreadFactory handler = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        };
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, milliseconds,runnableTaskQueue, handler);
        Runnable task = new Runnable() {
            
            @Override
            public void run() {
                System.out.println("I am thinking...");
            }
        };
        threadPoolExecutor.execute(task );
    }

    private static void simpleScheduleOne() {
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
        threadPool.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                System.out.println("I am running now..");
            }
            
        },  10, 1, TimeUnit.SECONDS);
        
    }

    @Override
    public void run() {
//        callableAndFuture();
        List<RunnableTask> tasks = new ArrayList<RunnableTask>();
        ExecutorService pool = Executors.newCachedThreadPool();
        Random random = new Random();
        for (int i = 0; i < 20 ; i ++) {
            tasks.add(new RunnableTask("task-" + i, "sd", random.nextInt(10) + 1, 0));
        }
        long current;
        long timeGone = 0;
        while (!Thread.currentThread().isInterrupted()) {
            current = System.currentTimeMillis();
            System.out.println("outter---------------------------------------" + new Date(current));
            try {
                for (RunnableTask task : tasks) {
                    if (task.getTimestamp() != 0) {
                        long minus = (current - task.getTimestamp()) / 1000;
                        if (minus > task.getFreq()) {
                         // 第一次运行
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> minus is : " + minus + " ----- freq is " + task.getFreq() + " ----- timeGone : " + timeGone);
//                            System.out.println(">>>>>>>>>>>>>>>" + task);
                            task.setTimestamp(System.currentTimeMillis());
                            pool.execute(task);
//                            System.out.println(submit.get());
                        }
                    } else {
                        // 第一次运行
                        System.out.println("first time for : " + task);
                        task.setTimestamp(current);
                        pool.execute(task);
                    }
                }

                timeGone++;
                Thread.sleep(TIME_SLOT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void callableAndFuture() {
        List<CallableTask> tasks = new ArrayList<CallableTask>();
        ExecutorService pool = Executors.newCachedThreadPool();
        Random random = new Random();
        for (int i = 0; i < 20 ; i ++) {
            tasks.add(new CallableTask("task-" + i, "sd", random.nextInt(10) + 1, 0));
        }
        long current;
        long timeGone = 0;
        while (!Thread.currentThread().isInterrupted()) {
            current = System.currentTimeMillis();
            System.out.println("outter---------------------------------------" + new Date(current));
            try {
                for (CallableTask task : tasks) {
                    if (task.getTimestamp() != 0) {
                        long minus = (current - task.getTimestamp()) / 1000;
                        if (minus > task.getFreq()) {
                         // 第一次运行
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> minus is : " + minus + " ----- freq is " + task.getFreq() + " ----- timeGone : " + timeGone);
//                            System.out.println(">>>>>>>>>>>>>>>" + task);
                            task.setTimestamp(System.currentTimeMillis());
                            Future<String> submit = pool.submit(task);
//                            System.out.println(submit.get());
                        }
                    } else {
                        // 第一次运行
                        System.out.println("first time for : " + task);
                        task.setTimestamp(current);
                        Future<String> submit = pool.submit(task);
                        System.out.println(submit.get());
                    }
                }

                timeGone++;
                Thread.sleep(TIME_SLOT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

class CallableTask implements Callable<String> {
    private String name;
    private String job;
    private int freq;
    private long timestamp;
    private int honors;
    public int getFreq() {
        return freq;
    }
    public void setFreq(int freq) {
        this.freq = freq;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    @Override
    public String toString() {
        return "Task [name=" + name + ", job=" + job + ", freq=" + freq
                + ", timestamp=" + timestamp + ", honors=" + honors + "]";
    }
    public CallableTask(String name, String job, int freq, long timestamp) {
        super();
        this.name = name;
        this.job = job;
        this.freq = freq;
        this.timestamp = timestamp;
    }
    @Override
    public String call() throws Exception {
        honors++;
        System.out.println(this + " is running.......");
        return "success";
    }
}

class RunnableTask implements Runnable {
    private String name;
    private String job;
    private int freq;
    private long timestamp;
    private int honors;
    public int getFreq() {
        return freq;
    }
    public void setFreq(int freq) {
        this.freq = freq;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    @Override
    public String toString() {
        return "Task [name=" + name + ", job=" + job + ", freq=" + freq
                + ", timestamp=" + timestamp + ", honors=" + honors + "]";
    }
    public RunnableTask(String name, String job, int freq, long timestamp) {
        super();
        this.name = name;
        this.job = job;
        this.freq = freq;
        this.timestamp = timestamp;
    }
    @Override
    public void run() {
        honors++;
        System.out.println(this + " is running.......");        
    }
}

package com.will.concurrent;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

class DirectExecutor implements Executor {
     public void execute(Runnable r) {
         r.run();
     }
 }
//More typically, tasks are executed in some thread other than the caller's thread. The executor below spawns a new thread for each task.
 class ThreadPerTaskExecutor implements Executor {
     public void execute(Runnable r) {
         new Thread(r).start();
     }
 }
//Many Executor implementations impose some sort of limitation on how and when tasks are scheduled. The executor below serializes the submission of tasks to a second executor, illustrating a composite executor.
 class SerialExecutor implements Executor {
   final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
   final Executor executor;
   Runnable active;

   SerialExecutor(Executor executor) {
     this.executor = executor;
   }

   public synchronized void execute(final Runnable r) {
     tasks.offer(new Runnable() {
       public void run() {
         try {
           r.run();
         } finally {
           scheduleNext();
         }
       }
     });
     if (active == null) {
       scheduleNext();
     }
   }

   protected synchronized void scheduleNext() {
     if ((active = tasks.poll()) != null) {
       executor.execute(active);
     }
   }
 }
//package com.will.concurrent;
//
//import java.util.concurrent.Callable;
//import java.util.concurrent.RunnableScheduledFuture;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//
//public class CustomScheduledExecutor extends ScheduledThreadPoolExecutor {
//
//   static class CustomTask<V> implements RunnableScheduledFuture<V> {
//
//   }
//
//   protected <V> RunnableScheduledFuture<V> decorateTask(
//                Runnable r, RunnableScheduledFuture<V> task) {
//       return new CustomTask<V>(r, task);
//   }
//
//   protected <V> RunnableScheduledFuture<V> decorateTask(
//                Callable<V> c, RunnableScheduledFuture<V> task) {
//       return new CustomTask<V>(c, task);
//   }
//   // ... add constructors, etc.
// }
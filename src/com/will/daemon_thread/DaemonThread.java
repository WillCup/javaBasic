package com.will.daemon_thread;

public class DaemonThread extends Thread{

    public void run() {
        Thread inner = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    System.out.println("Daemon.Normal  is running....");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }
            }
            
        });
        
        inner.start();
        while (true) {
            System.err.println("Daemon is running....");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
}

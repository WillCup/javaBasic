package com.will.daemon_thread;

public class NormalThread extends Thread{

    public void run() {
        Thread inner = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    System.out.println("Normal.Daemon is running....");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                }
            }
            
        });
        
        inner.setDaemon(false);
        inner.start();
        while (true) {
            System.err.println("Normal is running....");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}

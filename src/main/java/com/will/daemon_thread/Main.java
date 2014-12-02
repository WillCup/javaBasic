package com.will.daemon_thread;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DaemonThread daemon = new DaemonThread();
        NormalThread normal = new NormalThread();
        daemon.setDaemon(true);
        
        daemon.start();
        normal.start();
        Thread.sleep(3000);
        normal.stop();
        normal.destroy();
        normal.interrupt();
//        System.exit(0);
    }
}

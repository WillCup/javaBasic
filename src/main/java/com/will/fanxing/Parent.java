package com.will.fanxing;

/**
 * 测试泛型里的类型擦除对runnable对象的影响
 * 
 * @author Will
 * @created at 2014-6-27 上午9:53:00
 * @param <T>
 */
public class Parent<T extends Runnable> {
    protected T t;
    void start() {
        new Thread(t).start();
    }
    
    public static void main(String[] args) {
    }
}

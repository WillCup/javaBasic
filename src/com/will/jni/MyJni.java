package com.will.jni;

public class MyJni {
    // 使用JNI的关键字native
    // 这个关键字决定我们那些方法能在我们的C文件中使用
    // 只须声明,不必实现
    public native void display();

    public native double sum(double x, double y);

    // 这个是到时候调用我们写好的C文件
    // 现在用不上
    // static {
    // System.loadLibrary("sum");
    // }

    public static void main(String[] args) {
        // 到时候测试用方法现在用不上
        // new MyJni().display();
        // System.out.println(new MyJni().sum(2.0, 3.0));
    }
}
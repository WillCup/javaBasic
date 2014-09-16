package com.will.jni;

public class HelloJNI {
	
	// 声明 so 库中的方法
	public native static String sayHi(String name);

	// 载入 so 动态链接库
	static {
		System.load("/home/hongquan/main.so");
	}

	// java 类入口函数
	public static void main(String[] args) {
		System.out.println(sayHi("It's a result return by main.so file!!"));
	}

}
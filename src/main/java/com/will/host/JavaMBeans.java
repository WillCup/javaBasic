package com.will.host;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

public class JavaMBeans {
	public static void main(String[] args) {
		Method[] methods = ManagementFactory.class.getDeclaredMethods();
		for (Method m : methods) {
//			if (m.getName().startsWith("get") && m.getName().endsWith("ean")) {
				System.out.println(m.getName());
//			}
		}
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
//		System.out.println(operatingSystemMXBean.getAvailableProcessors());
//		System.out.println(operatingSystemMXBean.getSystemLoadAverage());
	}
}

package com.will.tools;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class PidGetter {
	public static void main(String[] args) {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		System.out.println(runtimeMXBean.getName());
		System.out.println(runtimeMXBean.getName().split("@")[0]);
	}
}

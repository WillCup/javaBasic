package com.will.jmx;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class PidGetter {
	public static void main(String[] args) {
//		try {
//			while (true) {
//				String pid=ManagementFactory.getRuntimeMXBean().getName();
//				String[] pids = pid.split("@");
//				for (String id : pids) {
//					System.out.println(id + "----\n");
//				}
//				Thread.sleep(1000);
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		populateJDKMBean();
	}
	
	private static void populateJDKMBean () {
		CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
		ManagementFactory.getClassLoadingMXBean();
		MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
	}
}

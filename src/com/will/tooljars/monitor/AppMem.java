package com.will.tooljars.monitor;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import com.sun.management.OperatingSystemMXBean;

public class AppMem {
    public static void main(String[] args) {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
        OperatingSystemMXBean.class);
        // What % CPU load this current JVM is taking, from 0.0-1.0
//        System.out.println(osBean.getProcessCpuLoad());
        // What % load the overall system is at, from 0.0-1.0
//        System.out.println(osBean.getSystemCpuLoad());
//        System.out.println(osBean.getProcessCpuTime());
//        System.out.println(osBean.getFreePhysicalMemorySize());
        Map a = new HashMap();
        TIntObjectMap trove = new TIntObjectHashMap();
        
        System.out.println((osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize()) / 1024l / 1024l);
        System.out.println((osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize()) / 1024l);
        System.out.println((osBean.getTotalSwapSpaceSize() - osBean.getFreeSwapSpaceSize()) / 1024l);
        for (int i = 0; i < 1000 * 1000; i++) {
//            a.put("a"+i, new Person("d", i));
            trove.put(i, new Person("will", 20));
        }
        System.out.println((osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize()) / 1024l / 1024l);
        System.out.println((osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize()) / 1024l);
        System.out.println((osBean.getTotalSwapSpaceSize() - osBean.getFreeSwapSpaceSize()) / 1024l);
        System.out.println(a.size());
    }
    
    static class Person {
        private String name;
        private int age;
        public Person(String name, int age) {
            super();
            this.name = name;
            this.age = age;
        }
    }
}

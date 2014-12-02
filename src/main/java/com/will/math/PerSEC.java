package com.will.math;

public class PerSEC {
    public static void main(String[] args) {
        long t1  = System.currentTimeMillis();
        long a = 100000344;
        long t2 = System.currentTimeMillis();
        System.out.println(t2);
        System.out.println(t1);
        System.out.println(a/(t2 -t1) /1000L);
    }
}

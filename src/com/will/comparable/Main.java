package com.will.comparable;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        ToBeCompared a = new ToBeCompared();
        a.setAge(2);
        a.setName("a");
        ToBeCompared b = new ToBeCompared();
        b.setAge(6);
        b.setName("a");
        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(a.compareTo(b));
//        if (a.compareTo(b) == Comparator.class) {
//            System.err.println("a == b");
//        }
    }
}

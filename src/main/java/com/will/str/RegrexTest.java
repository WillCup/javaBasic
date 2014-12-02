package com.will.str;

public class RegrexTest {
    public static void main(String[] args) {
        String name = "server-name_test";
//        name = "apache::80";
        String abc = "sdfdsf3243";
        String re = "[^A-Za-z0-9]";
        String[] split = name.split(re);
        String result = "";
        boolean sa = false;
        for (String s : split) {
        	System.err.println(s.length());
        }
        
        for (String str : split) {
            if (sa) {
                result += str.substring(0, 1).toUpperCase() + str.substring(1);
            } else {
                result = str;
                sa = true;
            }
        }
        System.out.println(result);
        
        String[] splits = name.split(re);
        boolean sas = false;
        for (String str : split) {
            if (sas) {
                name += str.substring(0, 1).toUpperCase() + str.substring(1);
            } else {
                name = str;
                sas = true;
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + name);
        
        
        
    }
}

package com.will.map;

import java.util.HashMap;
import java.util.Map;

public class MapRemoveMap {
    public static void main(String[] args) {
        Map m1 = new HashMap();
        m1.put("a", "a");
        m1.put("b", "b");
        Map m2 = new HashMap();
        m2.put("b", "b");
        
        System.out.println(m1.remove(m2));
    }

}

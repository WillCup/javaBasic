package com.will.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.will.Cat;
import com.will.Obj;

/**
 * 
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-10-13
 */
public class MapInObj {
    public static void main(String[] args) {
        Cat[] cats = new Cat[]{new Cat("sd"), new Cat("tom")};
        Obj obj = new Obj();
        Map<Integer, List<Cat>> maps = obj.getMap();
        maps.put(1, Arrays.asList(cats));
        Iterator<Entry<Integer, List<Cat>>> iterator = obj.getMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Integer, List<Cat>> next = iterator.next();
            System.out.println(next.getKey());
            System.out.println(next.getValue());
        }
    }
}
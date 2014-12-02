package com.will.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * If o1 is bigger than o2, then o1 - o2 > 0.
 * 
 * @author Will
 * @created at 2014-7-15 下午6:38:26
 */
public class Main {
    public static void main(String[] args) {
        List<O> os = new ArrayList<O>();
        for (int i = 0; i < 100; i++) {
            os.add(new O(new Random().nextInt(500)));
        }
        
        Collections.sort(os, new Comparator<O>() {

            public int compare(O o1, O o2) {
                System.out.println(o2.getAge() - o1.getAge() + " ------------------" + o2.getAge() + "  " + o1.getAge());
                return o2.getAge() - o1.getAge();
            }
        });
        int counter = 14;
        for (O o : os) {
            System.out.println(o.getAge());
            if (counter-- < 1) {
                break;
            }
        }
    }
}

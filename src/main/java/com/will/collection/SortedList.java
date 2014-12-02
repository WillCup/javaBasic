package com.will.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SortedList {
    private static boolean debuging = false;

    // 10 * 1000 * 1000 --> 11403, 11420, 11808 --> 1.7.0_40-b43
    static List<Obj> list = new ArrayList<Obj>();
    
 // 10 * 1000 * 1000 --> 13417, 13362, 13393 --> 1.7.0_40-b43
//    static List<Obj> list = new LinkedList<Obj>();
    
    // 10 * 1000 * 1000 --> 13417, 13362, 13393 --> 1.7.0_40-b43
//  static Obj[] list = new Obj[5];
    
//    static Comparator<Obj> comparator = new AgeComparator();
    static Comparator<Obj> comparator = new GirlFriendsComparator();
    
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int counter = 10 * 1000 * 1000;
        Random random = new Random();
        while (counter > 0) {
            addToList(new Obj("will", random.nextInt(100), random.nextInt(100)));
            addToList(new Obj("chris", random.nextInt(100), random.nextInt(100)));
            addToList(new Obj("tailer", random.nextInt(100), random.nextInt(100)));
            addToList(new Obj("steven", random.nextInt(100), random.nextInt(100)));
            addToList(new Obj("victor",random.nextInt(100), random.nextInt(100)));
            addToList(new Obj("james", random.nextInt(100), random.nextInt(100)));
            addToList(new Obj("linken", random.nextInt(100), random.nextInt(100)));
            addToList(new Obj("dneg", random.nextInt(100), random.nextInt(100)));
            counter--;
        }
        
        System.out.println("----------------------------------------------begin to sort..-----------------------");
        
        for (Obj o : list) {
            System.out.println(o);
        }
        
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
    
    private static void addToList(Obj obj) {
        if (list.size() < 4) {
            list.add(obj);
        } else if (list.size() == 4) {
            list.add(obj);
            Collections.sort(list, comparator);
            if (debuging) {
                System.out.println("we got 5 guys now..");
                for (Obj o : list) {
                    System.out.println(o);
                }
            }
            System.out.println();
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (comparator.compare(list.get(i), obj) > 0) {
                    list.add(i, obj);
                    list.remove(list.size() - 1);
                    break;
                }
            }
            if (debuging) {
                System.out.println("A new guy comes ->>>> " + obj);
                for (Obj o : list) {
                    System.out.println(o);
                }
                System.out.println();
            }
        }
    }
    
    static class AgeComparator implements Comparator<Obj> {

        public int compare(Obj o1, Obj o2) {
            if (o1.getAge() > o2.getAge()) {
                return -1;
            } else if (o1.getAge() == o2.getAge()) {
                return 0;
            } else {
                return 1;
            }
        }
    }
    
    static class GirlFriendsComparator implements Comparator<Obj> {

        public int compare(Obj o1, Obj o2) {
            if (o1.getGirls() > o2.getGirls()) {
                return -1;
            } else if (o1.getGirls() == o2.getGirls()) {
                return 0;
            } else {
                return 1;
            }
        }
    }


    static class Obj {
        private String name;
        private int age;
        private int girls;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
        public int getGirls() {
            return girls;
        }
        public void setGirls(int girls) {
            this.girls = girls;
        }
        public Obj(String name, int age, int girls) {
            super();
            this.name = name;
            this.age = age;
            this.girls = girls;
        }
        public String toString() {
            return name + " : age=" + age + ", girls=" + girls;
        }
    }
}

package com.will.tooljars.monitor.trove4j;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;

/**
 * Trove相当于把JDK集合类都针对原生类型处理了一遍，例如int，常见的类有 TIntList、TIntObjectMap、TObjectIntMap、TIntSet。
 * 可想而知，维护Trove的工作量是挺大的。
 * 
 * @author Will
 * @created at 2014-8-8 上午10:46:46
 */
public class Test {
    public static void main(String[] args) {
//        test1();
        
        test2();
    }

    /**
     * Trove不推荐JDK的entryXX的做法，而是采用了forEach的回调方式。 
     * 代码显得更好看些，另外内存方面也有优势，因为使用entryXX的做法，需要创建一个新的数组
     */
    public static void test2() {
        TIntObjectMap<String> ints = new TIntObjectHashMap<String>();
        ints.put(100, "John");
        ints.put(101, "Tom");
        ints.forEachEntry(new TIntObjectProcedure<String>() {
            public boolean execute(int a, String b) {
                System.out.println("key: " + a + ", val: " + b);
                return true;
            }
        });
        ints.forEachKey(new TIntProcedure() {
            public boolean execute(int value) {
                System.out.println("key: " + value);
                return true;
            }
        });
        ints.forEachValue(new TObjectProcedure<String>() {
            public boolean execute(String object) {
                System.out.println("val: " + object);
                return true;
            }
        });
    }

    public static void test1() {
        TIntObjectMap<String> ints = new TIntObjectHashMap<String>();
        ints.put(100, "John");
        ints.put(101, "Tom");
        System.out.println(ints.get(100));
    }
}

package com.will.reflect;
import java.lang.reflect.*;
public class Test {
    static class A<T> {
        T t;

        public void test() {
            System.out.println(t);
        }
    }

    static class B<T> {
        T t;
        public B(T t) {this.t = t;}

        public void test() {System.out.println(t);}
    }

    public static void main(String[] args) throws Throwable {
        Class<?> c = A.class;
        Object o = c.newInstance();
        Method m = c.getDeclaredMethod("test");
        m.invoke(o);
        
        c = B.class;
        Constructor con = c.getDeclaredConstructor(new Class[]{Object.class}); //用Object.class代替T
        o = con.newInstance(new Object[]{"bbb"});
        m = c.getDeclaredMethod("test");
        m.invoke(o);
    }
}
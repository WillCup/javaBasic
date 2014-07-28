package com.will.getTransfer;

/**
 * Whether 
 * 
 * @author Will
 * @created at 2014-7-16 下午4:46:55
 */
public class Main {
    public static void main(String[] args) {
        TestObj obj = new TestObj();
        obj.setName("dsfsd");
        String a = obj.getName();
        a = "111";
        System.out.println(obj.getName());
    }
    
    static class TestObj {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

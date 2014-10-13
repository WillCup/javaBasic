package com.will.tooljars.jackson;


public class Obj {
    private boolean test;
    private int number;
    private String str;
    
public Obj(){
        
    }
    public boolean isTest() {
        return test;
    }
    
    
    public void setTest(boolean test) {
        this.test = test;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getStr() {
        return str;
    }
    public void setStr(String str) {
        this.str = str;
    }
    @Override
    public String toString() {
        return "ListTest [test=" + test + ", number=" + number + ", str="
                + str + "]";
    }
}

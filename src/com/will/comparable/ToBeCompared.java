package com.will.comparable;

public class ToBeCompared implements Comparable<ToBeCompared>{
    public String name;
    
    public int age;
    
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

    public int compareTo(ToBeCompared o) {
        if (o.getName().equals(name)) {
            return 0;
        }
        return 1;
    }
}

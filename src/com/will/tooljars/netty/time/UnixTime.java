package com.will.tooljars.netty.time;

import java.util.Date;

public class UnixTime {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString() {
        return new Date(value * 1000L).toString();
    }
    
}
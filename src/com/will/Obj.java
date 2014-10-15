package com.will;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Obj {
    private Map<Integer, List<Cat>> map = new HashMap<Integer, List<Cat>>();

    public Map<Integer, List<Cat>> getMap() {
        return map;
    }

    public void setMap(Map<Integer, List<Cat>> map) {
        this.map = map;
    }
}
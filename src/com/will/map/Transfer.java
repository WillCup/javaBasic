package com.will.map;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-7-25
 */
public class Transfer {
	public static void main(String[] args) {
		Map m = new HashMap();
		m.put("test", 12321);
		Map m2 = new HashMap();
		m2.putAll(m);
		System.out.println(m2);
		m.clear();
		System.out.println(m2);
	}
}

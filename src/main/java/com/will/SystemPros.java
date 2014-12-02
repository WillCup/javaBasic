package com.will;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class SystemPros {
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.name"));
		Properties properties = System.getProperties();
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Object, Object> next = iterator.next();
			System.out.println(next.getKey() + "  " + next.getValue());
		}
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}

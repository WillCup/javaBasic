package com.will.host;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class JavaProperties {
	public static void main(String[] args) {
		Properties properties = System.getProperties();
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Object, Object> next = iterator.next();
			System.out.println(next.getKey() + " - " + next.getValue());
		}
	}
}

package com.will.collection;

import java.util.ArrayList;
import java.util.List;

public class IteratorChanges {
	public static void main(String[] args) {
		List<String> strList = new ArrayList<String>();
		strList.add("dfsdfsd");
		strList.add("bbbbbbbbbbbbbbbb");
		for (String d : strList) {
			d = "ddddddddddddd";
		}
		for (String s : strList) {
			System.out.println(s);
		}
	}
}

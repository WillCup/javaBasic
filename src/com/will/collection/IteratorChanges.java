package com.will.collection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.will.Cat;

public class IteratorChanges {
	public static void main(String[] args) throws Exception {
	    Cat n = new Cat("will");
		List<Cat> strList = new ArrayList<Cat>();
		strList.add(new Cat("1"));
		strList.add(new Cat("323"));
		for (Cat d : strList) {
//		    d.setName("chris");
//	        d = n;
	        BeanUtils.copyProperties(d, n);
//	        System.out.println("inner + " + d.getName());
		}
		for (Cat s : strList) {
			System.out.println(s.getName());
		}
	}
}

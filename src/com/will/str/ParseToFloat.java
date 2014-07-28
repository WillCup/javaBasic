package com.will.str;

public class ParseToFloat {
	public static void main(String[] args) {
		Object str = ".00176392";
		System.out.println(str.toString().indexOf("."));
		System.out.println(Double.parseDouble(str.toString()));
		System.out.println(Float.parseFloat(str.toString()));
	}
}

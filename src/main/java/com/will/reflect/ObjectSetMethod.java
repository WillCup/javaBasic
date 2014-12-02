package com.will.reflect;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.MethodUtils;

public class ObjectSetMethod {
	static class Obj {
		private String name;
		private double height;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getHeight() {
			return height;
		}
		public void setHeight(double height) {
			this.height = height;
		}
		@Override
		public String toString() {
			return "Obj [name=" + name + ", height=" + height + "]";
		}
	}
	
	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Obj o = new Obj();
		MethodUtils.invokeMethod(o, "setName", "this?");
		MethodUtils.invokeMethod(o, "setHeight", "1.23");
		System.out.println(o);
	}
}

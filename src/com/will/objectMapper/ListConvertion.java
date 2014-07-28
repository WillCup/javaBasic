package com.will.objectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListConvertion {
	static class Obj {
		private String name;
		private int age;
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
	}
	
	/**
	 *
	 * <br/>
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Obj> objs = new ArrayList<Obj>();
			Obj o1 = new Obj();
			o1.setAge(1);
			o1.setName("chris");
			
			Obj o2 = new Obj();
			o2.setAge(10);
			o2.setName("steven");
			objs.add(o1);
			objs.add(o2);
			String strOri = mapper.writeValueAsString(objs);
			byte[] bts = mapper.writeValueAsBytes(objs);
			System.out.println(strOri);
			List mapB = mapper.readValue(bts, List.class);
			List mapS = mapper.readValue(strOri, List.class);
			System.out.println(mapS);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

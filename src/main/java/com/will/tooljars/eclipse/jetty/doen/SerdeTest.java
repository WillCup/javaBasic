package com.will.tooljars.eclipse.jetty.doen;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerdeTest {
	static class X{
		Class clazz;
		Serializable data;
	}
	static class Model implements Serializable{
		public String a;
		public int b;
		public double c;
		public Date d;
	}
	/**
	 * @Title: main 
	 * @Description: TODO
	 * @param args
	 * @return void
	 * @throws 
	 * 2014-7-21 下午5:24:11 created by David Liu
	 */
	public static void main(String[] args)throws Exception {
		ObjectMapper om=new ObjectMapper();
		
		int n=1000000;
		long t=0;
		for(int i=0;i<n;i++){
			long s=System.nanoTime();
			X x=new X();
			Model m=new Model();
			x.data=m;
			x.clazz=m.getClass();
			m.a="fasdfasdf";
			m.b=2341234;
			m.c=23.234523;
			m.d=new Date();
			
			byte[] b=om.writeValueAsBytes(x);
			X mm=om.readValue(b, X.class);
			System.out.println(mm.data+"\t"+new String(b));
			
//			ByteArrayOutputStream outstream=new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(outstream);
//			oos.writeObject(m);
//			oos.flush();
//			oos.close();
//			byte[] b=outstream.toByteArray();
//			outstream.close();
//			
//			 ByteArrayInputStream instr = new ByteArrayInputStream(b);
//			 ObjectInputStream inputFromServlet = new ObjectInputStream(instr);
//			 Model mm=(Model) inputFromServlet.readObject();
//			 inputFromServlet.close();
//			 instr.close();
			
			t+=System.nanoTime()-s;
		}
		
		System.out.println(t/n);
	}

}

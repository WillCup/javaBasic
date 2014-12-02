package com.will.tooljars.eclipse.jetty.doen;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * @author David Liu
 * Created: 2014-7-24 下午5:49:44
 */
public class BaseHandler extends AbstractHandler{
	public void handle(String target, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String uri=baseRequest.getRequestURI();
		
		System.out.println(uri);
		
		InputStream is=request.getInputStream();
		byte[] b=new byte[1024];
		int c=0;
		while((c=is.read(b))!=-1){
			System.out.print(c+"");
		}
		
		response.getOutputStream().write("asd".getBytes());
		response.getOutputStream().flush();
	}

}

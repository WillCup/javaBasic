//package com.will.tooljars.eclipse.jetty;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.eclipse.jetty.server.Request;
//import org.eclipse.jetty.server.handler.AbstractHandler;
//
///**
// * To produce a response to a request, Jetty requires a Handler to be set on the server. A handler may:
//        
//        Examine/modify the HTTP request.
//        Generate the complete HTTP response.
//        Call another Handler (see HandlerWrapper).
//        Select one or many Handlers to call (see HandlerCollection).
// * 
// * @author Will
// * @created at 2014-8-13 下午5:43:28
// */
//public class HelloHandler extends AbstractHandler
//{
//    public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) 
//        throws IOException, ServletException
//    {
//        // The handler sets the response status, content-type and 
//        // marks the request as handled before it generates the body of the response using a writer.
//        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);  
//        baseRequest.setHandled(true);
//        response.getWriter().println("<h1>Hello World</h1>");
//    }
//
//}
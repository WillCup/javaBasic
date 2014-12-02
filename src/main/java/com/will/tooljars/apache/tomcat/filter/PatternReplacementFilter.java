package com.will.tooljars.apache.tomcat.filter;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PatternReplacementFilter implements Filter {
    ServletContext sc = null;
    
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
//        ServletContext servletContext = req.getServletContext();
        // check that it is a HTTP request
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            
            // nonce encode the normal output
            System.out.println("sc is : " + sc);
            PatternReplacementResponseWrapper wrappedResponse = new PatternReplacementResponseWrapper(
                    response, sc);
            
            wrappedResponse.setPattern("<tiltle>");
            wrappedResponse.setPattern("<title> Will");

            chain.doFilter(req, wrappedResponse);
            // finish the respone
            wrappedResponse.finishResponse();
        }
    }
    
    public void init(FilterConfig filterConfig) {
        // reference the context
        sc = filterConfig.getServletContext();
    }
    
    public void destroy() {
        // noop
    }
}
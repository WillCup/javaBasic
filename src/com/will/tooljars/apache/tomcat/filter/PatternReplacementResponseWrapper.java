package com.will.tooljars.apache.tomcat.filter;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PatternReplacementResponseWrapper extends HttpServletResponseWrapper {
    protected HttpServletResponse origResponse = null;
    protected ServletOutputStream stream = null;
    protected PrintWriter writer = null;
    ServletContext sc;
    String pattern = "<title>";
    String replacement = "<title> Will ";
    
    public PatternReplacementResponseWrapper(HttpServletResponse response, ServletContext sc) {
        super(response);
        this.sc = sc;
        origResponse = response;
    }
    
    public PatternReplacementResponseWrapper(HttpServletResponse response, ServletContext sc, String pattern, String replacement) {
        super(response);
        this.sc = sc;
        origResponse = response;
        this.pattern = pattern;
        this.replacement = replacement;
    }
    
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public ServletOutputStream createOutputStream() throws IOException {
        return (new PatternReplacementResponseStream(origResponse, sc, pattern, replacement));
    }
    
    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {}
    }
    
    public void flushBuffer() throws IOException {
        stream.flush();
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }
        
        if (stream == null)
            stream = createOutputStream();
        return (stream);
    }
    
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return (writer);
        }
        
        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }
        
        stream = createOutputStream();
        // BUG FIX 2003-12-01 Reuse content's encoding, don't assume UTF-8
        writer = new PrintWriter(new OutputStreamWriter(stream, origResponse.getCharacterEncoding()));
        return (writer);
    }
    
    public void setContentLength(int length) {}
}
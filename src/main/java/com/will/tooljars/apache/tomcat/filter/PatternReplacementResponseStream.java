package com.will.tooljars.apache.tomcat.filter;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.*;

public class PatternReplacementResponseStream extends ServletOutputStream {
    // abstraction of the output stream used for compression
    protected OutputStream bufferedOutput = null;

    // state keeping variable for if close() has been called
    protected boolean closed = false;

    protected String pattern = null;

    protected String replacement = "";

    // reference to original response
    protected HttpServletResponse response = null;

    // reference to the output stream to the client's browser
    protected ServletOutputStream output = null;

    // default size of the in-memory buffer
    private int bufferSize = 50000;

    ServletContext sc;

    public PatternReplacementResponseStream(HttpServletResponse response,
            ServletContext sc) throws IOException {
        super();
        closed = false;
        this.sc = sc;
        this.response = response;
        this.output = response.getOutputStream();
        bufferedOutput = new ByteArrayOutputStream();
    }

    public PatternReplacementResponseStream(HttpServletResponse response,
            ServletContext sc, String pattern, String replacement)
            throws IOException {
        this(response, sc);
        this.pattern = pattern;
        this.replacement = replacement;
    }

    public void close() throws IOException {
        // make up a nonce
        String nonce = Integer
                .toString((int) (Math.random() * Integer.MAX_VALUE));
        // set the nonce in app scope
        sc.setAttribute("nonce", nonce);

        // get the content
        ByteArrayOutputStream baos = (ByteArrayOutputStream) bufferedOutput;

        // make a string out of the response
        String pageText = new String(baos.toByteArray());

        if (pattern != null) {
            if (response.getContentType().contains("text/html")) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(pageText);
                int offset = 0;
                if (m.find(offset)) {
                    output.write(m.replaceAll(replacement).getBytes());
                }
            } else {
                output.write(baos.toByteArray());
            }
        } else {
            output.write(baos.toByteArray());
        }
        output.flush();
        output.close();
        closed = true;

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

    public void flush() throws IOException {
        if (closed) {
            throw new IOException("Cannot flush a closed output stream");
        }
        bufferedOutput.flush();
    }

    public void write(int b) throws IOException {
        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        // write the byte to the temporary output
        bufferedOutput.write((byte) b);
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        // write the content to the buffer
        bufferedOutput.write(b, off, len);
    }

    public boolean closed() {
        return (this.closed);
    }

    public void reset() {
        // noop
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setWriteListener(WriteListener arg0) {
        // TODO Auto-generated method stub
        
    }
}
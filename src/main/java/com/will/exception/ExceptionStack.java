package com.will.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionStack {
    public static void main(String[] args) {
        Exception exception = new Exception("hell, There?");
        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement e : stackTrace) {
            System.out.println(e.getClassName());
            System.out.println(e.getFileName());
            System.out.println(e.getMethodName());
            System.out.println(e.getLineNumber());
        }
        System.out.println(exception.getMessage());
        System.out.println(exception.toString());
        System.out.println("-------");
        exception.printStackTrace(new PrintWriter(System.out));
        
        System.out.println(getStackTrace(exception));
    }
    

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}

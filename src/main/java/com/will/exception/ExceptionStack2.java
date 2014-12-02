package com.will.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 可以用来写log
 * <br/><br/>
 * @author will
 *
 * @2014-10-22
 */
public class ExceptionStack2 {
    private static final Log logger = LogFactory.getLog(ExceptionStack2.class);
    public static void main(String[] args) {
        Throwable ex = new Throwable();
        StackTraceElement[] stackElements = ex.getStackTrace();
        try {
            Te.getd();
        } catch (Exception e) {
//            stackElements = e.getStackTrace();
        }
        stackElements = null;
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                System.out.println(stackElements[i].getClassName());//返回类的完全限定名，该类包含由该堆栈跟踪元素所表示的执行点。
                System.out.println(stackElements[i].getFileName());//返回源文件名，该文件包含由该堆栈跟踪元素所表示的执行点。
                System.out.println(stackElements[i].getLineNumber());//返回源行的行号，该行包含由该堆栈该跟踪元素所表示的执行点。
                System.out.println(stackElements[i].getMethodName());//返回方法名，此方法包含由该堆栈跟踪元素所表示的执行点。
                System.out.println("-------------第"+i+"级调用-------------------");
            }
        }

    }
    
}

class Te {
    public static void getd() throws Exception {
        SmartAgentConfiguration t = new SmartAgentConfiguration();
        t.getException(" you have a exception now....");
    }
}

class SmartAgentConfiguration {
    public static void getException(String msg) throws Exception {
        StackTraceElement[] stackElements;
        try {
            throw new Exception("this i1s a test");
        }  catch (Exception e) {
            stackElements = e.getStackTrace();
//            e.printStackTrace();
        }
        if (stackElements != null) {
//            for (int i = 0; i < stackElements.length; i++) {
            for (int i = 0; i < 2; i++) {
                if (i == 1) {
                    System.out.println(stackElements[i].getClassName() + " - " + stackElements[i].getLineNumber() + " - " + stackElements[i].getMethodName()
                            + " : " + msg);
                }
//                System.out.println(stackElements[i].getClassName());//返回类的完全限定名，该类包含由该堆栈跟踪元素所表示的执行点。
//                System.out.println(stackElements[i].getFileName());//返回源文件名，该文件包含由该堆栈跟踪元素所表示的执行点。
//                System.out.println(stackElements[i].getLineNumber());//返回源行的行号，该行包含由该堆栈该跟踪元素所表示的执行点。
//                System.out.println(stackElements[i].getMethodName());//返回方法名，此方法包含由该堆栈跟踪元素所表示的执行点。
//                System.out.println("-------------第"+i+"级调用-------------------");
            }
        }
    }
}


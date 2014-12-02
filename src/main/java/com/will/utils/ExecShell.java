package com.will.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.OperatingSystem;

import com.will.exception.ExceptionCollector;


public class ExecShell {
    public static final Log logger = LogFactory.getLog(ExecShell.class);

    /**
     * Return the result of the command stdout.
     *
     * <br/>
     * @param string
     * @return
     */
    public static String getExecCmdString(String string) {
        String result = null;
        BufferedReader reader = null;
        try {
            result = "";
            String tmp = "";
            reader = getExecCmdReader(string);
            while (StringUtils.isNotBlank((tmp = reader.readLine()))) {
                result += tmp;
            }
        } catch (Exception e) {
            ExceptionCollector.registerException(e);
            logger.error("Error happens in " + ExceptionCollector.getStackTrace(e));
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
                reader = null;
            }
        }
        return result;
    }
    
    /**
     * Always remember to release the resource --- close the reader in a finally block.
     *
     * <br/>
     * @param string
     * @return
     */
    public static BufferedReader getExecCmdReader(String string) {
        BufferedReader reader = null;
        try {
            String[] exeStrings = getExeStrings();
            exeStrings[2] = string;
            Process process = Runtime.getRuntime().exec(exeStrings);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
        } catch (Exception e) {
            ExceptionCollector.registerException(e);
            logger.error("Error happens in " + ExceptionCollector.getStackTrace(e));
        }
        return reader;
    }
    
    /**
     * Always remember to release the resource --- close the reader in a finally block.
     *
     * <br/>
     * @param string
     * @return
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static int execCmd(String string) throws InterruptedException, IOException {
        String[] exeStrings = getExeStrings();
        exeStrings[2] = string;
        
        for (String str : exeStrings) {
            System.out.println("command : " + str);
        }
//        Process process = Runtime.getRuntime().exec(exeStrings);
        Process process = Runtime.getRuntime().exec(string);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
        reader = null;
        int waitFor = process.waitFor();
        return waitFor;
    }

    /**
     * Prepare for the executions.
     *
     * <br/>
     * @return
     */
    public static String[] getExeStrings() {
        if (OperatingSystem.IS_WIN32) {
            return new String[]{"cmd", "/c", ""};
        } else {
            return new String[]{"/bin/sh", "-c", ""};
        }
    }
    
    public static void main(String[] args) throws Exception {
//        System.out.println(execCmd("/root/workspace/javaBasicTest/test-logs/test.sh"));
        Process exec = Runtime.getRuntime().exec("/root/workspace/javaBasicTest/test-logs/test.sh");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
        System.out.println("--------- error --------------");
        reader = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
        exec.waitFor();
        //        System.out.println(execCmdString);
    }
}

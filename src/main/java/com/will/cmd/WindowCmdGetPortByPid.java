package com.will.cmd;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.OperatingSystem;


public class WindowCmdGetPortByPid {
    public static final Log logger = LogFactory.getLog(WindowCmdGetPortByPid.class);
    public static void main(String[] args) {

        String pid = "1284";
        
        logger.info("----------------------------------------");
        logger.info("----------------------------------------");
        logger.info("we are going to get port for pid : " + pid);
        String str;
        String content = "";
        String[] exeStrs;
        if (OperatingSystem.IS_WIN32) {
            exeStrs = new String[] { "cmd", "/c", "netstat -ano | findStr " + pid };
        } else {
            exeStrs = new String[] { "sh", "-c", "netstat -anp|grep " + pid };
        }
        logger.info("Port query string is : " + exeStrs[2]);
        logger.info("Port query string is : " + exeStrs[2]);
        String[] port = null;
        try {
//            Process process = Runtime.getRuntime().exec("cmd /c netstat -ano | findStr 1284");
            Process process = Runtime.getRuntime().exec(exeStrs);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
//            String str1 = bufferedReader.readLine(); 
//            logger.info(str1);
            while ((str = bufferedReader.readLine()) != null) {
                content = content + str;
                logger.info("result content is : " + new String(content.getBytes("gbk"), "UTF-8"));
            }
            logger.info("Port query result is : " + str);
            if (str == null || str.trim().length() <= 0) {
                System.out.println("result is null");
            }
            logger.info("Port query result is : " + str);
//            port = str.split("\\s+")[3].split("[:]+");
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        logger.info("----------------------------------------");
        logger.info("----------------------------------------");
    }
}

package com.will.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.OperatingSystem;

public class ExecShell {
    public static final Log logger = LogFactory.getLog(ExecShell.class);

    public static String getExecCmdString(String string) {
        System.out.println(string);
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
            e.printStackTrace();
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
     * Must remember to close the reader.
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
            e.printStackTrace();
        }
        return reader;
    }

    public static String[] getExeStrings() {
        if (OperatingSystem.IS_WIN32) {
            return new String[]{"cmd", "/c", ""};
        } else {
            return new String[]{"sh", "-c", ""};
        }
    }
}

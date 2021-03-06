package com.will.tooljars.sigar;

import java.io.BufferedReader;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ptql.ProcessFinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.will.utils.ExecShell;

public class ApacheConfPath {
    public static void main(String[] args) throws Exception {
        Sigar sigar = new Sigar();
        ObjectMapper mapper = new ObjectMapper();
        long[] pids = ProcessFinder.find(sigar, getUnixPtql());
        for (long pid : pids) {
            System.out.println("sigar.getProcExe(pid) >>> " + mapper.writeValueAsString(sigar.getProcExe(pid)));
            System.out.println("sigar.getProcArgs(pid) >>> " + mapper.writeValueAsString(sigar.getProcArgs(pid)));
            System.out.println("sigar.getProcEnv(pid) >>> " + mapper.writeValueAsString(sigar.getProcEnv(pid)));
            System.out.println("sigar.getProcFd(pid) >>> " + mapper.writeValueAsString(sigar.getProcFd(pid)));
            String cmd = sigar.getProcExe(pid).getName();
            String finalCmd = null;
            if (cmd.endsWith("apache")) {
                finalCmd = cmd.replace("apache", "apachectl");
            } else if (cmd.endsWith("apache2")) {
                finalCmd = cmd.replace("apache2", "apache2ctl");
            } else {
                finalCmd = cmd;
            }
            BufferedReader reader = ExecShell.getExecCmdReader(finalCmd + " -V");
            String line = "";
            String httpRoot = null;
            String confFile = null;
            while (StringUtils.isNotBlank((line = reader.readLine()))) {
                if (line.trim().startsWith("-D HTTPD_ROOT")) {
                    String[] split = line.trim().split("\\s");
                    String argVal = split[1];
                    String[] keyVal = argVal.split("=");
                    httpRoot = keyVal[1].replaceAll("\"", "");
                    System.out.println(line + " ---->>> " + split[1] + " >>>>>>>>>>>>>>> " + keyVal[1]);
                } else if (line.trim().startsWith("-D SERVER_CONFIG_FILE")) {
                    String[] confFiles = line.split("=");
                    confFile = confFiles[1].replaceAll("\"", "");
                    System.err.println(confFiles[1]);
                }
            }
            String confFilePath = httpRoot + File.separator + confFile;
            System.out.println(confFilePath);
            System.out.println(FileUtils.readFileToString(new File(confFilePath)));
        }
    }


    public static String getUnixPtql() {
        return "State.Name.re=apache|apache2|httpd,State.Name.Pne=$1";
    }
}

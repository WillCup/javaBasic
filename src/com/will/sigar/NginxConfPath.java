package com.will.sigar;

import java.io.BufferedReader;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ptql.ProcessFinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.will.utils.ExecShell;

public class NginxConfPath {
    public static void main(String[] args) throws Exception {
        Sigar sigar = new Sigar();
        ObjectMapper mapper = new ObjectMapper();
        long[] pids = ProcessFinder.find(sigar, getUnixPtql());
        for (long pid : pids) {
            System.out.println("sigar.getProcExe(pid) >>> " + mapper.writeValueAsString(sigar.getProcExe(pid)));
            System.out.println("sigar.getProcArgs(pid) >>> " + mapper.writeValueAsString(sigar.getProcArgs(pid)));
            System.out.println("sigar.getProcEnv(pid) >>> " + mapper.writeValueAsString(sigar.getProcEnv(pid)));
            System.out.println("sigar.getProcFd(pid) >>> " + mapper.writeValueAsString(sigar.getProcFd(pid)));
            String finalCmd = sigar.getProcExe(pid).getName() + " -V";
            BufferedReader reader = null;
            String httpRoot;
            String confFile;
            try {
                reader = ExecShell.getExecCmdReader(finalCmd);
                String execCmdString = ExecShell.getExecCmdString(finalCmd);
                System.out.println(execCmdString);
                String line = "";
                httpRoot = null;
                confFile = null;
                while (StringUtils.isNotBlank((line = reader.readLine()))) {
                    System.out.println(line);
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            String confFilePath = httpRoot + File.separator + confFile;
            System.out.println(confFilePath);
            reader.close();
//            System.out.println(FileUtils.readFileToString(new File(confFilePath)));
        }
    }


    public static String getUnixPtql() {
        return "State.Name.re=nginx,State.Name.Pne=$1";
    }
}

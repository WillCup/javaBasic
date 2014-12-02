package com.will.tooljars.sigar;

import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.Sigar;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TomcatCatalinaHome {
    public static void main(String[] args) throws Exception {
        Sigar sigar = new Sigar();
        ObjectMapper mapper = new ObjectMapper();
        
        ProcExe procExe = sigar.getProcExe(10215);
        System.out.println(procExe.getName());
        System.out.println(procExe.getCwd());
    }
}

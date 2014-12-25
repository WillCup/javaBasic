package com.will.tooljars.sigar;

import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ptql.ProcessFinder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TomcatCatalinaHome {
    public static void main(String[] args) throws Exception {
        Sigar sigar = new Sigar();
        ObjectMapper mapper = new ObjectMapper();
        long[] pids = ProcessFinder.find(sigar, "State.Name.re=java|jsvc,State.Name.Pne=jsvc,Args.*.sw=-Dcatalina.base");
        for (long pid : pids) {
            ProcExe procExe = sigar.getProcExe(pid);
            System.out.println(procExe.getName());
            System.out.println(procExe.getCwd());
            
            String[] procArgs = sigar.getProcArgs(pid);
            for (String arg : procArgs) {
                System.out.println(arg);
            }
        }
    }
}

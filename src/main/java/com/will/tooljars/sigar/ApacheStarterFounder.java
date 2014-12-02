package com.will.tooljars.sigar;

import java.util.Map;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.ptql.ProcessFinder;

import com.will.utils.ExecShell;

/**
 * 业务场景:
 *  在加载完rum的module后,需要重启apache,所以需要找到apache到启动程序进行重启.
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-9-26
 */
public class ApacheStarterFounder {
    public static void main(String[] args) throws Exception {
        Sigar sigar = new Sigar();
        long[] pids = ProcessFinder.find(sigar, "State.Name.re=apache|apache2|httpd,State.Name.Pne=$1");
        for (long pid : pids) {
            Map procEnv = sigar.getProcEnv(pid);
            String[] procArgs = sigar.getProcArgs(pid);
            System.out.println(procEnv);
            for (String arg : procArgs) {
                System.out.println(arg);
            }
            
            String finalCmd;
            String cmd = sigar.getProcExe(pid).getName();
            if (cmd .endsWith("apache")) {
                finalCmd = cmd.replace("apache", "apachectl");
            } else if (cmd.endsWith("apache2")) {
                finalCmd = cmd.replace("apache2", "apache2ctl");
            } else {
                finalCmd = cmd;
            }
            ExecShell.execCmd(finalCmd + " -k stop");
            System.out.println("ApacheStarterFounder.main()");
        }
    }
}

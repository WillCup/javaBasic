package com.will.snmp;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnmpTest {
    public static void main(String[] args) {
        String ip = "10.0.1.44";
        String community = "public";

        List<String> oidList = new ArrayList<String>();
        //ssCpuRawUser
        oidList.add(".1.3.6.1.4.1.2021.11.50.0");
        //ssCpuRawNice
        oidList.add(".1.3.6.1.4.1.2021.11.51.0");
        //ssCpuRawSystem
        oidList.add(".1.3.6.1.4.1.2021.11.52.0");
        //ssCpuRawIdle
        oidList.add(".1.3.6.1.4.1.2021.11.53.0");
        //ssCpuRawWait
        oidList.add(".1.3.6.1.4.1.2021.11.54.0");
        //ssCpuRawKernel
        //oidList.add(".1.3.6.1.4.1.2021.11.55.0");
        //ssCpuRawInterrupt
        oidList.add(".1.3.6.1.4.1.2021.11.56.0");
        //ssCpuRawSoftIRQ
        oidList.add("1.3.6.1.4.1.2021.11.61.0");
        // 异步采集数据
        List cpuData = SnmpGetAsyn.snmpAsynGetList(ip, community, oidList);
        
        double ssCpuRawUser =  Double.parseDouble(cpuData.get(0).toString());
        double ssCpuRawNice =  Double.parseDouble(cpuData.get(1).toString());
        double ssCpuRawSystem = Double.parseDouble(cpuData.get(2).toString());
        double ssCpuRawIdle =  Double.parseDouble(cpuData.get(3).toString());
        double ssCpuRawWait =  Double.parseDouble(cpuData.get(4).toString());
        double ssCpuRawInterrupt =  Double.parseDouble(cpuData.get(5).toString());
        double ssCpuRawSoftIRQ = Double.parseDouble(cpuData.get(6).toString());
        Map<String, Object> jsonMap = new HashMap<String, Object>();//定义map
        

        double cpuRatio = 100*(ssCpuRawUser+ssCpuRawNice+ssCpuRawSystem+ssCpuRawWait+ssCpuRawInterrupt+ssCpuRawSoftIRQ)/(ssCpuRawUser+ssCpuRawNice
                +ssCpuRawSystem+ssCpuRawIdle+ssCpuRawWait+ssCpuRawInterrupt+ssCpuRawSoftIRQ);
        
        System.out.println("CPU利用率："+cpuRatio);
        
        jsonMap.put("y",cpuRatio);
        jsonMap.put("x", new Date().getTime());
        System.out.println(jsonMap);
    }
}

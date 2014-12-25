package com.will.tooljars.javassist.will;

import java.io.File;

import com.sun.tools.attach.VirtualMachine;

public class TomcatFilterMonitor {
    public static void main(String[] args) throws Exception {
        System.out.println("begin...");
        VirtualMachine vm = VirtualMachine.attach("9280");
//        Properties agentProperties = vm.getAgentProperties();
//        String javaHome = agentProperties.getProperty("java.home");
//        String jmxAgent = javaHome + File.separator + "lib" + File.separator
//                + "management-agent.jar";
        String agentJar = "E:/cloudwise-hostagent-code-0.0.1-SNAPSHOT.jar";
        File file = new File(agentJar);
        if (!file.exists()) {
            System.out.println(file.getName() + " not found");
        }
        System.out.println("TomcatFilterMonitor.main()");
        vm.loadAgent(agentJar);
        System.out.println("dsfsdf");
        vm.detach();
        System.out.println("end...");
    }
}

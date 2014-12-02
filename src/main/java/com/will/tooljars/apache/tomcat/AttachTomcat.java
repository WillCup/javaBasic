package com.will.tooljars.apache.tomcat;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.sun.tools.attach.VirtualMachine;
import com.will.Consts;
import com.will.tools.virtualMachine.VMUtils;

/**
 * 获取运行时的tomcat的一些环境变量
 * 
 * @author Will
 * @created at 2014-8-30 上午11:21:08
 */
public class AttachTomcat {
    
    
    public static void main(String[] args) {
        try {
            VirtualMachine vm = VMUtils.getVMByPid("7432");
            Properties props = vm.getAgentProperties();
            props.list(System.out);
            String catalinaHome = props.getProperty(Consts.CATALINA_HOME_PROP);
            System.out.println(catalinaHome);
            String j2eeHome = props.getProperty("com.sun.enterprise.home");
            System.out.println(j2eeHome);
            catalinaHome=props.getProperty("com.sun.enterprise.home");
            System.out.println(catalinaHome);
            catalinaHome = props.getProperty(Consts.CATALINA_BASE_PROP);
            System.out.println(catalinaHome);
            catalinaHome=props.getProperty("user.dir");
            System.out.println(catalinaHome);
            String temp = props.getProperty("java.io.tmpdir");
            System.out.println(temp);
            VMUtils.detachVM();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}

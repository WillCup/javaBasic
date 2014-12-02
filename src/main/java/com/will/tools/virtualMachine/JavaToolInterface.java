package com.will.tools.virtualMachine;

import java.io.File;
import java.util.List;
import java.util.Properties;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class JavaToolInterface {
	public static void main(String[] args) throws Exception{
			// attach to target VM
//	        VirtualMachine vm = VirtualMachine.attach("5148");
	
	        // get system properties in target VM
//	        Properties props = vm.getSystemProperties();
	    
//	        System.out.println(props);
//	        System.out.println(vm.getAgentProperties());
	
	        // construct path to management agent
//	        String home = props.getProperty("java.home");
//	        String agent = home + File.separator + "lib" + File.separator + "management-agent.jar";
	
	        // load agent into target VM
	//        vm.loadAgent(agent, "com.sun.management.jmxremote.port=5000");
	        
	        List<VirtualMachineDescriptor> list= VirtualMachine.list();
	        for(VirtualMachineDescriptor vmd:list){
	        	System.out.println("displayName="+vmd.displayName());
	        	System.out.println("id="+vmd.id());
	        	System.out.println("vmd.provider().type()="+vmd.provider().type());
	        	System.out.println("vmd.provider().name()="+vmd.provider().name());
	        }
	        
	        // detach
	//        vm.detach();
	
		}
}


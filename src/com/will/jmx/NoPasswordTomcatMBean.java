package com.will.jmx;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class NoPasswordTomcatMBean {
    public static void main(String[] args) throws AttachNotSupportedException,
            IOException, AgentLoadException, AgentInitializationException, MalformedObjectNameException, InstantiationException, IllegalAccessException, ClassNotFoundException, InstanceNotFoundException, IntrospectionException, ReflectionException, AttributeNotFoundException, MBeanException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor des : list) {
            System.out.println("name is : " + des.displayName() + " ; provider name is : " + des.provider().name());
        }
        
        VirtualMachine virtualmachine = VirtualMachine.attach("5940");
        
        // 让JVM加载jmx Agent，后续讲到Java Instrutment再讲解
        String javaHome = virtualmachine.getSystemProperties().getProperty("java.home");
        String jmxAgent = javaHome + File.separator + "lib" + File.separator
                + "management-agent.jar";
        System.out.println("jmxAgent : " + jmxAgent);
        virtualmachine.loadAgent(jmxAgent, "com.sun.management.jmxremote");
        // 获得连接地址
        Properties properties = virtualmachine.getAgentProperties();
        String address = (String) properties
                .get("com.sun.management.jmxremote.localConnectorAddress");
        // Detach
        virtualmachine.detach();
        System.out.println("address : " + address);
        JMXServiceURL url = new JMXServiceURL(address);
        JMXConnector connector = JMXConnectorFactory.connect(url);
        MBeanServerConnection mBeanServerConnection = connector.getMBeanServerConnection();
        RuntimeMXBean rmxb = ManagementFactory.newPlatformMXBeanProxy(
                mBeanServerConnection, "java.lang:type=Runtime",
                RuntimeMXBean.class);
        System.out.println(rmxb.getBootClassPath());

        
        
        Set<ObjectInstance> mbeans = mBeanServerConnection.queryMBeans(new ObjectName("Catalina:type=Host,host=*"), null);
        for (ObjectInstance mbean : mbeans) {
            System.out.println("---------------------------------------------------");
            String className = mbean.getClassName();
            MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(mbean.getObjectName());
            MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
            ObjectName objName = mbean.getObjectName();
            System.out.println(objName);
            for (MBeanAttributeInfo info : attributes) {
//                System.out.println(info.getName() + "  >>> " + mBeanServerConnection.getAttribute(mbean.getObjectName(), info.getName()));
            }
            System.out.println("className : " + className);
        }
        
    }
}

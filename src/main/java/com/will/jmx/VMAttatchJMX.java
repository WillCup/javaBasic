package com.will.jmx;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.tools.attach.VirtualMachine;

public class VMAttatchJMX {

    public static void main(String[] args) throws Exception {

        // Attach 到5656的JVM进程上，后续Attach API再讲解
        VirtualMachine virtualmachine = VirtualMachine.attach("1316");

        // 让JVM加载jmx Agent，后续讲到Java Instrutment再讲解
        String javaHome = virtualmachine.getSystemProperties().getProperty(
                "java.home");
        String jmxAgent = javaHome + File.separator + "lib" + File.separator
                + "management-agent.jar";
        virtualmachine.loadAgent(jmxAgent, "com.sun.management.jmxremote");

        // 获得连接地址
        Properties properties = virtualmachine.getAgentProperties();
        String address = (String) properties
                .get("com.sun.management.jmxremote.localConnectorAddress");

        // Detach
        virtualmachine.detach();

        JMXServiceURL url = new JMXServiceURL(address);
        JMXConnector connector = JMXConnectorFactory.connect(url);
        MBeanServerConnection mBeanServerConnection = connector
                .getMBeanServerConnection();
        RuntimeMXBean rmxb = ManagementFactory.newPlatformMXBeanProxy(
                mBeanServerConnection, "java.lang:type=Runtime",
                RuntimeMXBean.class);
        System.out.println("uptime : " + rmxb.getUptime());

        ObjectName objectName = ObjectName
                .getInstance("com.bea:Name=myserver,Type=WebServer,Server=myserver");
        Set<ObjectName> ons = mBeanServerConnection
                .queryNames(objectName, null);
        for (ObjectName on : ons) {
            MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(on);
            MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
            for (MBeanAttributeInfo attr : attributes) {

                Object obj = mBeanServerConnection.getAttribute(objectName,
                        attr.getName());
                String rtn;
                if (obj == null) {
                    System.out.println(attr.getName() + " for object :"
                            + objectName + " is null");
                    continue;
                }
                if (obj instanceof TabularDataSupport) {
                    TabularDataSupport tds = (TabularDataSupport) obj;
                    rtn = stringTabularDataSupport(tds);
                } else if (obj instanceof CompositeDataSupport) {
                    // CompositeDataSupport cds=(CompositeDataSupport)obj;
                    // System.out.println(stringCompositeData(cds));
                } else if (obj instanceof CompositeData) {
                    CompositeData cd = (CompositeData) obj;
                    rtn = stringCompositeData(cd).toString();
                } else if (obj instanceof ObjectName) {
                    // ObjectName on=(ObjectName)obj;
                    // MBeanInfo mbi2 = conn.getMBeanInfo(on);
                    // stringMBeanInfo(conn, on, mbi2);
                    // System.out.println("\t"+mbi2);
                } else if (obj instanceof String[]) {
                    String[] sa = (String[]) obj;
                    StringBuilder sb = new StringBuilder();
                    for (String s : sa) {
                        sb.append(s);
                    }
                    rtn = sb.toString();
                } else if (obj instanceof long[]) {
                    long[] sa = (long[]) obj;
                    StringBuilder sb = new StringBuilder();
                    for (long s : sa) {
                        sb.append(s);
                    }
                    rtn = sb.toString();
                } else if (obj instanceof ObjectName[]) {
                    // ObjectName[] sa=(ObjectName[])obj;
                    // for(ObjectName s:sa){
                    // System.out.println("\tObjectName="+s);
                    // MBeanInfo mbi2 = conn.getMBeanInfo(s);
                    // stringMBeanInfo(conn, s, mbi2);
                    // }
                } else if (obj instanceof CompositeData[]) {
                    CompositeData[] sa = (CompositeData[]) obj;
                    StringBuilder sb = new StringBuilder();
                    for (CompositeData s : sa) {
                        sb.append(stringCompositeData(s));
                    }
                    rtn = sb.toString();
                } else {
                    rtn = (obj.toString());
                    String classType = obj.getClass().toString();
                    if (classType.startsWith("Ljava.lang")
                            && classType.length() > 2) {
                        /**
                         * If coming dowon to here, that means some types are
                         * not covered, then needs to add more logics.
                         */
                        System.out.println("*******************" + classType);
                    }
                    System.out.println(attr.getName() + " -- \t"
                            + attr.getType() + " -- " + rtn);
                }

            }
        }
        System.out.println(ons);
    }

    public static Map<String, Object> stringCompositeData(CompositeData cd) {
        Map<String, Object> rtn = new HashMap<String, Object>();
        Set<String> keys = cd.getCompositeType().keySet();
        for (String key : keys) {
            rtn.put(key, cd.get(key));
        }
        return rtn;
    }

    public static String stringMBeanInfo(MBeanServerConnection conn,
            ObjectName objName, MBeanInfo mbi) throws Exception {
        StringBuilder sb = new StringBuilder();
        MBeanAttributeInfo[] mbAttributes = mbi.getAttributes();
        for (MBeanAttributeInfo mbai : mbAttributes) {
            String attrName = mbai.getName();
            String tmp1 = "\r\nObjectName=" + objName + "\tAttributeName="
                    + attrName + "\twritable=" + mbai.isWritable()
                    + "\treadable=" + mbai.isReadable();
            System.out.println(tmp1);
            tmp1 += "\r\n";
            String attrValue = getAttributeValue(conn, objName, attrName);
            // System.out.println("\t"+attrValue);
            tmp1 += attrValue + "\r\n";
            sb.append(tmp1);
        }
        return sb.toString();
    }

    public static String stringTabularDataSupport(TabularDataSupport tds) {
        StringBuilder sb = new StringBuilder();
        Set<Entry<Object, Object>> entrySet = tds.entrySet();
        for (Entry<Object, Object> e : entrySet) {
            CompositeDataSupport cds = (CompositeDataSupport) e.getValue();
            sb.append(e.getKey() + "=" + cds.values() + "\r\n");
        }
        return sb.toString();
    }

    public static String getAttributeValue(MBeanServerConnection conn,
            ObjectName objName, String attrName) {
        String rtn = null;
        try {
            Object obj = conn.getAttribute(objName, attrName);
            if (obj instanceof TabularDataSupport) {
                TabularDataSupport tds = (TabularDataSupport) obj;
                rtn = stringTabularDataSupport(tds);
            } else if (obj instanceof CompositeDataSupport) {
                // CompositeDataSupport cds=(CompositeDataSupport)obj;
                // System.out.println(stringCompositeData(cds));
            } else if (obj instanceof CompositeData) {
                CompositeData cd = (CompositeData) obj;
                rtn = stringCompositeData(cd).toString();
            } else if (obj instanceof ObjectName) {
                // ObjectName on=(ObjectName)obj;
                // MBeanInfo mbi2 = conn.getMBeanInfo(on);
                // stringMBeanInfo(conn, on, mbi2);
                // System.out.println("\t"+mbi2);
            } else if (obj instanceof String[]) {
                String[] sa = (String[]) obj;
                StringBuilder sb = new StringBuilder();
                for (String s : sa) {
                    sb.append(s);
                }
                rtn = sb.toString();
            } else if (obj instanceof long[]) {
                long[] sa = (long[]) obj;
                StringBuilder sb = new StringBuilder();
                for (long s : sa) {
                    sb.append(s);
                }
                rtn = sb.toString();
            } else if (obj instanceof ObjectName[]) {
                // ObjectName[] sa=(ObjectName[])obj;
                // for(ObjectName s:sa){
                // System.out.println("\tObjectName="+s);
                // MBeanInfo mbi2 = conn.getMBeanInfo(s);
                // stringMBeanInfo(conn, s, mbi2);
                // }
            } else if (obj instanceof CompositeData[]) {
                CompositeData[] sa = (CompositeData[]) obj;
                StringBuilder sb = new StringBuilder();
                for (CompositeData s : sa) {
                    sb.append(stringCompositeData(s));
                }
                rtn = sb.toString();
            } else {
                rtn = (obj.toString());
                String classType = obj.getClass().toString();
                if (classType.startsWith("Ljava.lang")
                        && classType.length() > 2) {
                    /**
                     * If coming dowon to here, that means some types are not
                     * covered, then needs to add more logics.
                     */
                    System.out.println("*******************" + classType);
                }
            }
        } catch (Exception ex) {
        }
        return rtn;
    }
}

package com.will.jmx;

import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class Tomcat {
	public static void main(String[] args) {
		try {
			String jmxURL = "service:jmx:rmi:///jndi/rmi://10.0.1.44:8999/jmxrmi";
			JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);

			Map map = new HashMap();
			// 用户名密码，在jmxremote.password文件中查看
			String[] credentials = new String[] { "controlRole", "R&D" };
			map.put("jmx.remote.credentials", credentials);
			JMXConnector connector = JMXConnectorFactory.connect(serviceURL, map);
//			JMXConnector connector = JMXConnectorFactory.connect(serviceURL);
			MBeanServerConnection mbsc = connector.getMBeanServerConnection();

			// 端口最好是动态取得
			ObjectName threadObjName = new ObjectName("Catalina:type=ThreadPool,name=http-8080");
			MBeanInfo mbInfo = mbsc.getMBeanInfo(threadObjName);

			// tomcat的线程数对应的属性值
			String attrName = "currentThreadCount";
			MBeanAttributeInfo[] mbAttributes = mbInfo.getAttributes();
			System.out.println("currentThreadCount:" + mbsc.getAttribute(threadObjName, attrName));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

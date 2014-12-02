package com.will.jmx;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXUtils {

	private static JMXConnector jmxConnector;

	/**
	 * Get a JMX connection 2014-6-27 下午4:11:18 created by David Liu
	 */
	public static MBeanServerConnection conn(String ip, int port,
			String username, String password) {
		MBeanServerConnection mbsc = null;
		try {
			Map<String, String[]> map = new HashMap<String, String[]>();
			// map.put(Context.SECURITY_PRINCIPAL, username);
			// map.put(Context.SECURITY_CREDENTIALS, password);

			String[] credentials = new String[] { username, password };
			map.put("jmx.remote.credentials", credentials);
			JMXServiceURL u = new JMXServiceURL(
					"service:jmx:rmi:///jndi/rmi://" + ip + ":" + port
							+ "/jmxrmi");
			jmxConnector = JMXConnectorFactory.connect(u, map);
			mbsc = jmxConnector.getMBeanServerConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mbsc;
	}

	public static synchronized void close() {
		try {
			jmxConnector.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get all MBeans 2014-6-27 下午3:07:33 created by David Liu
	 */
	public static Map<ObjectName, MBeanInfo> getAllMBean(
			MBeanServerConnection mbsc) {
		Map<ObjectName, MBeanInfo> rtn = new HashMap<ObjectName, MBeanInfo>();
		try {
			ObjectName qe = ObjectName.getInstance("");
			Set<ObjectName> ons = mbsc.queryNames(qe, null);
			for (ObjectName on : ons) {
				rtn.put(on, mbsc.getMBeanInfo(on));
			}
		} catch (Exception e) {

		}
		return rtn;
	}

	/**
	 * Convert TabularDataSupport to String 2014-6-27 下午3:07:53 created by David
	 * Liu
	 */
	public static String stringTabularDataSupport(TabularDataSupport tds) {
		StringBuilder sb = new StringBuilder();
		for (Entry<Object, Object> e : tds.entrySet()) {
			CompositeDataSupport cds = (CompositeDataSupport) e.getValue();
			sb.append(e.getKey() + "=" + cds.values() + "\r\n");
		}
		return sb.toString();
	}

	/**
	 * Convert CompositeData to String 2014-6-27 下午3:08:04 created by David Liu
	 */
	public static Map<String, Object> stringCompositeData(CompositeData cd) {
		Map<String, Object> rtn = new HashMap<String, Object>();
		Set<String> keys = cd.getCompositeType().keySet();
		for (String key : keys) {
			rtn.put(key, cd.get(key));
		}
		return rtn;
	}

	public static void main(String[] args) throws Exception {
		FileOutputStream fos = new FileOutputStream(
				"/usr/local/will/test/tomcatjmx-44.txt");
		MBeanServerConnection conn = conn("127.0.0.1", 8999, "controlRole",
				"R&D");

		StringBuilder sb = new StringBuilder("[Domains]\r\n");
		String[] domains = conn.getDomains();
		for (String s : domains) {
			sb.append(s + "\r\n");
		}

		sb.append("\r\n[All ObjectInstance]\r\n");
		ObjectName qe = ObjectName.getInstance("");
		Set<ObjectInstance> ois = conn.queryMBeans(qe, null);
		for (ObjectInstance oi : ois) {
			sb.append(oi + "\r\n");
		}

		sb.append("\r\n[All ObjectInstance and Their Attributes]\r\n");
		Set<ObjectName> ons = conn.queryNames(qe, null);
		for (ObjectName on : ons) {
			sb.append(on + "\r\n");
			MBeanInfo mbInfo = conn.getMBeanInfo(on);
			MBeanAttributeInfo[] mbAttributes = mbInfo.getAttributes();
			for (MBeanAttributeInfo mbai : mbAttributes) {
				sb.append("\tattributeName=" + mbai.getName() + "\ttype="
						+ mbai.getType() + "\r\n");
			}
		}
		fos.write(sb.toString().getBytes());

		Map<ObjectName, MBeanInfo> mbs = getAllMBean(conn);
		for (Map.Entry<ObjectName, MBeanInfo> e : mbs.entrySet()) {
			ObjectName objName = e.getKey();
			MBeanInfo mbi = e.getValue();
			String tmp1 = stringMBeanInfo(conn, objName, mbi);
			fos.write(tmp1.getBytes());
		}
		fos.close();
	}

	/**
	 * @desc
	 * @return void
	 * @since 2014 11:42:35
	 * @author David Liu
	 */
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

	/**
	 * Given an ObjectName and its attribute name, retrieve that attribute's
	 * value 2014-6-27 下午3:08:35 created by David Liu
	 */
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

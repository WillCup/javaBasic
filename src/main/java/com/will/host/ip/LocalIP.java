package com.will.host.ip;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * 业务背景： 做主机监控的时候，使用java api获取本地ip的时候总是返回127.0.0.1 获取本机IP，非127.0.0.1
 */
public class LocalIP {
	public static void main(String[] args) throws UnsupportedEncodingException {

		Collection<InetAddress> colInetAddress = getAllHostAddress();

		for (InetAddress address : colInetAddress) {
			System.out.println(new String(address.getAddress(), "utf-8"));
			if (!address.isLoopbackAddress()) {
				System.out.println("IP is loop back :" + address.getHostAddress());
				if (!address.getHostAddress().contains(":")) {
//					if (address.isLinkLocalAddress()) {
//						System.err.println("dfd");
//					}
//					if (InOrOut.isInnerIP(address.getHostAddress())) {
//						System.out.println(address.getHostAddress() + " is inner ip");
//					}
//					System.out.println("no contains :" + address.getHostAddress());
//					String[] split = address.getHostAddress().split("\\.");
//					for (String s : split) {
//						System.out.println(s);
//					}
//					Integer ipHead = Integer.valueOf(split[0]);
//					System.out.println("belongs to : " + MyIp.TellIpType(ipHead));
					System.out.println("<font color='red'>  Final result IP  </font> : " + address.getHostAddress());
				}
			}
		}
		
//		try {
//			InetAddress addr = InetAddress.getLocalHost();
//			System.out.println(addr.getHostAddress());
//			System.out.println(InetAddress.getLoopbackAddress());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
	}

	public static Collection<InetAddress> getAllHostAddress() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			Collection<InetAddress> addresses = new ArrayList<InetAddress>();

			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces
						.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface
						.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					addresses.add(inetAddress);
				}
			}

			return addresses;
		} catch (SocketException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
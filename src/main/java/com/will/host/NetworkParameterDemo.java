package com.will.host;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
 
public class NetworkParameterDemo {
  public static void main(String[] args) throws Exception {
    Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
    while (en.hasMoreElements()) {
      NetworkInterface ni = en.nextElement();
      printParameter(ni);
    }
  }
 
  public static void printParameter(NetworkInterface ni) throws SocketException {
    System.out.println(" Name = " + ni.getName());
    System.out.println(" Display Name = " + ni.getDisplayName());
    System.out.println(" Is up = " + ni.isUp());
    System.out.println(" Support multicast = " + ni.supportsMulticast());
    System.out.println(" Is loopback = " + ni.isLoopback());
    System.out.println(" Is virtual = " + ni.isVirtual());
    System.out.println(" Is point to point = " + ni.isPointToPoint());
    System.out.println(" Hardware address = " + ni.getHardwareAddress());
    System.out.println(" MTU = " + ni.getMTU());
    byte[] mac = ni.getHardwareAddress();
    if (mac != null) {
    	System.out.println("mac数组长度："+mac.length);
		StringBuffer sb = new StringBuffer("");
		for(int i=0; i<mac.length; i++) {
			if(i!=0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i]&0xff;
			String str = Integer.toHexString(temp);
			System.out.println("每8位:"+str);
			if(str.length()==1) {
				sb.append("0"+str);
			}else {
				sb.append(str);
			}
		}
		System.out.println("本机MAC地址:"+sb.toString().toUpperCase());
    }
 
    System.out.println("\nList of Interface Addresses:");
    List<InterfaceAddress> list = ni.getInterfaceAddresses();
    Iterator<InterfaceAddress> it = list.iterator();
    
    while (it.hasNext()) {
      InterfaceAddress ia = it.next();
      System.out.println(" Address = " + ia.getAddress());
      System.out.println(" Broadcast = " + ia.getBroadcast());
      System.out.println(" Network prefix length = " + ia.getNetworkPrefixLength());
      System.out.println("");
    }
  }
}
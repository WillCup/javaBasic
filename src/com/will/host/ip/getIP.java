package com.will.host.ip;
import java.net.*;
import java.util.*;

public class getIP {
    public static void main(String[] args) {
  
    getIP t = new getIP();
    System.out.println(t.getLocalIP());
    System.out.println(t.getMacAddr());
    }

    public String getMacAddr() {    
        String MacAddr = "";
        String str = "";
        try {
            NetworkInterface NIC = NetworkInterface.getByName("eth0");
            byte[] buf = NIC.getHardwareAddress();
            for (int i = 0; i < buf.length; i++) {
                str = str + byteHEX(buf[i]);
            }
            MacAddr = str.toUpperCase();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return MacAddr;
    }

    public String getLocalIP() {
        String ip = "";
        try {
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                if (!ni.getName().equals("eth0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address)
                            continue;
                        ip = ia.getHostAddress();
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return ip;
    }

 /* 一个将字节转化为十六进制ASSIC码的函数 */
    public static String byteHEX(byte ib) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a','b', 'c', 'd', 'e', 'f' };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
}
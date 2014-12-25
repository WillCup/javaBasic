package com.will.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

public class IPGetter {
    public static void main(String[] args) {
        // test2();
        String strUrl = "Http://www.baidu.com";
        try {

            URL url = new URL(strUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            String s = "";
            StringBuffer sb = new StringBuffer("");
            String webContent = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "rn");
            }

            br.close();
            webContent = sb.toString();
            int start = webContent.indexOf("[") + 1;
            int end = webContent.indexOf("]");
            System.out.println("webContent=" + webContent);
            System.out.println("start=" + start);
            System.out.println("end=" + end);
            if (start < 0 || end < 0) {
            }
            webContent = webContent.substring(start, end);
            System.out.println(webContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2() {
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                System.out.println("DisplayName:" + ni.getDisplayName());
                System.out.println("Name:" + ni.getName());
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    System.out.println("IP:"
                            + ips.nextElement().getHostAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

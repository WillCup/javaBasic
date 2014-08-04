
   package com.will.host.ip;

 import java.net.*;

 /*
  *  getAddress方法和getHostAddress类似，它们的唯一区别是getHostAddress方法返回的是字符串形式的IP地址，
  *  而getAddress方法返回的是byte数组形式的IP地址。
  *  Java中byte类型的取值范围是-128?127。如果返回的IP地址的某个字节是大于127的整数，在byte数组中就是负数。
  *  由于Java中没有无符号byte类型，因此，要想显示正常的IP地址，必须使用int或long类型。
  */
 public class MyIp
 {
     public static void main(String[] args) throws Exception
     {
//         InetAddress ia = InetAddress.getByName("www.cnblogs.com");
    	 InetAddress ia = InetAddress.getLocalHost();
         byte ip[] = ia.getAddress();
         /*
         for (byte part : ip)
             System.out.print(part + " ");
         System.out.println("");
         for (byte part : ip)
         {
             int newIp = (part < 0) ? 256 + part : part;
             System.out.print(newIp + " ");
         }
         */
         int[] array = new int[5];
         for(int i=0; i<ip.length; i++) {
             array[i] = (ip[i] < 0) ? 256 + ip[i]  : ip[i];

         }
         String str = TellIpType(array[0]);
         System.out.println(str);

         
     }
     /*
      * 根据第一个字节判断IP地址类型
      */
     public static String TellIpType(int num) {
         if(num<127)
             return "A";
         else if(num<192)
             return "B";
         else if(num<224)
             return "C";
         else if(num<240)
             return "D";
         else
             return "E";
     }
 }
   
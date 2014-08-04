///==============================获取CPU序列号========
package com.will.host;
import java.io.IOException;
import java.util.Scanner;

public class CpuUtil {
 public static void main(String[] args) throws IOException {
  long start = System.currentTimeMillis();
  Process process = Runtime.getRuntime().exec(
    new String[] { "wmic", "cpu", "get", "ProcessorId" });
  process.getOutputStream().close();
  Scanner sc = new Scanner(process.getInputStream());
  String property = sc.next();
  String serial = sc.next();
  System.out.println(property + ": " + serial);
  System.out.println("time:" + (System.currentTimeMillis() - start));
 }
}
 
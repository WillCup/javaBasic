package com.will.str;

/**
 * 测试apache，::80中对于特殊符号::的过滤；
 * 业务场景： 使用service name 和 service main port来唯一标志一个主机上的一个service. 
 * 但是在命令行输出的字符里截取到的端口号有::80这种情况，很不美观；
 * <br/><br/>
 * @author will
 *
 * @2014-7-28
 */
public class SpecialChar {
	public static void main(String[] args) {
		String serviceName = "apache";
		String servicePort = "::80";
		for (String s : servicePort.split("[:]+")) {
			if (s.length() > 0) {
				System.out.println(s);
			}
		}
	}
}

package com.will.log;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SendMail {
	private final static Log logger = LogFactory.getLog(SendMail.class);
	public static void main(String[] args) throws InterruptedException {
		while (true) {
			logger.info("THis is a test");
			try {
				throw new Exception("hello");
			} catch (Exception e) {
				logger.error(formatErrorMsg(e));
			}
			Thread.sleep(3000);
		}
	}
	
	private static String formatErrorMsg(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		StringBuilder result = new StringBuilder();
		result.append("\n\n\n\n");
		result.append("--------------------StackTrace---------------------\n");
		result.append(sw);
		result.append("\n\n");
		result.append("--------------------Cause---------------------\n");
		result.append(e.getCause());
		result.append("\n\n");
		result.append("--------------------Message---------------------\n");
		result.append(e.getMessage());
		result.append("\n\n");
		return result.toString();
	}
}

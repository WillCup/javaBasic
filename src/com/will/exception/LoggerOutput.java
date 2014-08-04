package com.will.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerOutput {
	private static final Log logger = LogFactory.getLog(LoggerOutput.class);
	public static void main(String[] args) {
		try {
			throw new Exception("test for stackflow");
		} catch (Exception e) {
			StringWriter writer = new StringWriter();
			PrintWriter s = new PrintWriter(writer, true);
			e.printStackTrace(s );
			logger.info(writer.getBuffer().toString());
		}
	}
}

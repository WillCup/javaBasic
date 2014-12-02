package com.will.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutilLog {
	private final static Logger logger = LoggerFactory.getLogger(MutilLog.class);
	private final static Logger logger2 = LoggerFactory.getLogger("loggerByName");
	private final static Logger logger3 = LoggerFactory.getLogger("loggerByLevel");
	
	public static void main(String[] args) {
		logger.info("this is a test, from logger by class");
		logger2.info("logger by name");
		logger3.error("error message");
	}
}

package com.will.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Collect all exceptions thrown in this application, and send them to Server.
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-7-25
 */
public abstract class ExceptionCollector {
	
	private final static Log logger = LogFactory.getLog(ExceptionCollector.class);
	
	private static ConcurrentMap<String, Throwable> exceptions = new ConcurrentHashMap<String, Throwable>();

	private static ConcurrentMap<String, Integer> exceptionsCounter = new ConcurrentHashMap<String, Integer>();
	
	/**
	 * XXX The exceptions and counters since the project has started. Maybe too large. So just leave it aside for now.
	 */
//	private ConcurrentMap<String, Integer> exceptionsTotalCounter = new ConcurrentHashMap<String, Integer>();
	
	private static long startTime;
	private static long stopTime;
	
    /**
     * DateFormat is not thread-safe object. But the ExceptionCollector.populateSelfMetrics
     * should only be invoked by WatcherManager.moniter. So only one thread access this object.
     */
    private static final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Register an exception to collector. It includes two parts:
	 * 	1. Save it into exceptions map, name -> Throwable;
	 * 	2. Save the counter info for every Throwable object, also use the name as the key.
	 *
	 * <br/>
	 * @param throwable
	 */
	public static void registerException(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		String name = throwable.getClass().getName();
		exceptions.put(name,throwable);
		if (exceptionsCounter.get(name) != null) {
			exceptionsCounter.put(name, exceptionsCounter.get(name) + 1);
		}
//		if (exceptionsTotalCounter.get(name) != null) {
//			exceptionsTotalCounter.put(name, exceptionsTotalCounter.get(name) + 1);
//		}
	}

	/**
	 * Populate the metrics to console.
	 *
	 * <br/>
	 */
	public static void populateSelfMetrics() {
		logger.info(" ------------------------------------------------------------------------- ");
		Iterator<Entry<String, Integer>> iterator = exceptionsCounter.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> next = iterator.next();
			logger.info(" -- " + next.getKey() + "  ->  " + next.getValue() + " during " + getFormattedTime(startTime) + " to " + getFormattedTime(stopTime));
		}
		logger.info(" -------------------------------------------------------------------------- ");
	}

	private static String getFormattedTime(long timestamp) {
		return sdf.format(new Date(timestamp));
	}

	/**
	 * When this method is invoked, it means that all the exceptions will be handled, so
	 * another round is going to begin. Just clear the counter, and prepare the new lives.
	 *	
	 * XXX There is a risk here, if we extract the exceptions, but handler has some wrong
	 * behaves or errors. Then the exceptions just lost.
	 * <br/>
	 * @return
	 */
	public static Map<String, Throwable> getExceptions() {
		Map<String, Throwable> tmp = new HashMap<String, Throwable>();
		tmp.putAll(exceptions);
		exceptions.clear();
		return tmp;
	}
	
	/**
	 * XXX Maybe we don't need this.
	 * <br/>
	 * @return
	 */
	public static Map<String, Integer> getExceptionsCounter() {
		Map<String, Integer> tmp = new HashMap<String, Integer>();
		tmp.putAll(exceptionsCounter);
		exceptionsCounter.clear();
		return tmp;
	}
	
	/**
	 * If we want to print stack trace into logs, we call this method.
	 *
	 * <br/>
	 * @param e
	 * @return
	 */
	public synchronized static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter p = new PrintWriter(sw, true);
		e.printStackTrace(p);
		return sw.toString();
	}
	
}

package com.will.tooljars.mongo;

import java.util.HashMap;
import java.util.Map;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoDBSlowQueryInfo { 
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	private Mongo mg =null;
	private DB db;
	private CommandResult command;

    
	Map<String, Object> globalLock;    
	Map<String, Object> mem; 
	Map<String, Object> indexCounters; 
	Map<String, Object> connections; 
	Map<String, Object> opcounters;
	Map<String, Object> currentQueue;
	
	public MongoDBSlowQueryInfo(CommandResult command) {
		properties = command.toMap();
		
		globalLock = (Map<String, Object>) command.get("globalLock");    
		mem = (Map<String, Object>) command.get("mem"); 
		indexCounters = (Map<String, Object>) command.get("indexCounters"); 
		connections = (Map<String, Object>) command.get("connections"); 
		opcounters = (Map<String, Object>) command.get("opcounters");
		currentQueue = (Map<String, Object>) globalLock.get("currentQueue");
	}
	
	public Float getRatio() {
		java.lang.Float ratio = java.lang.Float.valueOf(String.valueOf((java.lang.Long) globalLock.get("lockTime"))) * 100 / java.lang.Float.valueOf(String.valueOf((java.lang.Long) globalLock.get("totalTime")));
		return ratio;
	}
	 
	public Integer getTotal() {
		java.lang.Integer total = (Integer) currentQueue.get("total");
		return total;
	}
	 
	public Integer getReaders() {
		java.lang.Integer readers = (Integer) currentQueue.get("readers");
		return readers;
	}
	 
	public Integer getWriters() {
		java.lang.Integer writers = (Integer) currentQueue.get("writers");
		return writers;
	}
	 
	public Integer getInsert() {
		java.lang.Integer insert = (Integer) opcounters.get("insert");
		return insert;
	}
	 
	public Integer getQuery() {
		java.lang.Integer query = (Integer) opcounters.get("query");
		return query;
	}
	 
	public Integer getUpdate() {
		java.lang.Integer update = (Integer) opcounters.get("update");
		return update;
	}
	 
	public Integer getDelete() {
		java.lang.Integer delete = (Integer) opcounters.get("delete");
		return delete;
	}
	 
	public Integer getGetmore() {
		java.lang.Integer getmore = (Integer) opcounters.get("getmore");
		return getmore;
	}
	 
	public Integer getVirtual() {
		java.lang.Integer virtual = (Integer) mem.get("virtual");
		return virtual;
	}
	 
	public Integer getResident() {
		java.lang.Integer resident = (Integer) mem.get("resident");
		return resident;
	}
	 
	public Float getHits() {
		java.lang.Float hit = java.lang.Float.valueOf(String.valueOf(indexCounters.get("hits"))) / java.lang.Float.valueOf(String.valueOf(indexCounters.get("accesses")));
		return hit;
	}
	 
	public Float getUse_frequency() {
		java.lang.Float usefrequency = java.lang.Float.valueOf(String.valueOf(indexCounters.get("hits"))) * 100 / java.lang.Float.valueOf(String.valueOf(indexCounters.get("accesses")));
		return usefrequency;
	}
	 
	public Integer getAvailable() {
		java.lang.Integer available = (Integer) connections.get("available");
		return available;
	}
	 
	public static void main(String[] args) {
		try {
		    Mongo mg = new Mongo();
	         
	        DB db = mg.getDB("local");
	        
	        CommandResult stats = db.getStats();
	         
	        CommandResult command = db.command("serverStatus");
	        System.out.println("command >>>>>" + command.toString());
	        Map<String, Object> cmd = (Map<String, Object>) command.toMap();
	        Map<String, Object> globalLock = (Map<String, Object>) command.get("globalLock");    
	        Map<String, Object> mem = (Map<String, Object>) command.get("mem"); 
	        Map<String, Object> indexCounters = (Map<String, Object>) command.get("indexCounters"); 
	        Map<String, Object> connections = (Map<String, Object>) command.get("connections"); 
	        Map<String, Object> opcounters = (Map<String, Object>) command.get("opcounters");
	        Map<String, Object> currentQueue = (Map<String, Object>) globalLock.get("currentQueue");
	        
	        MongoDBSlowQueryInfo info = new MongoDBSlowQueryInfo(command);
	        System.out.print(info.getUse_frequency());
	        
	        Map<String, Object> result = new HashMap<String, Object>();
	        result.put("globalLock_ratio", "ratio");
	        result.put("globalLock_currentQueue_total", "total");
	        result.put("globalLock_currentQueue_readers", "readers");
	        result.put("globalLock_currentQueue_writers", "writers");
	        result.put("opcounters_insert", "insert");
	        result.put("opcounters_query", "query");
	        result.put("opcounters_update", "update");
	        result.put("opcounters_delete", "delete");
	        result.put("opcounters_getmore", "getmore");
	        result.put("mem_virtual", "virtual");
	        result.put("mem_resident", "resident");
	        result.put("indexCounters_hit", "hit");
	        result.put("indexCounters_usefrequency", "usefrequency");
	        result.put("connections_available", "available");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}

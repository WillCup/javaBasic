package com.will.tooljars.mapDB;
import java.io.File;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;


public class StatMapDB {
	private static final String MAP_NAME = "STAT_MAP";
	private String filePath;
	DB db = null;
	Map<Long,Long> statMap = null;
	DBMod type = null;
	
	static enum DBMod
	{
		READ,
		WRITE
	}
	
	public StatMapDB(String filePath,DBMod type)
	{
		this.filePath = filePath;
		this.type = type;
		init();
	}
	
	private void init()
	{
		 File file = new File(filePath);
		 db = DBMaker
               .newFileDB(file)
               .transactionDisable()
               .asyncWriteFlushDelay(100)
               .make();
		 if(type.equals(DBMod.WRITE))
		 {
			 if(file.exists())
			 {
				 file.delete();
				 new File(filePath + ".p").delete();
			 }
			 statMap = db.createTreeMap(MAP_NAME).make();
		 }
		 else{
			 statMap = db.getTreeMap(MAP_NAME);
		 }
	}
	
	public Map<Long,Long> getStatMapDB()
	{
		return this.statMap;
	}
	
	public void close()
	{
		if(db!=null){
			db.close();
			db = null;
		}
	}
	
}

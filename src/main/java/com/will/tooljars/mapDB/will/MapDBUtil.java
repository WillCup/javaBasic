package com.will.tooljars.mapDB.will;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

/**
 * Maybe we could save all setting info into map db, cause they are all dependent plugin now..
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-12-23
 */
public class MapDBUtil {
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    private static DB db = DBMaker.newFileDB(new File("/root/workspace/will/apm-agent/cloudwise-hostagent-master/test/agentDB")).mmapFileEnable().make();
    
    private static DB tmpDB = DBMaker.newTempFileDB()._newFileDB(new File("agentTmpDB")).make();
    
    /**
     * All tmp db now.
     */
    private static Map<String, Map> mapSet = new ConcurrentHashMap<String, Map>();
    
    /**
     * Just used to save some temporary data.
     *
     * <br/>
     * @param mapName
     * @return
     */
    public static Map<String, String> getTmpHashMap(String mapName) {
        lock.readLock().lock();
        try {
            if (mapSet.containsKey(mapName)) {
                return mapSet.get(mapName);
            } else {
                lock.writeLock().lock();
                try {
                    HTreeMap<String, String> createHashMap = DBMaker.newTempHashMap();
                    mapSet.put(mapName, createHashMap);
                    return createHashMap;
                } finally {
                    lock.writeLock().unlock();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Just used to 
     *
     * <br/>
     * @param mapName
     * @return
     */
    public static Map<String, String> getHashMap(String mapName) {
        if (db.isClosed()) {
            db = DBMaker.newFileDB(new File("/root/workspace/will/apm-agent/cloudwise-hostagent-master/test/agentDB")).mmapFileEnable().make();
        }
        HTreeMap<String, String> createHashMap = db.createHashMap(mapName).makeOrGet();
        return createHashMap;
    }
    
    public static void getAll() {
        Map<String, Object> all = db.getAll();
        for (String key : all.keySet()) {
            System.out.println(key + " \t " + all.get(key));
        }
    }
    
    public static void commit() {
        db.commit();
    }
    
    public static void close() {
        db.close();
    }
}

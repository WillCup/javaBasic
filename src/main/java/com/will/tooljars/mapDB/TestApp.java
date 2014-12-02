package com.will.tooljars.mapDB;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class TestApp {

	private static final String PATH = "testMapDB";
	private static long TOTAL = 100000;
//	private static long TOTAL = 100000000;
	
	private static File file = new File("testMapFile");

	private static void write() {
		StatMapDB db = new StatMapDB(PATH, StatMapDB.DBMod.WRITE);
		Map<Long,Long> map = db.getStatMapDB();
		long sum = 0;
		long count = 0;
		long startTime = System.currentTimeMillis();
		for (long i = -1 * TOTAL / 2; i < TOTAL / 2; i++) {
			Long key = Math.abs(i);
			long oneStartTime = System.nanoTime();
			if (!map.containsKey(key)) {
				map.put(key, key);
			} else {
				map.put(key, map.get(key) + key);
			}
			if (i % 100000 == 0) {
				sum += (System.nanoTime() - oneStartTime);
				count++;
			}
		}
		System.out.println("avg:" + sum / count + " ns");
		System.out.println("write 10 million times:" + (System.currentTimeMillis() - startTime) + " ms");
		db.close();
	}
	
	private static void writeToFile() throws IOException {
	    if (!file.exists()) {
	        file.createNewFile();
	    }
	    String content = FileUtils.readFileToString(file);
	    StringBuilder builder = new StringBuilder();
        long sum = 0;
        long count = 0;
        long startTime = System.currentTimeMillis();
        for (long i = -1 * TOTAL / 2; i < TOTAL / 2; i++) {
            Long key = Math.abs(i);
            long oneStartTime = System.nanoTime();
            if (!content.contains(String.valueOf(key))) {
                builder.append(key);
            } else {
                builder.append(key);
            }
            if (i % 100000 == 0) {
                sum += (System.nanoTime() - oneStartTime);
                count++;
            }
        }
        FileUtils.writeStringToFile(file, builder.toString());
        System.out.println("avg:" + sum / count + " ns");
        System.out.println("write file >>>> 10 million times:" + (System.currentTimeMillis() - startTime) + " ms");
    }
	
	private static void readFromFile() throws IOException
    {
        long startTime = System.currentTimeMillis();
	    String content = FileUtils.readFileToString(file);
        System.out.println("read file >>>> 10 million times:" + (System.currentTimeMillis() - startTime) + " ms");
    }
	
	private static void read()
	{
		StatMapDB db = new StatMapDB(PATH, StatMapDB.DBMod.READ);
		Map<Long,Long> map = db.getStatMapDB();
		long startTime = System.currentTimeMillis();
		for (Entry<Long, Long> entry : map.entrySet()) {
			Long key = entry.getKey();
			Long value = entry.getValue();
		}
		System.out.println("read 10 million times:" + (System.currentTimeMillis() - startTime) + " ms");
		db.close();
	}

	public static void main(String[] args) {
		try {
            write();
            read();
            writeToFile();
            readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}

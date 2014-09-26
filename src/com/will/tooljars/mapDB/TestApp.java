package com.will.tooljars.mapDB;
import java.util.Map;
import java.util.Map.Entry;

public class TestApp {

	private static final String PATH = "test";
	//private static long TOTAL = 10000;
	private static long TOTAL = 100000000;

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
		write();
		read();
	}

}

package com.will.thread;

public interface TimeSlotTaskI extends Runnable{
	public static final int STATE_NEW=0;
	public static final int STATE_MOD=1;
	public static final int STATE_DEL=2;
	public static final int STATE_NOM=3;
	
	String getId();
	int getState();
	int getFreqInSeconds();
	int getProbability();
	long getLastExecTime();
	
}

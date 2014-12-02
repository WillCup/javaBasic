package com.cloudwise.hostagent.task.os.model;

import com.cloudwise.hostagent.utils.StringUtil;

public class ProcEleInfo {
    private String id;
	private String stateName;
    private String credUser;
    private String credGroup;
    
	private double cpuPercent;
    private long cpuTotal;
    private long memSize;
    private long memResident;
    private long diskIOBytesTotal;
    private double mPercent; 
    
    public double getMPercent() {
        return mPercent;
    }
    public void setMPercent(double mePercent) {
        this.mPercent = mePercent;
    }
    public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCredUser() {
		return credUser;
	}
	public void setCredUser(String credUser) {
		this.credUser = credUser;
	}
	public String getCredGroup() {
		return credGroup;
	}
	public void setCredGroup(String credGroup) {
		this.credGroup = credGroup;
	}
	public double getCpuPercent() {
		return cpuPercent;
	}
	public void setCpuPercent(double cpuPercent) {
		this.cpuPercent = cpuPercent;
	}
	public long getCpuTotal() {
		return cpuTotal;
	}
	public void setCpuTotal(long cpuTotal) {
		this.cpuTotal = cpuTotal;
	}
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	public long getDiskIOBytesTotal() {
		return diskIOBytesTotal;
	}
	public void setDiskIOBytesTotal(long diskIOBytesTotal) {
		this.diskIOBytesTotal = diskIOBytesTotal;
	}
    public long getMemResident() {
        return memResident;
    }
    public void setMemResident(long memResident) {
        this.memResident = memResident;
    }
    public String getId() {
        return StringUtil.MD5(stateName);
    }
    public void setId(String id) {
        this.id = id;
    }
}

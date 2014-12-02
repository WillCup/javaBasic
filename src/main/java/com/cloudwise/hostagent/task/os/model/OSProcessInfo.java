package com.cloudwise.hostagent.task.os.model;

public class OSProcessInfo {
    private double cpuPercent;
    private long cpuLastTime;
    private long cpuStartTime;
    private long cpuUser;
    private long cpuSys;
    private long cpuTotal;
    private long memSize;
    private long memResident;
    private long memShare;
    private long memMinorFaults;
    private long memMajorFaults;
    private long memPageFaults;
    private long memRss;
    private long memVsize;
    private long diskIOBytesRead;
    private long diskIOBytesWritten;
    private long diskIOBytesTotal;
    private String credUser;
    private String credGroup;
    private char stateState;
    private String stateName;
    private long statePpid;
    private int stateTty;
    private int stateNice;
    private int statePriority;
    private long stateThreads;
    private int stateProcessor;
    
    public double getCpuPercent() {
        return cpuPercent;
    }
    public void setCpuPercent(double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }
    public long getCpuLastTime() {
        return cpuLastTime;
    }
    public void setCpuLastTime(long cpuLastTime) {
        this.cpuLastTime = cpuLastTime;
    }
    public long getCpuStartTime() {
        return cpuStartTime;
    }
    public void setCpuStartTime(long cpuStartTime) {
        this.cpuStartTime = cpuStartTime;
    }
    public long getCpuUser() {
        return cpuUser;
    }
    public void setCpuUser(long cpuUser) {
        this.cpuUser = cpuUser;
    }
    public long getCpuSys() {
        return cpuSys;
    }
    public void setCpuSys(long cpuSys) {
        this.cpuSys = cpuSys;
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
    public long getMemResident() {
        return memResident;
    }
    public void setMemResident(long memResident) {
        this.memResident = memResident;
    }
    public long getMemShare() {
        return memShare;
    }
    public void setMemShare(long memShare) {
        this.memShare = memShare;
    }
    public long getMemMinorFaults() {
        return memMinorFaults;
    }
    public void setMemMinorFaults(long memMinorFaults) {
        this.memMinorFaults = memMinorFaults;
    }
    public long getMemMajorFaults() {
        return memMajorFaults;
    }
    public void setMemMajorFaults(long memMajorFaults) {
        this.memMajorFaults = memMajorFaults;
    }
    public long getMemPageFaults() {
        return memPageFaults;
    }
    public void setMemPageFaults(long memPageFaults) {
        this.memPageFaults = memPageFaults;
    }
    public long getDiskIOBytesRead() {
        return diskIOBytesRead;
    }
    public void setDiskIOBytesRead(long diskIOBytesRead) {
        this.diskIOBytesRead = diskIOBytesRead;
    }
    public long getDiskIOBytesWritten() {
        return diskIOBytesWritten;
    }
    public void setDiskIOBytesWritten(long diskIOBytesWritten) {
        this.diskIOBytesWritten = diskIOBytesWritten;
    }
    public long getDiskIOBytesTotal() {
        return diskIOBytesTotal;
    }
    public void setDiskIOBytesTotal(long diskIOBytesTotal) {
        this.diskIOBytesTotal = diskIOBytesTotal;
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
    public char getStateState() {
        return stateState;
    }
    public void setStateState(char stateState) {
        this.stateState = stateState;
    }
    public String getStateName() {
        return stateName;
    }
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public long getStatePpid() {
        return statePpid;
    }
    public void setStatePpid(long statePpid) {
        this.statePpid = statePpid;
    }
    public int getStateTty() {
        return stateTty;
    }
    public void setStateTty(int stateTty) {
        this.stateTty = stateTty;
    }
    public int getStateNice() {
        return stateNice;
    }
    public void setStateNice(int stateNice) {
        this.stateNice = stateNice;
    }
    public int getStatePriority() {
        return statePriority;
    }
    public void setStatePriority(int statePriority) {
        this.statePriority = statePriority;
    }
    public long getStateThreads() {
        return stateThreads;
    }
    public void setStateThreads(long stateThreads) {
        this.stateThreads = stateThreads;
    }
    public int getStateProcessor() {
        return stateProcessor;
    }
    public void setStateProcessor(int stateProcessor) {
        this.stateProcessor = stateProcessor;
    }
    public long getMemRss() {
        return memRss;
    }
    public void setMemRss(long memRss) {
        this.memRss = memRss;
    }
    public long getMemVsize() {
        return memVsize;
    }
    public void setMemVsize(long memVsize) {
        this.memVsize = memVsize;
    }
//    public long getTimeSys() {
//        return timeSys;
//    }
//    public void setTimeSys(long timeSys) {
//        this.timeSys = timeSys;
//    }
//    public long getTimeUser() {
//        return timeUser;
//    }
//    public void setTimeUser(long timeUser) {
//        this.timeUser = timeUser;
//    }
//    public long getTimeStartTime() {
//        return timeStartTime;
//    }
//    public void setTimeStartTime(long timeStartTime) {
//        this.timeStartTime = timeStartTime;
//    }
//    public long getTimeTotal() {
//        return timeTotal;
//    }
//    public void setTimeTotal(long timeTotal) {
//        this.timeTotal = timeTotal;
//    }
}

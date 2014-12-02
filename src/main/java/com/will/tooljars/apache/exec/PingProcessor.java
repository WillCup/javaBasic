package com.will.tooljars.apache.exec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * @Description:
 * @author James.zhang
 * @date 2014-5-21 下午6:00:27
 * 
 */
public class PingProcessor {

    private StringBuffer taskIdBuf;

    private int status;

    private String logMsg(String msg) {
        return "[taskId-" + taskIdBuf.toString() + "] " + msg;
    }

    private PingRespInfo process(String host) throws IOException {
        PingRespInfo pingRespInfo = new PingRespInfo();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        double lostRate = 0;
        long averageTime = 0;
        int transmitted = 10;
        int received = 0;
        String respIp = null;
        String timeStr = null;
        double minTime = 0;
        double avgTime = 0;
        double maxTime = 0;
        String snapshot = "";
        int ttl = 0;
        int size = 0;
        try {
            String line = "ping " + host + " -c " + transmitted + " -A";

            pingRespInfo.setCmd(line);
            pingRespInfo.setCurrentTime(System.currentTimeMillis());
            
            PumpStreamHandler streamHandler = new PumpStreamHandler(
                    outputStream);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            CommandLine cmdLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(streamHandler);
            ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
            executor.setWatchdog(watchdog);
            executor.setExitValue(1);
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            String resultStr = outputStream.toString("UTF-8");
            if (resultStr.contains("unknown")) {
                pingRespInfo.setResp_status(0);
                pingRespInfo.setResp_err(resultStr);
//                return jsonObjectMapper.writeValueAsString(pingRespInfo);
                return pingRespInfo;
            }
            int beg = resultStr.indexOf("received,");
            int end = resultStr.indexOf("packet", beg + 9);
            snapshot = resultStr.replace("\n", "|");
            received = Integer.valueOf(resultStr.substring(
                    resultStr.indexOf("transmitted") + 12,
                    resultStr.lastIndexOf("received")).trim());
            if (received == 0) {
                pingRespInfo.setResp_status(0);
                pingRespInfo.setResp_err("0 packets received!");
                pingRespInfo.setSnapshot(snapshot);
//                return jsonObjectMapper.writeValueAsString(pingRespInfo);
                return pingRespInfo;
            }
            lostRate = Double.valueOf(resultStr.substring(beg + 10, end)
                    .replace("%", ""));
            averageTime = Long.valueOf(resultStr
                    .substring(resultStr.lastIndexOf("time") + 4,
                            resultStr.lastIndexOf("rtt")).trim()
                    .replace("ms", ""));
            respIp = resultStr.substring(resultStr.indexOf("(") + 1,
                    resultStr.indexOf(")"));
            timeStr = resultStr.substring(resultStr.lastIndexOf("=") + 1)
                    .trim();
            ttl = Integer.valueOf(resultStr.substring(
                    resultStr.indexOf("ttl") + 4, resultStr.indexOf("time"))
                    .trim());
            size = Integer.valueOf(resultStr.substring(
                    resultStr.indexOf("data.") + 5,
                    resultStr.indexOf("bytes", resultStr.indexOf("data.") + 5))
                    .trim());
            String[] times = timeStr.split("/");
            minTime = Double.valueOf(times[0]);
            avgTime = Double.valueOf(times[1]);
            maxTime = Double.valueOf(times[2]);
        } catch (Exception ex) {
            status = 0;
            pingRespInfo.setResp_err(ex.getMessage());
//                return jsonObjectMapper.writeValueAsString(pingRespInfo);
            return pingRespInfo;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        pingRespInfo.setResp_status(status);
        pingRespInfo.setPacketLoss(lostRate);
        pingRespInfo.setResp_time(averageTime);
        pingRespInfo.setTransmitted(transmitted);
        pingRespInfo.setReceived(received);
        pingRespInfo.setRespIp(respIp);
        pingRespInfo.setMinTime(minTime);
        pingRespInfo.setAvgTime(avgTime);
        pingRespInfo.setMaxTime(maxTime);
        pingRespInfo.setSnapshot(snapshot);
        pingRespInfo.setTimeToLive(ttl);
        pingRespInfo.setPackageSize(size);
//        return jsonObjectMapper.writeValueAsString(pingRespInfo);
        return pingRespInfo;
    }

    public static class PingTaskInfo {
        private String site_domain;

        private int app_id;
        private int service_type;
        private int target_type;
        private int target_id;
        private int account_id;
        private int domain_id;
        private String monitor_list;

        public String getMonitor_list() {
            return monitor_list;
        }

        public void setMonitor_list(String monitor_list) {
            this.monitor_list = monitor_list;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public int getDomain_id() {
            return domain_id;
        }

        public void setDomain_id(int domain_id) {
            this.domain_id = domain_id;
        }

        public int getApp_id() {
            return app_id;
        }

        public void setApp_id(int app_id) {
            this.app_id = app_id;
        }

        public int getService_type() {
            return service_type;
        }

        public void setService_type(int service_type) {
            this.service_type = service_type;
        }

        public int getTarget_type() {
            return target_type;
        }

        public void setTarget_type(int target_type) {
            this.target_type = target_type;
        }

        public int getTarget_id() {
            return target_id;
        }

        public void setTarget_id(int target_id) {
            this.target_id = target_id;
        }

        public String getSite_domain() {
            return site_domain;
        }

        public void setSite_domain(String site_domain) {
            this.site_domain = site_domain;
        }
    }

    public static class PingRespInfo {
        private int resp_status = 1;
        private String resp_err = "";
        private long resp_time;
        private double packetLoss;
        private int transmitted;
        private int received;
        private String respIp;
        private double minTime;
        private double avgTime;
        private double maxTime;
        private String snapshot;
        private int timeToLive;
        private int packageSize;

        private int monitorId;
        private int app_id;
        private int service_type;
        private int target_type;
        private int target_id;
        private int account_id;
        private int domain_id;
        private String monitor_list;

        private String cmd;
        private long currentTime;
        
        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        public String getCmd() {
            return cmd;
        }

        public void setCmd(String cmd) {
            this.cmd = cmd;
        }

        public String getMonitor_list() {
            return monitor_list;
        }

        public void setMonitor_list(String monitor_list) {
            this.monitor_list = monitor_list;
        }

        public int getAccount_id() {
            return account_id;
        }

        public void setAccount_id(int account_id) {
            this.account_id = account_id;
        }

        public int getDomain_id() {
            return domain_id;
        }

        public void setDomain_id(int domain_id) {
            this.domain_id = domain_id;
        }

        public int getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(int packageSize) {
            this.packageSize = packageSize;
        }

        public int getTimeToLive() {
            return timeToLive;
        }

        public void setTimeToLive(int timeToLive) {
            this.timeToLive = timeToLive;
        }

        public String getSnapshot() {
            return snapshot;
        }

        public void setSnapshot(String snapshot) {
            this.snapshot = snapshot;
        }

        public double getMinTime() {
            return minTime;
        }

        public void setMinTime(double minTime) {
            this.minTime = minTime;
        }

        public double getAvgTime() {
            return avgTime;
        }

        public void setAvgTime(double avgTime) {
            this.avgTime = avgTime;
        }

        public double getMaxTime() {
            return maxTime;
        }

        public void setMaxTime(double maxTime) {
            this.maxTime = maxTime;
        }

        public String getRespIp() {
            return respIp;
        }

        public void setRespIp(String respIp) {
            this.respIp = respIp;
        }

        public int getReceived() {
            return received;
        }

        public void setReceived(int received) {
            this.received = received;
        }

        public int getTransmitted() {
            return transmitted;
        }

        public void setTransmitted(int transmitted) {
            this.transmitted = transmitted;
        }

        public int getMonitorId() {
            return monitorId;
        }

        public void setMonitorId(int monitorId) {
            this.monitorId = monitorId;
        }

        public int getApp_id() {
            return app_id;
        }

        public void setApp_id(int app_id) {
            this.app_id = app_id;
        }

        public int getService_type() {
            return service_type;
        }

        public void setService_type(int service_type) {
            this.service_type = service_type;
        }

        public int getTarget_type() {
            return target_type;
        }

        public void setTarget_type(int target_type) {
            this.target_type = target_type;
        }

        public int getTarget_id() {
            return target_id;
        }

        public void setTarget_id(int target_id) {
            this.target_id = target_id;
        }

        public int getResp_status() {
            return resp_status;
        }

        public void setResp_status(int resp_status) {
            this.resp_status = resp_status;
        }

        public long getResp_time() {
            return resp_time;
        }

        public void setResp_time(long resp_time) {
            this.resp_time = resp_time;
        }

        public double getPacketLoss() {
            return packetLoss;
        }

        public void setPacketLoss(double packetLoss) {
            this.packetLoss = packetLoss;
        }

        public String getResp_err() {
            return resp_err;
        }

        public void setResp_err(String resp_err) {
            this.resp_err = resp_err;
        }

        public String toString() {
            return "PingRespInfo [resp_status=" + resp_status + ", resp_err="
                    + resp_err + ", resp_time=" + resp_time + ", packetLoss="
                    + packetLoss + ", transmitted=" + transmitted
                    + ", received=" + received + ", respIp=" + respIp
                    + ", minTime=" + minTime + ", avgTime=" + avgTime
                    + ", maxTime=" + maxTime + ", snapshot=" + snapshot
                    + ", timeToLive=" + timeToLive + ", packageSize="
                    + packageSize + ", monitorId=" + monitorId + ", app_id="
                    + app_id + ", service_type=" + service_type
                    + ", target_type=" + target_type + ", target_id="
                    + target_id + ", account_id=" + account_id + ", domain_id="
                    + domain_id + ", monitor_list=" + monitor_list + ", cmd="
                    + cmd + ", currentTime=" + currentTime + "]";
        }
        
    }

    public static void main(String[] args) {
        try {
            PingProcessor processor = new PingProcessor();
            List<String> hosts = new ArrayList<String>();
            String string = "10.0.3.";
            for (int i= 100; i < 110; i++) {
                hosts.add(string+i);
            }
            for (int i= 120; i < 126; i++) {
                hosts.add(string+i);
            }
            while (true) {
                for (String host : hosts) {
                    PingRespInfo process = processor.process(host);
                    if (process.getResp_status() != 0) {
                        System.out.println(process);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

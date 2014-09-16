package com.will.tooljars.jackson;

import java.io.File;
import java.util.Comparator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReadJsonTree {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PHPCode code = new PHPCode();
            ObjectNode root = (ObjectNode) mapper.readTree(new File("E:/files/work/collegues/Neeke/1_2_3_4_5___1409366292935.cw"));
            JsonNode jsonNode = root.get("{start}").get("counts");
            code.setWorkTime(jsonNode.get("wt").asInt());
            code.setCpu(jsonNode.get("cpu").asLong());
            code.setMemUsed(jsonNode.get("mu").asLong());
            code.setPeakMemUsed(jsonNode.get("pmu").asLong());
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static class PHPCode {
        private int workTime;
        private long cpu;
        private long memUsed;
        private long peakMemUsed;
        private String url;
        public int getWorkTime() {
            return workTime;
        }
        public void setWorkTime(int workTime) {
            this.workTime = workTime;
        }
        public long getCpu() {
            return cpu;
        }
        public void setCpu(long cpu) {
            this.cpu = cpu;
        }
        public long getMemUsed() {
            return memUsed;
        }
        public void setMemUsed(long memUsed) {
            this.memUsed = memUsed;
        }
        public long getPeakMemUsed() {
            return peakMemUsed;
        }
        public void setPeakMemUsed(long peakMemUsed) {
            this.peakMemUsed = peakMemUsed;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String toString() {
            return "PHPCode [workTime=" + workTime + ", cpu=" + cpu
                    + ", memUsed=" + memUsed + ", peakMemUsed=" + peakMemUsed
                    + ", url=" + url + "]";
        }
    }
    
    static class WTComparator implements Comparator<PHPCode> {
        public int compare(PHPCode o1, PHPCode o2) {
            if (o1.getWorkTime() > o2.getWorkTime()) {
                return 1;
            } else if (o1.getWorkTime() == o2.getWorkTime()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
    static class CpuComparator implements Comparator<PHPCode> {
        public int compare(PHPCode o1, PHPCode o2) {
            if (o1.getCpu() > o2.getCpu()) {
                return 1;
            } else if (o1.getCpu() == o2.getCpu()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
    static class MemComparator implements Comparator<PHPCode> {
        public int compare(PHPCode o1, PHPCode o2) {
            if (o1.getMemUsed() > o2.getMemUsed()) {
                return 1;
            } else if (o1.getMemUsed() == o2.getMemUsed()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
    static class MemPeakComparator implements Comparator<PHPCode> {
        public int compare(PHPCode o1, PHPCode o2) {
            if (o1.getPeakMemUsed() > o2.getPeakMemUsed()) {
                return 1;
            } else if (o1.getPeakMemUsed() == o2.getPeakMemUsed()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}

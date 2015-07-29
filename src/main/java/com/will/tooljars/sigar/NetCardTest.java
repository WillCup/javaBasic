//package com.will.tooljars.sigar;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.commons.beanutils.MethodUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.hyperic.sigar.NetInterfaceStat;
//import org.hyperic.sigar.NetStat;
//import org.hyperic.sigar.OperatingSystem;
//import org.hyperic.sigar.ProcCpu;
//import org.hyperic.sigar.ProcCredName;
//import org.hyperic.sigar.ProcDiskIO;
//import org.hyperic.sigar.ProcMem;
//import org.hyperic.sigar.ProcState;
//import org.hyperic.sigar.SigarException;
//import org.hyperic.sigar.SigarProxy;
//import org.hyperic.sigar.SigarProxyCache;
//
//import com.cloudwise.hostagent.plugin.system.ProcessData;
//import com.cloudwise.hostagent.task.os.model.OSProcessInfo;
//import com.cloudwise.hostagent.task.os.model.ProcEleInfo;
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class NetCardTest extends Thread{
//    private final static Log logger = LogFactory.getLog(NetCardTest.class);
//
//    private static final String REGEX = "\\/";
//
//    private static final Object ATTRIBUTE_SUB_METRIC = "nest_sub";
//    
//    Map cacheOne = null;
//
//    protected Map<String, Map> lastInfoCache = new HashMap<String, Map>();
//    
//    private final String get = "get";
//    long[] procList;
//    Map<String, ProcEleInfo> groupMap;
//    ProcessData processData = new ProcessData();
//    ProcCpu procCpu;
//    ProcMem procMem;
//    ProcCredName procCred;
//    ProcState procState;
//    ProcDiskIO procDiskIO;
//    Field[] fields = OSProcessInfo.class.getDeclaredFields();
//    Map report = new HashMap();
//    String name;
//    Iterator<Entry<String, ProcEleInfo>> iterator;
//    List<ProcEleInfo> procDataList = new ArrayList<ProcEleInfo>();
//    Map finalResult = new HashMap();
//    
//    @Override
//    public void run() {
//        try {
//            testNetCard();
////            testProcess();
//            
//            logger.error("finalReport is : " + getMapper().writeValueAsString(finalResult));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void testProcess() throws SigarException, NoSuchMethodException,
//            IllegalAccessException, InvocationTargetException {
//        procList = getSigar().getProcList();
//        groupMap = new HashMap<String, ProcEleInfo>();
//        int totalNum = 0;
//        
//        long totalCpu = getSigar().getCpu().getTotal();
//        long totalMem = getSigar().getMem().getTotal();
//        
//        for (long pid : procList) {
//            totalNum++;
////                processData = new ProcessData();
//            try {
//                processData.populate(getSigar(), pid);
//            } catch (Exception e) {
//                logger.warn("No process " + pid + " now");
//                continue;
//            }
//            procCpu = processData.getProcCpu();
//            procMem = processData.getProcMem();
//            procCred = processData.getProcCredName();
//            procState = processData.getProcState();
//            procDiskIO = null;
//            logger.info("procCpu is " + procCpu);
//            //          procDiskIO = getSigar().getProcDiskIO(pid);
////                report = new HashMap();
//            for (Field f : fields) {
//                name = f.getName();
//                if (name.startsWith("mem")) {
//                    if (procMem != null) {
//                        report.put(
//                                name,
//                                MethodUtils.invokeMethod(
//                                        procMem,
//                                        get +  name.substring("mem".length()), null));
//                    }
//                } else if (name.startsWith("cred")){
//                    if (procCred != null) {
//                        report.put(
//                                name,
//                                MethodUtils.invokeMethod(
//                                        procCred,
//                                        get + name.substring("cred".length()), null));
//                    }
//                } else if (name.startsWith("diskIO")){
//                    if (procDiskIO != null) {
//                        report.put(
//                                name,
//                                MethodUtils.invokeMethod(
//                                        procDiskIO,
//                                        get + name.substring("diskIO".length()), null));
//                    }
//                } else if (name.startsWith("cpu")){
//                    if (procCpu != null) {
//                        report.put(
//                                name,
//                                MethodUtils.invokeMethod(
//                                        procCpu,
//                                        get + name.substring("cpu".length()), null));
//                    }
//                } else if (name.startsWith("state")){
//                    if (procState != null) {
//                        report.put(
//                                name,
//                                MethodUtils.invokeMethod(
//                                        procState,
//                                        get + name.substring("state".length()), null));
//                    }
//                }
//            }
//            try {
//                logger.info(getMapper().writeValueAsString(report));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            if (!OperatingSystem.IS_WIN32) {
//                report.put("cpuPercent", Double.valueOf(String.valueOf(report.get("cpuTotal"))) / totalCpu);
//            }
//            report.put("mPercent", Double.valueOf(String.valueOf(report.get("memResident"))) / totalMem );
//            
//            addElementToGroup(groupMap, report);
//        }
//        
//        iterator = groupMap.entrySet().iterator();
////          procDataList = new ArrayList<ProcEleInfo>();
//        procDataList.clear();
//        while (iterator.hasNext()) {
//            Entry<String, ProcEleInfo> next = iterator.next();
//            procDataList.add(next.getValue());
//        }
//        
////            finalResult = new HashMap();
//        finalResult.put(ATTRIBUTE_SUB_METRIC, procDataList);
//        finalResult.put("totalCount", totalNum);
//    }
//
//    private void testNetCard() throws SigarException, IOException,
//            JsonParseException, JsonMappingException, JsonProcessingException,
//            SocketException, NoSuchMethodException, IllegalAccessException,
//            InvocationTargetException {
//        String[] netInterfaceList = getSigar().getNetInterfaceList();;
//        NetInterfaceStat netInterfaceStat;
//        Map newOne;
//        Enumeration<NetworkInterface> en;
//        NetStat netStat;
//        Map netStats = null;
//        List<Map> netInterfaceStats = new ArrayList<Map>();
//        for (String ni : netInterfaceList) {
//            netInterfaceStat = getSigar().getNetInterfaceStat(ni);
//            newOne = getMapper().readValue(
//                    getMapper().writeValueAsBytes(netInterfaceStat), Map.class);
//            en = NetworkInterface.getNetworkInterfaces();
//            newOne.put("name", ni);
//            newOne.put("id", ni);
//            if ((cacheOne = lastInfoCache.get(ni)) != null) {
//                newOne.put("inMbps",
//                        formatToMbps(getPerSec(cacheOne, newOne, "rxBytes")));
//                newOne.put("outMbps",
//                        formatToMbps(getPerSec(cacheOne, newOne, "txBytes")));
//            }
//            netInterfaceStats.add(newOne);
//            lastInfoCache.put(ni, newOne);
//        }
//        netStat = getSigar().getNetStat();
//        netStats = getMapper().readValue(
//                getMapper().writeValueAsBytes(netStat), Map.class);
//        netStats.put("nest_sub", netInterfaceStats);
//        System.out.println(getMapper().writeValueAsString(netStats));
//    }
//
//    public static void main(String[] args) {
//       new NetCardTest().start();
//    }
//
//    private ObjectMapper getMapper() {
//        return new ObjectMapper();
//    }
//
//    private SigarProxy getSigar() {
//        return SigarProxyCache.newInstance();
//    }
//    
//    /**
//     * This method is to handle some immaturity data, like req/s, bytes/s.
//     * 
//     * <br/>
//     * 
//     * @param cacheOne
//     * @param newOne
//     * @param totalField
//     * @param avgField
//     * @return
//     * @throws NoSuchMethodException
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public Double getPerSec(Object cacheOne, Object newOne, String totalField)
//            throws NoSuchMethodException, IllegalAccessException,
//            InvocationTargetException {
//        double newTotal = 0;
//        double cacheTotal = 0;
//        if (cacheOne instanceof Map) {
//            Map newMap = (Map) newOne;
//            Map cacheMap = (Map) cacheOne;
//            newTotal = Double.valueOf(String.valueOf(newMap.get(totalField)));
//            cacheTotal = Double.valueOf(String.valueOf(cacheMap.get(totalField)));
//        } else {
//            newTotal = Double.valueOf(String.valueOf(MethodUtils.invokeMethod(
//                    newOne, getGetMethod(totalField), null)));
//            cacheTotal = Double.valueOf(String.valueOf(MethodUtils
//                    .invokeMethod(cacheOne, getGetMethod(totalField), null)));
//        }
//        double value = (newTotal - cacheTotal) / getFreqInSeconds();
//        return value;
//    }
//    
//    public String getGetMethod(String name) {
//        if (name != null) {
//            return "get" + name.substring(0, 1).toUpperCase()
//                    + name.substring(1);
//        }
//        return null;
//    }
//    
//    public int getFreqInSeconds() {
//        return 60;
//    }
//    
//    /**
//     * Format bytes and frequency to Mbps. <br/>
//     * 
//     * @param bytes
//     * @return
//     */
//    public double formatToMbps(double bytes) {
//        return bytes / 1024d / 1024d * 8d;
//    }
//
//    public double formatToMB(double bytes) {
//        return bytes / 1024d / 1024d;
//    }
//    
//    private void addElementToGroup(Map<String, ProcEleInfo> target, Map ele) {
//        String key = String.valueOf(ele.get("credGroup")).trim() + "|" + String.valueOf(ele.get("credUser")).trim() + "|" + String.valueOf(ele.get("stateName")).trim();
//        if (target.get(key) != null) {
//            ProcEleInfo info = target.get(key);
//            if (!OperatingSystem.IS_WIN32) {
//                info.setCpuTotal(info.getCpuTotal() + Long.valueOf(String.valueOf(ele.get("cpuTotal"))));
//                info.setCpuPercent(info.getCpuPercent() + Double.valueOf((String.valueOf(ele.get("cpuPercent")))));
//            }
//            info.setMemResident(info.getCpuTotal() + Long.valueOf(String.valueOf(ele.get("memResident"))));
//            info.setMemSize(info.getCpuTotal() + Long.valueOf(String.valueOf(ele.get("memSize"))));
////          info.setDiskIOBytesTotal(info.getDiskIOBytesTotal() + Long.valueOf((String)ele.get("diskIOBytesTotal")));
//            info.setMPercent(info.getCpuPercent() + Double.valueOf(String.valueOf(ele.get("mPercent"))));
//            target.put(key, info);
//        } else {
//            ProcEleInfo info = new ProcEleInfo();
//            info.setId(key);
//            if (!OperatingSystem.IS_WIN32) {
//                info.setCpuPercent(Double.valueOf(String.valueOf(ele.get("cpuPercent"))));
//                info.setCpuTotal(Long.valueOf(String.valueOf(ele.get("cpuTotal"))));
//            }
//            info.setCredUser(String.valueOf(ele.get("credUser")));
//            info.setCpuTotal(Long.valueOf(String.valueOf(ele.get("memResident"))));
//            info.setMemSize(Long.valueOf(String.valueOf(ele.get("memSize"))));
//            info.setStateName(getformattedName(String.valueOf(ele.get("stateName")), true));
////          info.setDiskIOBytesTotal(Long.valueOf((String)ele.get("diskIOBytesTotal")));
//            info.setCredGroup(String.valueOf(ele.get("credGroup")));
//            info.setMPercent(Double.valueOf(String.valueOf(ele.get("mPercent"))));
//            target.put(key, info);
//        }
//    }
//    
//    /**
//     * For split words in elasticSearch, we only get the last string, and the
//     * string can only contain letters and numbers.
//     * 
//     * <br/>
//     * 
//     * @param name
//     * @return
//     */
//    public String getformattedName(String name, boolean last) {
////      AgentUtils.debugMsg("name is : " + name + " .... last is : " + last);
//        int index;
//        if (last) {
//            if ((index = name.lastIndexOf("/")) > 0) {
//                name = name.substring(index + 1);
//            }
//        }
//        String[] split = name.split(REGEX);
//        boolean sa = false;
//        for (String str : split) {
//            if (sa) {
//                if (str.length() > 1) {
//                    name += str.substring(0, 1).toUpperCase() + str.substring(1);
//                } else {
//                    name += str.toUpperCase();
//                }
//            } else {
//                name = str;
//                sa = true;
//            }
//        }
//        return name;
//    }
//
//
//}

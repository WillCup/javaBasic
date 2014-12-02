package com.will.snmp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * 演示：异步GET OID值
 *
 * blog http://www.micmiu.com
 *
 * @author Michael
 *
 */
public class SnmpGetAsyn {

    public static final int DEFAULT_VERSION = SnmpConstants.version2c;
    public static final String DEFAULT_PROTOCOL = "udp";
    public static final int DEFAULT_PORT = 161;
    public static final long DEFAULT_TIMEOUT = 3 * 1000L;
    public static final int DEFAULT_RETRY = 3;

    /**
     * 创建对象communityTarget
     *
     * @param targetAddress
     * @param community
     * @param version
     * @param timeOut
     * @param retry
     * @return CommunityTarget
     */
    public static CommunityTarget createDefault(String ip, String community) {
        Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
                + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(address);
        target.setVersion(DEFAULT_VERSION);
        target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
        target.setRetries(DEFAULT_RETRY);
        return target;
    }

    /**
     * 异步采集信息
     *
     * @param ip
     * @param community
     * @param oid
     */
    public static List snmpAsynGetList(String ip, String community,
            List<String> oidList) {
        final List cpuData = new ArrayList();
        CommunityTarget target = createDefault(ip, community);
        Snmp snmp = null;
        try {
            DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            snmp.listen();

            PDU pdu = new PDU();
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            }

            final CountDownLatch latch = new CountDownLatch(1);
            ResponseListener listener = new ResponseListener() {
                public void onResponse(ResponseEvent event) {
                    ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                    PDU response = event.getResponse();
                    PDU request = event.getRequest();
                    System.out.println("[request]:" + request);
                    if (response == null) {
                        System.out.println("[ERROR]: response is null");
                    } else if (response.getErrorStatus() != 0) {
                        System.out.println("[ERROR]: response status"
                                + response.getErrorStatus() + " Text:"
                                + response.getErrorStatusText());
                    } else {
                        System.out.println("Received response Success!");
                        for (int i = 0; i < response.size(); i++) {
                            VariableBinding vb = response.get(i);
/*                            System.out.println(vb.getOid() + " = "
                                    + vb.getVariable());*/
                            cpuData.add(vb.getVariable());
                        }
                        System.out.println("SNMP Asyn GetList OID finished. ");
                        latch.countDown();
                    }
                }
            };

            pdu.setType(PDU.GET);
            snmp.send(pdu, target, null, listener);
            System.out.println("asyn send pdu wait for response...");

            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);

            snmp.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SNMP Asyn GetList Exception:" + e);
        }
        return cpuData;

    }
    public static void main(String[] args) {
        String ip = "10.0.1.44";
        String community = "public";

        List<String> oidList = new ArrayList<String>();
        //ssCpuRawUser
        oidList.add(".1.3.6.1.4.1.2021.11.50.0");
        //ssCpuRawNice
        oidList.add(".1.3.6.1.4.1.2021.11.51.0");
        //ssCpuRawSystem
        oidList.add(".1.3.6.1.4.1.2021.11.52.0");
        //ssCpuRawIdle
        oidList.add(".1.3.6.1.4.1.2021.11.53.0");
        //ssCpuRawWait
        oidList.add(".1.3.6.1.4.1.2021.11.54.0");
        //ssCpuRawInterrupt
        oidList.add(".1.3.6.1.4.1.2021.11.56.0");
        //ssCpuRawSoftIRQ
        oidList.add("1.3.6.1.4.1.2021.11.61.0");
        // 异步采集数据
        List cpuData = SnmpGetAsyn.snmpAsynGetList(ip, community, oidList);
        
        double ssCpuRawUser =  Double.parseDouble(cpuData.get(0).toString());
        double ssCpuRawNice =  Double.parseDouble(cpuData.get(1).toString());
        double ssCpuRawSystem = Double.parseDouble(cpuData.get(2).toString());
        double ssCpuRawIdle =  Double.parseDouble(cpuData.get(3).toString());
        double ssCpuRawWait =  Double.parseDouble(cpuData.get(4).toString());
        double ssCpuRawInterrupt =  Double.parseDouble(cpuData.get(5).toString());
        double ssCpuRawSoftIRQ = Double.parseDouble(cpuData.get(6).toString());
        
        double cpuRatio = 100*(ssCpuRawUser+ssCpuRawNice+ssCpuRawSystem+ssCpuRawWait+ssCpuRawInterrupt+ssCpuRawSoftIRQ)/(ssCpuRawUser+ssCpuRawNice
                +ssCpuRawSystem+ssCpuRawIdle+ssCpuRawWait+ssCpuRawInterrupt+ssCpuRawSoftIRQ);
        
        System.out.println("CPU利用率："+cpuRatio);
        
    }
}

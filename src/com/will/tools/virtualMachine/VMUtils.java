package com.will.tools.virtualMachine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class VMUtils {
    
    public static ThreadLocal<VirtualMachine> vms = new ThreadLocal<VirtualMachine>();
    
//    public static Map<String, ThreadLocal<VirtualMachine>> map = new ConcurrentHashMap<String, ThreadLocal<VirtualMachine>>();
    
    /**
     * Return corresponding VM for the pid.
     * 
     * @param pid
     * @return
     * @throws AttachNotSupportedException
     * @throws IOException
     */
    public static VirtualMachine getVMByPid(String pid) throws AttachNotSupportedException, IOException {
        if (vms.get() == null) {
            vms.set(VirtualMachine.attach(pid));
        }
        return vms.get();
    }
    
    public static void detachVM() throws IOException {
        vms.get().detach();
        vms.set(null);
    }
    
}

package com.will.tooljars.apache.commons.chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;

/**
 * 将这个流程定义成一个序列（或者说“命令链”）
 * 
 * 使用Commons Chain实现这个商业流程，必须将流程中的每一步写成一个类，这个类需要有一个public的方法execute()。这和传统的命令模式（Command pattern）实现相同。
 * 
 * TestDriveVehicle，NegotiateSale和ArrangeFinancing命令的实现只是简单的打印了将执行什么操作
 * 
 * @author Will
 * @created at 2014-8-29 下午1:01:48
 */
public class SellVehicleChain extends ChainBase {
    public SellVehicleChain() {
        super();
        addCommand(new GetCustomerInfo());
        addCommand(new TestDriveVehicle());
        addCommand(new NegotiateSale());
        addCommand(new ArrangeFinancing());
        addCommand(new CloseSale());
    }
    public static void main(String[] args) throws Exception {
        Command process = new SellVehicleChain();
        Context ctx = new ContextBase();
        process.execute(ctx);
    }
    
    
    static class TestDriveVehicle implements Command {
        public boolean execute(Context ctx) throws Exception {
            System.out.println("Test drive the vehicle");
            return false;
        }
    }

    static class NegotiateSale implements Command {
        public boolean execute(Context ctx) throws Exception {
            System.out.println("Negotiate sale");
            return false;
        }
    }

    static class ArrangeFinancing implements Command {
        public boolean execute(Context ctx) throws Exception {
            System.out.println("Arrange financing");
            return false;
        }
    }
    
    /**
     * CloseSale从Context对象中取出GetCustomerInfo放入的用户名，并将其打印
     * 
     * @author Will
     * @created at 2014-8-29 下午1:00:47
     */
    static class CloseSale implements Command {
        public boolean execute(Context ctx) throws Exception {
            System.out.println("Congratulations "
            +ctx.get("customerName")
               +", you bought a new car!");
            return false;
        }
    }
    
    /**
     * 
     * @author Will
     * @created at 2014-8-29 上午11:49:28
     */
    static class GetCustomerInfo implements Command {
        public boolean execute(Context ctx) throws Exception {
            System.out.println("Get customer info");
            // 这里将用户名放入了Context对象ctx中。这个Context对象连接了各个命令。
            // 暂时先将这个对象想象成根据关键字存取值的哈希表。所有后来的命令可以通过它访问刚才放入的用户名。
            ctx.put("customerName","George Burdell");
            return false;
        }
        
        
    }
}
package com.will.tooljars.apache.commons.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;

/**
 * 参数context仅仅是一个存放了名称-值对的集合。接口Context在这里作为一个标记接口：它扩展了java.util.Map但是没有添加任何特殊的行为。
 * 于此相反，类ContextBase不仅提供了对Map的实现而且增加了一个特性：属性-域透明。这个特性可以通过使用Map的put和get方法操作JavaBean的域，
 * 当然这些域必须使用标准的getFoo和setFoo方法定义。
 * 那些通过JavaBean的"setter"方法设置的值，可以通过对应的域名称，用Map的get方法得到。同样，那些用Map的put方法设置的值可以通过JavaBean的"getter"方法得到。
 * 
 * 我们这里创建一个专门的context提供显式的customerName属性支持.
 * 
 * 当一些新的命令（Command）被添加时，它们可以不用考虑context的具体实现，直接通过Map的get和put操作属性。
 * 不论采用何种机制，ContextBase类都可以保证命令（Command）间可以通过context互操作。
 * 
 * @author Will
 * @created at 2014-8-29 下午1:33:30
 */
public class SellVehicleContext extends ContextBase {

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String name) {
        this.customerName = name;
    }

    static class NewCommand implements Command {
        @Override
        public boolean execute(Context ctx) throws Exception {
            SellVehicleContext myCtx = (SellVehicleContext) ctx;
            System.out.println("Congratulations " + myCtx.getCustomerName()
                    + ", you bought a new car!");
            return false;
        }
    }
    
    public static void main(String[] args) throws Exception {
        Command process = new NewChain();
        SellVehicleContext ctx = new SellVehicleContext();
        ctx.setCustomerName("Will");
        process.execute(ctx);
    }
    
    static class NewChain extends ChainBase {

        public NewChain() {
            super();
            NewCommand command = new NewCommand();
            addCommand(command);
        }
    }
}
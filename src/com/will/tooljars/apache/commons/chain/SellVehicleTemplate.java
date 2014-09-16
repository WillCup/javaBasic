package com.will.tooljars.apache.commons.chain;

/**
 * 看一下Commons Chain是怎样工作的，我们从一个人造的例子开始：二手车销售员的商业流程。下面是销售流程的步骤：
 * 
 * 1. 得到用户信息
 * 
 * 2. 试车
 * 
 * 3. 谈判销售
 * 
 * 4. 安排财务
 * 
 * 5. 结束销售
 * 
 * 现在假设使用模版方法（Template Method）造型这个流程。首先建立一个定义了算法的抽象类：
 * 
 * @author Will
 * @created at 2014-8-29 上午11:48:51
 */
public abstract class SellVehicleTemplate {
    public void sellVehicle() {
        getCustomerInfo();
        testDriveVehicle();
        negotiateSale();
        arrangeFinancing();
        closeSale();
    }

    public abstract void getCustomerInfo();

    public abstract void testDriveVehicle();

    public abstract void negotiateSale();

    public abstract void arrangeFinancing();

    public abstract void closeSale();
}
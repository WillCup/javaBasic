package com.will.tooljars.netty.onecoder.eight;

import java.io.Serializable;

/**
 * @author lihzh
 * @alia OneCoder
 * @blog http://www.coderli.com
 */
public class Command implements Serializable {
 
    private static final long serialVersionUID = 7590999461767050471L;
 
    private String actionName;
 
    public String getActionName() {
        return actionName;
    }
 
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
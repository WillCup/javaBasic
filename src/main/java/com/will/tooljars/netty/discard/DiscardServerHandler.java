package com.will.tooljars.netty.discard;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 *  DISCARD protocol. It's a protocol which discards any received data without any response.
 *  这是一种协议。忽略所有受到的数据，不给任何回应。
 *  
 *  SimpleChannelHandler是对ChannelHandler的简单实现，可以通过继承然后override的方式使用，比较方便。
 * 
 * @author Will
 * @created at 2014-8-9 下午6:31:24
 */
public class DiscardServerHandler extends SimpleChannelHandler{

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        super.exceptionCaught(ctx, e);
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        super.messageReceived(ctx, e);
        ChannelBuffer buf = (ChannelBuffer) e.getMessage();
        while (buf.readable()) {
            System.out.print((char) buf.readByte());
            System.out.flush();
        }
    }
    
}

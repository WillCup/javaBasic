package com.will.tooljars.netty.time;

import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * It looks very simple and does not look any different from the server side example. 
 * However, this handler sometimes will refuse to work raising an IndexOutOfBoundsException.
 * 
 * @author Will
 * 
 * @created at 2014-8-9 下午9:46:38
 */
public class TimeClientHandler extends SimpleChannelHandler {

    
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        super.messageReceived(ctx, e);
        /**
         * It should receive a 32-bit integer from the server, translate it into a human readable format, 
         * print the translated time, and close the connection
         */
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        long currentTimeMillis = buffer.readInt() * 1000L;
        System.out.println(new Date(currentTimeMillis));
        e.getChannel().close();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        super.exceptionCaught(ctx, e);
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
    
}

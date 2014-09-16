package com.will.tooljars.netty.time;

import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import static org.jboss.netty.buffer.ChannelBuffers.*;
/**
 * The First Solution to note.
 * 
 * A 32-bit integer is a very small amount of data, and it is not likely to be fragmented often. 
 * However, the problem is that it can be fragmented, and the possibility of fragmentation will 
 * increase as the traffic increases.
 * 
 * The simplistic solution is to create an internal cumulative buffer and wait until all 4 bytes
 * are received into the internal buffer.
 * 
 * @author Will
 * @created at 2014-8-9 下午10:07:23
 */
public class TimeClientHandler1 extends SimpleChannelHandler {


    /**
     * A dynamic buffer is a ChannelBuffer which increases its capacity on demand. 
     * It's very useful when you don't know the length of the message.
     */
    private final ChannelBuffer buf = dynamicBuffer();
    
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        super.messageReceived(ctx, e);
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        
        /**
         * First, all received data should be cumulated into buf.
         */
        buffer.writeBytes(buffer);
        
        /**
         * And then, the handler must check if buf has enough data, 4 bytes in this example, 
         * and proceed to the actual business logic.
         * Otherwise, Netty will call the messageReceived method again when more data arrives,
         * and eventually all 4 bytes will be cumulated.
         */
        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = buffer.readInt() * 1000L;
            System.out.println(new Date(currentTimeMillis));
            e.getChannel().close();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        super.exceptionCaught(ctx, e);
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
    
}

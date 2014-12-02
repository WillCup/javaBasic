package com.will.tooljars.netty.echo;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * See http://tools.ietf.org/html/rfc862.
 * 
 * @author Will
 * @created at 2014-8-9 下午9:33:57
 */
public class EchoServerHandler extends SimpleChannelHandler {

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        super.messageReceived(ctx, e);
        Channel channel = e.getChannel();
        channel.write(e.getMessage());
    }
}

package com.will.tooljars.netty.onecoder.eleven;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.will.tooljars.netty.onecoder.eight.Command;

/**
 * 并发访问测试(1)
 * 
 * @author lihzh
 * @alia OneCoder
 * @blog http://www.coderli.com
 */
public class ConcurrencyNettyTestHandler extends SimpleChannelHandler {
 
    private static int count = 0;
 
    /**
     * 当接受到消息的时候触发
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx,
            final ChannelStateEvent e) throws Exception {
        for (int i = 0; i < 100000; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendObject(e.getChannel());
                }
            });
            t.start();
        }
 
    }
 
    /**
     * 发送Object
     * 
     * @author lihzh
     * @alia OneCoder
     */
    private void sendObject(Channel channel) {
        count++;
        Command command = new Command();
        command.setActionName("Hello action." + count);
        System.out.println("Write: " + count);
        channel.write(command);
    }
}
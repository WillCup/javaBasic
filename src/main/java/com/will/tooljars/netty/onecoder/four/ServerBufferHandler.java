package com.will.tooljars.netty.onecoder.four;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * ChannelBuffer
 * 
 * @author lihzh
 * @alia OneCoder
 * @blog http://www.coderli.com
 */
public class ServerBufferHandler extends SimpleChannelHandler {
     
    /**
     * 用户接受客户端发来的消息，在有客户端消息到达时触发
     * 
     * @author lihzh
     * @alia OneCoder
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        // 五位读取
        while (buffer.readableBytes() >= 5) {
            ChannelBuffer tempBuffer = buffer.readBytes(5);
            System.out.println(tempBuffer.toString(Charset.defaultCharset()));
        }
        // 读取剩下的信息
        System.out.println(buffer.toString(Charset.defaultCharset()));
    }
 
}
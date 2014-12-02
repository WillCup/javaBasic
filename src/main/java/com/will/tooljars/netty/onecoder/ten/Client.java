package com.will.tooljars.netty.onecoder.ten;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.will.tooljars.netty.onecoder.eight.Command;


/**
 * 我们刚介绍过了Object的传递，您是否好奇，在Object传递中是否会有这样的问题？
 * 如果Object流的字节截断错乱，那肯定是会出错的。Netty一定不会这么傻的，那么Netty是怎么做的呢？
 * 
 * 在ObjectDecoder的基类LengthFieldBasedFrameDecoder中注释中有详细的说明。
 * 
 * 原来，当初在编码时，在流开头增加的4字节的字符是做这个的。他记录了当前了这个对象流的长度，便于在解码时候准确的计算出该对象流的长度，正确解码。
 * 看来，我们如果我们自己写的对象编码解码的工具，要考虑的还有很多啊。
 * 
 * @author Will
 * @created at 2014-8-12 下午12:02:10
 */
public class Client {
    public static void main(String[] args) {
        ClientBootstrap bootstrap = new ClientBootstrap();
        bootstrap.setFactory(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
     // 设置一个处理服务端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new ObjectEncoder(),
                        new ObjectClientHandler()
                    );
            }
        });
        
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8888));
        future.awaitUninterruptibly();
//        future.addListener(ChannelFutureListener.CLOSE);
        bootstrap.releaseExternalResources();
    }
    
    static class ObjectClientHandler extends SimpleChannelHandler {
        /**
             * 当绑定到服务端的时候触发，给服务端发消息。
             * 
             * @author lihzh
             * @alia OneCoder
             */
            @Override
            public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
                // 向服务端发送Object信息
                sendObject(e.getChannel());
            }
         
            /**
             * 发送Object
             * 
             * @param channel
             * @author lihzh
             * @alia OneCoder
             */
            private void sendObject(Channel channel) {
                Command command = new Command();
                command.setActionName("Hello action.");
                Command commandOne = new Command();
                commandOne.setActionName("Hello action. One");
                Command command2 = new Command();
                command2.setActionName("Hello action. Two");
                channel.write(command2);
                channel.write(command);
                channel.write(commandOne);
            }
    }
}

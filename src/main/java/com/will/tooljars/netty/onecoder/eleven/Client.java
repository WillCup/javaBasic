package com.will.tooljars.netty.onecoder.eleven;

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
 * 在用Netty做并发测试的时候，会出现大量的connection refuse信息
 * 
 * 这个例子其实并不是并发，而只是所谓10w个线程的，单channel的伪并发，或者说是一种持续的连续访问。
 * 并且，如果你跑一下测试用例，会发现，Server端开始接受处理消息，是在Client端10w个线程请求都结束之后再开始的。
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
                        new ConcurrencyNettyTestHandler()
                    );
            }
        });
        
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8888));
        future.awaitUninterruptibly();
//        future.addListener(ChannelFutureListener.CLOSE);
        bootstrap.releaseExternalResources();
    }
    
}

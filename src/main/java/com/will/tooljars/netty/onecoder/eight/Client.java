package com.will.tooljars.netty.onecoder.eight;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;


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
}

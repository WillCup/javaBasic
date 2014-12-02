package com.will.tooljars.netty.onecoder.tweenteen;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.will.tooljars.netty.onecoder.eight.ObjectClientHandler;

/**
 * Netty并发，多connection，socket并发测试
 * 
 * @author lihzh(OneCoder)
 * @alia OneCoder
 * @Blog http://www.coderli.com
 * @date 2012-8-13 下午10:47:28
 */
public class ConcurrencyNettyMultiConnection {
     
    public static void main(String args[]) {
        final ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        // 设置一个处理服务端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ObjectEncoder(),
                        new ObjectClientHandler()
                );
            }
        });
        for (int i = 0; i < 3; i++) {
            bootstrap.connect(new InetSocketAddress("127.0.0.1", 8000));
        }
    }
}
package com.will.tooljars.netty.will;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

public class AdminClient {
    public static void main(String[] args) {
        ClientBootstrap boot = null;
        Channel channel = null;
        try {
            NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
            boot = new ClientBootstrap(factory);
            
            boot.setPipelineFactory(new ChannelPipelineFactory() {
                
                public ChannelPipeline getPipeline() throws Exception {
                    ChannelPipeline pipeline = Channels.pipeline();
                    pipeline.addLast("decoder", new HttpRequestDecoder());
                    pipeline.addLast("encoder", new HttpResponseEncoder());
                    pipeline.addLast("handler", new AdminClientHandler());
                    return pipeline;
                }
            });
            URI uri = new URI("http://localhost:8080");
            ChannelFuture future = boot.connect(new InetSocketAddress(uri .getHost(), uri.getPort()));
            future.syncUninterruptibly();
            channel = future.getChannel();
            channel.write("Hello there?".getBytes("UTF-8"));
            channel.getCloseFuture().awaitUninterruptibly();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (boot != null) {
                boot.releaseExternalResources();
            }
        }
    }
    
    static class AdminClientHandler extends SimpleChannelHandler {

        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                throws Exception {
            super.exceptionCaught(ctx, e);
            e.getCause().printStackTrace();
        }
        
    }
}

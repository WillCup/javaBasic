package com.will.tooljars.netty.time;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.elasticsearch.common.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * The biggest and only difference between a server and a client in Netty is that 
 * different Bootstrap and ChannelFactory are required.
 * 
 * @author Will
 * @created at 2014-8-9 下午9:41:29
 */
public class TimeClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        
        /**
         * NioClientSocketChannelFactory, instead of NioServerSocketChannelFactory was used to create a client-side Channel.
         */
        NioClientSocketChannelFactory nioClientSocketChannelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        
        /**
         * ClientBootstrap is a client-side counterpart of ServerBootstrap.
         */
        ClientBootstrap clientBootstrap = new ClientBootstrap(nioClientSocketChannelFactory);
        
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
//                return Channels.pipeline(new TimeClientHandler());
                return Channels.pipeline(
                        new TimeDecoder(), 
                        new TimeClientHandler()
                        );
            }
        });
        
        /**
         * Please note that there's no "child." prefix. A client-side SocketChannel does not have a parent.
         */
        clientBootstrap.setOption("tcpNoDelay", true);
        clientBootstrap.setOption("keepAlive", true);
        
        /**
         * We should call the connect method instead of the bind method.
         * The connect method of ClientBootstrap returns a ChannelFuture which notifies when a connection attempt
         * succeeds or fails.
         * It also has a reference to the Channel which is associated with the connection attempt.
         */
        ChannelFuture futrue = clientBootstrap.connect(new InetSocketAddress(port));
        
        /**
         * Wait for the returned ChannelFuture to determine if the connection attempt was successful or not.
         */
        futrue.awaitUninterruptibly();
        
        if (!futrue.isSuccess()) {
            futrue.getCause().printStackTrace();
        }
        
        /**
         * Now that the connection attempt is over, we need to wait until the connection is closed by waiting 
         * for the closeFuture of the Channel. Every Channel has its own closeFuture so that you are notified 
         * and can perform a certain action on closure.
         * 
         * Even if the connection attempt has failed the closeFuture will be notified because the Channel will
         *  be closed automatically when the connection attempt fails.
         */
        futrue.getChannel().getCloseFuture().awaitUninterruptibly();
        
        /**
         * All connections have been closed at this point. The only task left is to release the resources being 
         * used by ChannelFactory. It is as simple as calling its releaseExternalResources() method. All resources 
         * including the NIO Selectors and thread pools will be shut down and terminated automatically.
         */
        nioClientSocketChannelFactory.releaseExternalResources();
    }
}

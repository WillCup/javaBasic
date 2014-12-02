package com.will.tooljars.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.will.tooljars.netty.time.TimeServerHandler;

public class DiscardServer {
    /**
     * Shutting down a client was pretty easy, but how about shutting down a server? 
     * You need to unbind from the port and close all open accepted connections. To do this, you need 
     * a data structure that keeps track of the list of active connections, and it's not a trivial task. 
     * Fortunately, there is a solution, ChannelGroup.
     * 
     * ChannelGroup is a special extension of Java collections API which represents a set of open Channels. 
     * If a Channel is added to a ChannelGroup and the added Channel is closed, the closed Channel is removed 
     * from its ChannelGroup automatically. You can also perform an operation on all Channels in the same group.
     * 
     * Yes, ChannelGroup is thread-safe.
     * 
     * 
     * DefaultChannelGroup requires the name of the group as a constructor parameter. 
     * The group name is solely used to distinguish one group from others.
     */
    static final ChannelGroup allChannels = new DefaultChannelGroup("server");
    
    public static void main(String[] args) {
        /**
         * ChannelFactory is a factory which creates and manages Channels and its related resources. 
         * It processes all I/O requests and performs I/O to generate ChannelEvents. 
         * Netty provides various ChannelFactory implementations.
         *  We are implementing a server-side application in this example, and therefore NioServerSocketChannelFactory was used. 
         *  Another thing to note is that it does not create I/O threads by itself. 
         *  It is supposed to acquire threads from the thread pool you specified in the constructor, 
         *  and it gives you more control over how threads should be managed in the environment where your application runs,
         *   such as an application server with a security manager.
         */
        ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        
        /**
         * ServerBootstrap is a helper class that sets up a server. You can set up the server using a Channel directly. 
         * However, please note that this is a tedious process and you do not need to do that in most cases.
         */
        ServerBootstrap bootStrap = new ServerBootstrap(factory);
        
        /**
         * Here, we configure the ChannelPipelineFactory. Whenever a new connection is accepted by the server, 
         * a new ChannelPipeline will be created by the specified ChannelPipelineFactory. 
         * The new pipeline contains the DiscardServerHandler. 
         * As the application gets complicated, it is likely that you will add more handlers to the pipeline 
         * and extract this anonymous class into a top level class eventually.
         */
        bootStrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
//                return Channels.pipeline(new DiscardServerHandler());
                return Channels.pipeline(new TimeServerHandler());
//                return Channels.pipeline(new EchoServerHandler());
            }
        });
        
        /**
         * You can also set the parameters which are specific to the Channel implementation. 
         * We are writing a TCP/IP server, so we are allowed to set the socket options such as tcpNoDelay and keepAlive. 
         * Please note that the "child." prefix was added to all options. 
         * It means the options will be applied to the accepted Channels instead of the options of the ServerSocketChannel. 
         * You could do the following to set the options of the ServerSocketChannel:
         *      bootstrap.setOption("reuseAddress", true);
         */
        bootStrap.setOption("child.tcpNoDelay", true);
        bootStrap.setOption("child.keepAlive", true);
        
        /**
         * Here, we bind to the port 8080 of all NICs (network interface cards) in the machine. 
         * You can now call the bind method as many times as you want (with different bind addresses.)
         * 
         * 
         * The bind method of ServerBootstrap returns a server side Channel which is bound to the specified local address. 
         * Calling the close() method of the returned Channel will make the Channel unbind from the bound local address.
         */
        Channel channel = bootStrap.bind(new InetSocketAddress(8080));
        System.out.println("already started........");
        
        /**
         * Any type of Channels can be added to a ChannelGroup regardless if it is either server side, client-side, or accepted.
         * Therefore, you can close the bound Channel along with the accepted Channels in one shot when the server shuts down.
         */
        allChannels.add(channel);

        /**
         * waitForShutdownCommand() is an imaginary method that waits for the shutdown signal. You could wait for a message 
         * from a privileged client or the JVM shutdown hook. 
         */
//        allChannels.waitForShutdownCommand();
        
        /**
         * You can perform the same operation on all channels in the same ChannelGroup. In this case, we close all channels, 
         * which means the bound server-side Channel will be unbound and all accepted connections will be closed asynchronously. 
         * To notify when all connections were closed successfully, it returns a ChannelGroupFuture which has a similar role with 
         * ChannelFuture.
         */
        ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly();
        factory.releaseExternalResources();
    }
}

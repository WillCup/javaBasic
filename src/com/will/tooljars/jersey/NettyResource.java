package com.will.tooljars.jersey;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

@Path("/hellonetty")
public class NettyResource {
    @GET
    @Produces("text/plain")
    public String getClichedMessage(@PathParam(value = "na") String name) {
        System.out.println(name);
        return "Hello Netty";
    }

    public static void main(String[] args) {
        NioServerSocketChannelFactory channelFactory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

        // bootstrap.setPipelineFactory(new HttpServerPipelineFactory());
        bootstrap.bind(new InetSocketAddress(8080));
    }
}

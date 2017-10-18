package com.will.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String args[]) {
        ServerBootstrap bootsrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(Executors
                        .newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootsrap.setPipelineFactory(new PipelineFactoryTest());
        bootsrap.bind(new InetSocketAddress(8888));
    }
}

class PipelineFactoryTest implements ChannelPipelineFactory {

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("1", new UpstreamHandlerA());
        pipeline.addLast("2", new UpstreamHandlerB());
        pipeline.addLast("3", new DownstreamHandlerA());
        pipeline.addLast("4", new DownstreamHandlerB());
        pipeline.addLast("5", new UpstreamHandlerX());
        return pipeline;
    }
}

class UpstreamHandlerB extends SimpleChannelUpstreamHandler {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        System.out
                .println("UpstreamHandlerB.messageReceived：" + e.getMessage());
        ctx.sendUpstream(e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        System.out.println("UpstreamHandlerB.exceptionCaught：" + e.toString());
        e.getChannel().close();
    }
}

class UpstreamHandlerA extends SimpleChannelUpstreamHandler {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        Channel ctxchannel = ctx.getChannel();
        Channel echannel = e.getChannel();

        System.out.println(ctxchannel.equals(echannel));//handle和event共享一个channel       System.out.println("UpstreamHandlerA.messageReceived:" + e.getMessage());
        ctx.sendUpstream(e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        System.out.println("UpstreamHandlerA.exceptionCaught:" + e.toString());
        e.getChannel().close();
    }
}


class UpstreamHandlerX extends SimpleChannelUpstreamHandler {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        System.out.println("UpstreamHandlerX.messageReceived:" + e.getMessage());
        String msg = "Hello, This is Will";
        ChannelBuffer buffer2 = ChannelBuffers.buffer(msg.length());
        buffer2.writeBytes(msg.getBytes());
        e.getChannel().write(buffer2);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        System.out.println("UpstreamHandlerX.exceptionCaught");
        e.getChannel().close();
    }
}

class DownstreamHandlerA extends SimpleChannelDownstreamHandler {
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
            throws Exception {
        System.out.println("DownstreamHandlerA.handleDownstream");
        super.handleDownstream(ctx, e);
    }
}

class DownstreamHandlerB extends SimpleChannelDownstreamHandler {
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e)
            throws Exception {
        System.out.println("DownstreamHandlerB.handleDownstream:");
        super.handleDownstream(ctx, e);
    }
}
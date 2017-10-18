package com.will.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * http://blog.csdn.net/qian_348840260/article/details/8990109
 */
class AppClientChannelPipelineFactory implements ChannelPipelineFactory {

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        //pipeline.addLast("encode", new StringEncoder());
        pipeline.addLast("handler", new AppStoreClientHandler());
        return pipeline;
    }

}

class AppStoreClientHandler extends SimpleChannelUpstreamHandler {

    private static Logger log = Logger.getLogger(AppStoreClientHandler.class);

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
        System.out.println(buffer.toString(Charset.defaultCharset()));
    }

    @Override

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

    }

}

public class AppStoreClinetBootstrap {
    public static void main(String args[]) {
        ExecutorService bossExecutor = Executors.newCachedThreadPool();
        ExecutorService workerExecutor = Executors.newCachedThreadPool();
        ChannelFactory channelFactory = new NioClientSocketChannelFactory(bossExecutor, workerExecutor);
        ClientBootstrap bootstarp = new ClientBootstrap(channelFactory);
        bootstarp.setPipelineFactory(new AppClientChannelPipelineFactory());

        ChannelFuture future = bootstarp.connect(new InetSocketAddress("localhost", 8888));
        future.awaitUninterruptibly();
        if (future.isSuccess()) {
            String msg = "hello word";
            ChannelBuffer buffer = ChannelBuffers.buffer(msg.length());
            buffer.writeBytes(msg.getBytes());
            future.getChannel().write(buffer);
        }
    }
}
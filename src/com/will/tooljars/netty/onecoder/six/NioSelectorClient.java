package com.will.tooljars.netty.onecoder.six;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Java NIO Selector模式，客户端代码
 * 
 * @author lihzh
 * @alia OneCoder
 * @blog http://www.coderli.com
 */
public class NioSelectorClient {
 
    /**
     * @author lihzh
     * @throws IOException 
     * @alia OneCoder
     */
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1", 8000));
        channel.close();
    }
}
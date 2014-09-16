package com.will.nio.will;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.will.nio.socketfile.MyServer4;

public class WillServer {
    private final static Logger logger = Logger.getLogger(MyServer4.class
            .getName());

    static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;

        try {
            // Selector for incoming time requests
            selector = Selector.open();

            // Create a new server socket and set to non blocking mode
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            // Bind the server socket to the local host and port
            serverSocketChannel.socket().setReuseAddress(true);
            serverSocketChannel.socket().bind(new InetSocketAddress(10000));

            // Register accepts on the server socket with the selector. This
            // step tells the selector that the socket wants to be put on the
            // ready list when accept operations occur, so allowing multiplexed
            // non-blocking I/O to take place.
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // Here's where everything happens. The select method will
            // return when any operations registered above have occurred, the
            // thread has been interrupted, etc.
            while (selector.select() > 0) {
                // Someone is ready for I/O, get the ready keys
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                // Walk through the ready keys collection and process date
                // requests.
                while (it.hasNext()) {
                    SelectionKey readyKey = it.next();
                    it.remove();

                    // The key indexes into the selector so you
                    // can retrieve the socket that's ready for I/O
                    doit((ServerSocketChannel) readyKey.channel());
                }
            }
        } catch (ClosedChannelException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            try {
                selector.close();
            } catch (Exception ex) {
            }
            try {
                serverSocketChannel.close();
            } catch (Exception ex) {
            }
        }
    }

    private static void doit(final ServerSocketChannel serverSocketChannel)
            throws IOException {
        SocketChannel socketChannel = null;
        try {
            socketChannel = serverSocketChannel.accept();

            Map map = handRequest(socketChannel);
            
            ConfigInfo info = new ConfigInfo();
            info.setName("will");
            
            sendData(socketChannel, info);
        } finally {
            try {
                socketChannel.close();
            } catch (Exception ex) {
            }
        }

    }

    private static Map handRequest(SocketChannel socketChannel)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Map map;
        try {
            byte[] bytes;
            int size = 0;
            while ((size = socketChannel.read(buffer)) >= 0) {
                buffer.flip();
                bytes = new byte[size];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
            }
            bytes = baos.toByteArray();
            map = mapper.readValue(bytes, Map.class);
            System.out.println(map);
        } finally {
            try {
                baos.close();
            } catch (Exception ex) {
            }
        }
        return map;
    }

    private static void sendData(SocketChannel socketChannel,
            ConfigInfo configInfo) throws IOException {
        byte[] bytes = mapper.writeValueAsBytes(configInfo);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        socketChannel.write(buffer);
    }
}

package com.will.tooljars.netty.time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * Time Protocol. 
 * This protocol provides a site-independent, machine readable date and time.
 * The Time service sends back to the originating source the time in seconds since midnight on January first 1900.
 * 
 * see http://tools.ietf.org/html/rfc868
 * 
 * To test if our time server works as expected, you can use the UNIX rdate command:
 *      $ rdate -o <port> -p <host>
 * where port is the port number you specified in the main() method and host is usually localhost.
 * 
 * @author Will
 * @created at 2014-8-9 下午9:15:57
 */
public class TimeServerHandler extends SimpleChannelHandler {

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        super.exceptionCaught(ctx, e);
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        super.channelConnected(ctx, e);
        Channel channel = e.getChannel();
        /**
         * To send a new message, we need to allocate a new buffer which will contain the message. 
         * We are going to write a 32-bit integer, and therefore we need a ChannelBuffer whose capacity is 4 bytes. 
         * The ChannelBuffers helper class is used to allocate a new buffer. 
         * Besides the buffer method, ChannelBuffers provides a lot of useful methods related to the ChannelBuffer.
         */
        ChannelBuffer time = ChannelBuffers.buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        
        /**
         *  As usual, we write the constructed message.
         *  But wait, where's the flip? Didn't we used to call ByteBuffer.flip() before sending a message in NIO?
         *   ChannelBuffer does not have such a method because it has two pointers; 
         *   one for read operations and the other for write operations. 
         *   The writer index increases when you write something to a ChannelBuffer while the reader index does not change. 
         *   The reader index and the writer index represents where the message starts and ends respectively.
         *   In contrast, NIO buffer does not provide a clean way to figure out where the message content starts and ends 
         *   without calling the flip method. You will be in trouble when you forget to flip the buffer because nothing or 
         *   incorrect data will be sent. Such an error does not happen in Netty because we have different pointer for 
         *   different operation types. You will find it makes your life much easier as you get used to it -- a life without
         *   flipping out!
         *    
         *   Another point to note is that the write method returns a ChannelFuture. 
         *   A ChannelFuture represents an I/O operation which has not yet occurred. It means, 
         *   any requested operation might not have been performed yet because all operations are asynchronous in Netty. 
         *    
         *   For example, the following code might close the connection even before a message is sent:
         *        Channel ch = ...;
         *        ch.write(message);
         *        ch.close();
         *        
         *   Therefore, you need to call the close method after the ChannelFuture, 
         *   which was returned by the write method, notifies you when the write operation has been done. 
         *   Please note that, close also might not close the connection immediately, and it returns a ChannelFuture.
         */
        ChannelFuture channelFuture = channel.write(time);
        
        /**
         * How do we get notified when the write request is finished then? 
         * This is as simple as adding a ChannelFutureListener to the returned ChannelFuture. 
         * Here, we created a new anonymous ChannelFutureListener which closes the Channel when the operation is done.
         *
         * Alternatively, you could simplify the code using a pre-defined listener:
         *      f.addListener(ChannelFutureListener.CLOSE);
         */
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                future.getChannel().close();
            }
        });
    }
    
}

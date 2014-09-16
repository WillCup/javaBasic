package com.will.tooljars.netty.time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;

/**
 * The Second Solution to note.
 * 
 * Although the first solution has resolved the problem with the TIME client, the modified handler does
 * not look that clean. Imagine a more complicated protocol which is composed of multiple fields such as
 * a variable length field. Your ChannelHandler implementation will become unmaintainable very quickly.
 * 
 * As you may have noticed, you can add more than one ChannelHandler to a ChannelPipeline, and therefore,
 * you can split one monolithic ChannelHandler into multiple modular ones to reduce the complexity of your
 * application. For example, you could split TimeClientHandler into two handlers:
 *      1. TimeDecoder which deals with the fragmentation issue;
 *      2. the initial simple version of TimeClientHandler.
 *       
 * @author Will
 * @created at 2014-8-9 下午10:13:33
 */
public class TimeDecoder extends ReplayingDecoder<VoidEnum>{

    protected Object decode(ChannelHandlerContext ctx, Channel channel,
            ChannelBuffer buffer, VoidEnum state) throws Exception {
        return buffer.readBytes(4);
    }
    
}

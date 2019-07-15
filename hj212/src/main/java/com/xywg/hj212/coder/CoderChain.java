package com.xywg.hj212.coder;

import com.wyf.iot.properties.NettyProperties;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:45 2019/4/9
 * Modified By : wangyifei
 * 1.解决拆包分包
 *  客户端->lineDecoder->stringDecoder->bisDecoder->MessageHandler->bisEncoder->lineEncoder
 */
public class CoderChain implements com.wyf.iot.common.CoderChain {
    @Override
    public void intProtocolHandler(ChannelPipeline channelPipeline, NettyProperties serverBean) {
        channelPipeline.addLast("lineDecoder",new LineBasedFrameDecoder(1036))
                .addLast("stringDecoder",new StringDecoder())
                .addLast("bisDecoder",new Decoder())
                .addFirst("bisEncoder",new Encoder())
                .addFirst("stringEncoder",new StringEncoder());

    }
}

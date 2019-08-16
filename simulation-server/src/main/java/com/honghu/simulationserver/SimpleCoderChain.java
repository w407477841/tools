package com.honghu.simulationserver;

import com.wyf.iot.common.CoderChain;
import com.wyf.iot.properties.NettyProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class SimpleCoderChain implements CoderChain {
    @Override
    public void intProtocolHandler(ChannelPipeline channelPipeline, NettyProperties serverBean) {
        // 解码器，解决粘包半包
        ByteBuf delimiter = Unpooled.copiedBuffer("hahahahaha".getBytes());
        channelPipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
        channelPipeline.addLast(new DelimiterBasedFrameDecoder(64, delimiter));
        channelPipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
    }
}

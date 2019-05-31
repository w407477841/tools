package com.xywg.iot.mqtt.modular.coder;

import com.wyf.iot.common.CoderChain;
import com.wyf.iot.properties.NettyProperties;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:27 2019/3/31
 * Modified By : wangyifei
 */
public class HttpCoderChain implements CoderChain {


    @Override
    public void intProtocolHandler(ChannelPipeline channelPipeline, NettyProperties serverBean) {
        // 请求消息解码器
        channelPipeline.addLast("http-decoder",new HttpRequestDecoder());
        // 目的是将多个消息转换为单一的request或者response对象
        channelPipeline.addLast("http-aggregator",
         new HttpObjectAggregator(65536));
        //响应解码器
        channelPipeline.addLast("http-encoder",
          new HttpResponseEncoder());
        //目的是支持异步大文件传输（）
        channelPipeline.addLast("http-chunked",
          new ChunkedWriteHandler());
    }



}

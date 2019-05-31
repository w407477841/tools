package com.wyf.iot.common;

import com.wyf.iot.properties.NettyProperties;
import io.netty.channel.ChannelPipeline;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:26 2019/3/31
 * Modified By : wangyifei
 */
public class SimpleCoderChain implements CoderChain {


    @Override
    public void intProtocolHandler(ChannelPipeline channelPipeline, NettyProperties serverBean) {

    }
}

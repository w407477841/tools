package com.xywg.hj212.coder;

import com.wyf.iot.properties.NettyProperties;
import io.netty.channel.ChannelPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:45 2019/4/9
 * Modified By : wangyifei
 */
public class CoderChain implements com.wyf.iot.common.CoderChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoderChain.class);

    @Override
    public void intProtocolHandler(ChannelPipeline channelPipeline, NettyProperties serverBean) {

    }
}

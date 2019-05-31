package com.wyf.iot.common;

import com.wyf.iot.properties.NettyProperties;
import io.netty.channel.ChannelPipeline;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:23 2019/3/31
 * Modified By : wangyifei
 */
public interface CoderChain {

 void intProtocolHandler(ChannelPipeline channelPipeline, NettyProperties serverBean);

}

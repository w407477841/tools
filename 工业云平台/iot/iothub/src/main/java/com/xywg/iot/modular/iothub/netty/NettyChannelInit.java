package com.xywg.iot.modular.iothub.netty;

import com.xywg.iot.modular.iothub.netty.handler.IotHubHandler;
import com.xywg.iot.modular.iothub.netty.handler.XyIdleStateHandler;
import com.xywg.iot.util.ApplicationContextProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:37 2018/10/15
 * Modified By : wangyifei
 */
@Component
public class NettyChannelInit extends ChannelInitializer<SocketChannel> {


    private IotHubHandler iotHubHandler;




    @Autowired
    public NettyChannelInit(IotHubHandler iotHubHandler) {
        this.iotHubHandler = iotHubHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {

        ch.pipeline()
                .addLast(ApplicationContextProvider.getBean(XyIdleStateHandler.class))
                .addLast(iotHubHandler);
    }
}

package com.xywg.iot.modular.colud.thread;

import com.xywg.iot.modular.colud.netty.config.NettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:45 2019/1/23
 * Modified By : wangyifei
 */
public abstract class ClientThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientThread.class);

    private final NettyConfig nettyConfig ;

    public ClientThread(NettyConfig nettyConfig) {
        this.nettyConfig = nettyConfig;
    }

    @Override
    public void run() {
             this.run(nettyConfig);
    }

   protected abstract void run(NettyConfig nettyConfig);


}

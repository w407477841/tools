package com.xywg.iot.modular.colud.thread.impl;

import com.xywg.iot.modular.colud.netty.config.NettyConfig;
import com.xywg.iot.modular.colud.thread.ClientThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:49 2019/1/23
 * Modified By : wangyifei
 */
public class SimpleClientThread extends ClientThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClientThread.class);

    private final String loginJson;
    private final String clientId;

    public SimpleClientThread(NettyConfig nettyConfig, String loginJson,String clientId) {
        super(nettyConfig);
        this.loginJson = loginJson;
        this.clientId = clientId ;
    }

    @Override
    protected void run(NettyConfig nettyConfig) {
        try {
            nettyConfig.start(loginJson,clientId);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
}

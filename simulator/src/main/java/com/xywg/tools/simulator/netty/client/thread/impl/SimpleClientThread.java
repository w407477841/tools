package com.xywg.tools.simulator.netty.client.thread.impl;

import com.xywg.tools.simulator.netty.client.ClientConfig;
import com.xywg.tools.simulator.netty.client.thread.ClientThread;
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

    private final String clientId;

    public SimpleClientThread(ClientConfig clientConfig, String clientId) {
        super(clientConfig);
        this.clientId = clientId ;
    }

    @Override
    protected void run(ClientConfig clientConfig) {
        try {
            clientConfig.start(clientId);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
}

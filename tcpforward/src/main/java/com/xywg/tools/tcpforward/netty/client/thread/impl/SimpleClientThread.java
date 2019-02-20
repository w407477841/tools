package com.xywg.tools.tcpforward.netty.client.thread.impl;

import com.xywg.tools.tcpforward.netty.client.ClientConfig;
import com.xywg.tools.tcpforward.netty.client.thread.ClientThread;
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

    private final String serverId;
    private final String clientId;

    public SimpleClientThread(ClientConfig clientConfig, String serverId, String clientId) {
        super(clientConfig);
        this.serverId = serverId;
        this.clientId = clientId ;
    }

    @Override
    protected void run(ClientConfig clientConfig) {
        try {
            clientConfig.start(serverId,clientId);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
}

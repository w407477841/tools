package com.xywg.tools.simulator.netty.client.thread;

import com.xywg.tools.simulator.netty.client.ClientConfig;
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

    private final ClientConfig clientConfig ;

    public ClientThread(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public void run() {
             this.run(clientConfig);
    }

   protected abstract void run(ClientConfig clientConfig);


}

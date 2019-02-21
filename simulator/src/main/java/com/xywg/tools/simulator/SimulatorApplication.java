package com.xywg.tools.simulator;

import cn.hutool.core.util.RandomUtil;
import com.xywg.tools.simulator.netty.client.ClientConfig;
import com.xywg.tools.simulator.netty.client.thread.impl.SimpleClientThread;
import com.xywg.tools.simulator.netty.common.ApplicationContextProvider;
import com.xywg.tools.simulator.netty.common.Const;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApplication.class, args);


            ClientConfig clientConfig = ApplicationContextProvider.getBean(ClientConfig.class);
             new Thread(new SimpleClientThread(clientConfig,Const.clientId)).start();
            while(true){

            }

    }

}

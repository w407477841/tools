package com.xywg.tools.tcpforward;

import com.xywg.tools.tcpforward.netty.common.ApplicationContextProvider;
import com.xywg.tools.tcpforward.netty.server.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TcpforwardApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcpforwardApplication.class, args);

        try {
            ApplicationContextProvider.getBean(ServerConfig.class).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

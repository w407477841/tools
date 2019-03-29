package com.xywg.tools.rpc;

import com.xywg.tools.rpc.netty.common.ApplicationContextProvider;
import com.xywg.tools.rpc.netty.config.RpcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcApplication.class, args);

        try {
            ApplicationContextProvider.getBean(RpcConfig.class).start();
        } catch (InterruptedException e) {
            System.out.println("启动失败");
        }

    }

}

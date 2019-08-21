package com.honghu.oauth2.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class ServerApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext cac =  SpringApplication.run(ServerApplication.class,args);
        ServerProperties serverProperties   = cac.getBean(ServerProperties.class);
        log.info("------------------------------------------------------------------------------------");
        log.info("|                                OAUTH2服务已启动                                    |");
        log.info("|                      访问 http://locahost:{}/login                                 |",serverProperties.getPort());
        log.info("------------------------------------------------------------------------------------");
    }

}

package com.wyf.pp.wyfpp;

import com.wyf.pp.wyfpp.server.IServer;
import com.wyf.pp.wyfpp.server.impl.Serverimpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WyfPpApplication {
    @Bean
    public  IServer server(){
        IServer  server= new Serverimpl();
        return server;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WyfPpApplication.class, args);
        try {
            context.getBean(IServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

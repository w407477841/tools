package com.wyf.rabbitmqtest.rabbitadminconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RabbitAdminConsumerApplication {

    public static void main(String[] args) {

        SpringApplication.run(RabbitAdminConsumerApplication.class, args);

  //      SpingUtil.applicationContext.getBean(RabbitConfig.class).start();




    }

}

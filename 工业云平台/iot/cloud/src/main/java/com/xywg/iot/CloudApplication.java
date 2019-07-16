package com.xywg.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
/**
* @author: wangyifei
* Description:  入口
* Date: 17:49 2018/12/19
*/
@EnableScheduling
public class CloudApplication {

    public static void main(String[] args) {

        SpringApplication.run(CloudApplication.class, args);



    }

}


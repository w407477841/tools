package com.example.springbootadminclient.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:16 2019/5/17
 * Modified By : wangyifei
 */

@Component
public class Tast1 {
    @Scheduled(cron = "0/3 * * * * ?")
    public void run(){

        System.out.println("haha");

    }


}

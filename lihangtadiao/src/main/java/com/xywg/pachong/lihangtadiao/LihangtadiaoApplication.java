package com.xywg.pachong.lihangtadiao;

import com.xywg.pachong.lihangtadiao.core.WbHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LihangtadiaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LihangtadiaoApplication.class, args);

        WbHandler.satrt();




    }

}

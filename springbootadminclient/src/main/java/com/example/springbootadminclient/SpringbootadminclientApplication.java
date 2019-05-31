package com.example.springbootadminclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringbootadminclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootadminclientApplication.class, args);
    }

}

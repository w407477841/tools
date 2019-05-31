package com.example.springbootadminserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SpringbootadminserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootadminserverApplication.class, args);
    }

}

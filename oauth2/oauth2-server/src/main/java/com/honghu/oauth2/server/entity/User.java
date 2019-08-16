package com.honghu.oauth2.server.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private Integer id;
    private String userUuid;
    private String username;
    private String password;
    private String email;
    private String telephone;
    private String role;
    private String image;
    private String lastIp;
    private String lastTime;

}

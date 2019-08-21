package com.honghu.oauth2.server.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Client {

    private Integer id;
    private String clinetId;
    private String clientName;
    private String secret;
    private Long Expiration;
    private String mainUrl;
}

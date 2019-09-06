package com.honghu.oauth2.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoadPagesDTO {

    private String clientId;
    private String username;

}

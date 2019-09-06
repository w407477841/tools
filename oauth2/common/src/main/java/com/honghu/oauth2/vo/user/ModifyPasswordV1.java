package com.honghu.oauth2.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ModifyPasswordV1 {
    @NotBlank
    private  String username;
    @NotBlank
    private String password;
    @NotBlank
    private String oldPassword;

}

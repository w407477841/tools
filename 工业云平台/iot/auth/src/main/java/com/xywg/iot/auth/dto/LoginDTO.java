package com.xywg.iot.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
* @author: wangyifei
* Description:
* Date: 17:33 2018/12/20
*/
@Data
@ApiModel(value="LoginDTO",description = "登录对象")
public class LoginDTO {
	@ApiModelProperty(value = "用户名",name = "username")
	private String username;
	@ApiModelProperty(value = "密码",name = "password")
	private String password;
}

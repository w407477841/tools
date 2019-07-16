package com.xywg.iot.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:06 2018/12/25
 * Modified By : wangyifei
 */
@Data
@ApiModel
public class RePasswordDTO {
    @ApiModelProperty(value = "用户名",name = "username")
    private String username;
    @ApiModelProperty(value = "密码",name = "password")
    private String password;
    @ApiModelProperty(value = "新密码",name = "newPassword")
    private String newPassword;


}

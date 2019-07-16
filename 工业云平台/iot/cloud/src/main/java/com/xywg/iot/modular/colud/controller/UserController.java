package com.xywg.iot.modular.colud.controller;

import com.xywg.iot.base.BaseController;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.modular.cloud.model.CloudUser;
import com.xywg.iot.modular.cloud.service.ICloudUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:51 2018/12/26
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("user")
@Api
public class UserController extends BaseController<CloudUser,ICloudUserService> {

    @GetMapping("info")
    public Object userInfo(@RequestHeader(value = "user") String user){
        String key   =  user;
        CloudUser cloudUser =  service.selectCacheOne(key);
        return cloudUser;
    }

}

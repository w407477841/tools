package com.honghu.oauth2.server.system.controller;

import com.honghu.oauth2.Const;
import com.honghu.oauth2.controller.BaseController;
import com.honghu.oauth2.server.system.service.IUserService;
import com.honghu.oauth2.system.entity.User;
import com.honghu.oauth2.vo.ResultVO;
import com.honghu.oauth2.vo.user.ModifyPasswordV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/system/user")
@Slf4j
public class UserController extends BaseController<IUserService,User> {
    @Override
    protected String[] insertRoles() {
        return new String []{Const.ADMIN_ROLE};
    }

    @Override
    protected String[] updateRoles() {
        return new String []{Const.ADMIN_ROLE};
    }

    @Override
    protected String[] deleteRoles() {
        return new String []{Const.ADMIN_ROLE};
    }

    @Override
    protected String[] queryRoles() {
        return new String []{Const.ADMIN_ROLE};
    }

    /**
     * 使用旧密码 修改
     * @return
     */
    @PostMapping("v1/modifyPassword")
    public ResultVO modifyPassword(@RequestBody ModifyPasswordV1 modifyPasswordV1){
        return baseService.modifyPasswordV1(modifyPasswordV1);
    }



}

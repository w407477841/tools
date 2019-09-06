package com.honghu.oauth2.server.system.controller;

import com.honghu.oauth2.Const;
import com.honghu.oauth2.controller.BaseController;
import com.honghu.oauth2.server.system.service.IClientService;
import com.honghu.oauth2.system.entity.Client;
import org.springframework.web.bind.annotation.*;



@RequestMapping("/oauth2/system/client")
@RestController
public class ClientController extends BaseController<IClientService,Client> {

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
}

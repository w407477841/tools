package com.xywg.iot.modular.colud.controller;

import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.dictionary.model.MasterEvent;
import com.xywg.iot.modular.dictionary.service.IMasterEventService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 *@author:jixiaojun
 *DATE:2019/2/18
 *TIME:9:32
 */
@RestController
@RequestMapping("/business/master/event")
public class MasterEventController extends BaseControllerPlus<MasterEvent,IMasterEventService> {
    public MasterEventController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE};
        this.setQueryRoles(roles);
    }
}

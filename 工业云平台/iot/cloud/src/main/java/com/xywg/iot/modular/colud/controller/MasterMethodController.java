package com.xywg.iot.modular.colud.controller;

import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.modular.dictionary.dto.MasterMethodDTO;
import com.xywg.iot.modular.dictionary.model.MasterMethod;
import com.xywg.iot.modular.dictionary.service.IMasterMethodService;
import com.xywg.iot.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:18 2019/1/10
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/business/master/method")
public class MasterMethodController extends BaseControllerPlus<MasterMethod,IMasterMethodService> {
    public MasterMethodController() {
        // 管理员和认证后的用户可以查询
        this.setQueryRoles(RoleConstant.LEVEL_AUTH);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterMethodController.class);


}

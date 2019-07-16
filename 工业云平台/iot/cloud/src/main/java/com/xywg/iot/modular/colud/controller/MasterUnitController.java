package com.xywg.iot.modular.colud.controller;

import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.modular.dictionary.model.MasterMeasureUnit;
import com.xywg.iot.modular.dictionary.service.IMasterMeasureUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:28 2019/1/4
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/business/master/unit")
public class MasterUnitController extends BaseControllerPlus<MasterMeasureUnit,IMasterMeasureUnitService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterUnitController.class);

    public MasterUnitController() {
        this.setQueryRoles(RoleConstant.LEVEL_AUTH);
    }
}

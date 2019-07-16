package com.xywg.iot.modular.colud.controller.system;

import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.modular.system.model.Areas;
import com.xywg.iot.modular.system.service.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hjy
 * @date 2019/1/8
 * 省市区 基础表信息控制器
 */
@RestController
@RequestMapping("/business/areas")
public class AreasController {
    @Autowired
    private AreasService areasService;


    /**
     * 根据条件区域列表     特殊基础数据  请勿加权限过滤
     * @param areas
     * @return
     */
    @PostMapping("/getAreas")
    @OpenLog
    public List<Areas> getAreasListByParentId(@RequestBody Areas areas) {

        return areasService.getAreasList(areas);
    }

}

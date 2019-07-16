package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.DevicePropertyRecordService;
import com.xywg.iot.modular.device.model.DevicePropertyRecord;
import com.xywg.iot.modular.device.service.IDevicePropertyRecordService;
import com.xywg.iot.modular.device.vo.DevicePropertyRecordVo;
import com.xywg.iot.vo.ResultVO;
import com.xywg.log.annotations.OpenLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2019/1/8
 *TIME:21:14
 */
@RestController
@RequestMapping("/business/devicePropertyRecord")
public class DevicePropertyRecordController extends BaseControllerPlus<DevicePropertyRecord,IDevicePropertyRecordService> {
    @Autowired
    private DevicePropertyRecordService devicePropertyRecordService;

    @ApiOperation("运行情况")
    @PostMapping("/getRunCondition")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.THIRD,RoleConstant.AUTH})
    public ResultVO getRunCondition(@RequestBody DevicePropertyRecordVo devicePropertyRecordVo) {
        try {
            Page<DevicePropertyRecordVo> page = new Page<>(devicePropertyRecordVo.getPageNum(),devicePropertyRecordVo.getPageSize());
            List<DevicePropertyRecordVo> list = devicePropertyRecordService.getRunCondition(page,devicePropertyRecordVo);
            Map<String,Object> map = new HashMap<>(10);
            map.put("list",list);
            map.put("total",page.getTotal());
            return new ResultVO(ResultCode.SUCCESS,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }
}

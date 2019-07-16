package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.base.BaseController;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.device.dto.DeviceMethodRecordDTO;
import com.xywg.iot.modular.device.model.DeviceMethodRecord;
import com.xywg.iot.modular.device.service.IDeviceMethodRecordService;
import com.xywg.iot.vo.ResultVO;
import com.xywg.log.annotations.OpenLog;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:43 2019/3/19
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/business/deviceMethodRecord")
public class DeviceMethodRecordController extends BaseController<DeviceMethodRecord,IDeviceMethodRecordService> {


    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMethodRecordController.class);

    @ApiOperation("运行情况")
    @PostMapping("/getRunCondition")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.THIRD,RoleConstant.AUTH})
    public ResultVO getRunCondition(@RequestBody DeviceMethodRecordDTO dto){

        Page<Map<String,Object>> page = new Page(dto.getPageNum(),dto.getPageSize());
        this.service.getRunCondition(page,dto);
        ResultPage<Map<String,Object>> resultPage  = new ResultPage<>(page.getTotal(),page.getRecords());
        return new ResultVO(ResultCode.SUCCESS,resultPage);
    }

}

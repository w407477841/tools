package com.xywg.iot.modular.colud.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.modular.colud.service.DevicePropertyRecordService;
import com.xywg.iot.modular.device.dao.DevicePropertyRecordMapper;
import com.xywg.iot.modular.device.vo.DevicePropertyRecordVo;
import com.xywg.iot.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2019/1/8
 *TIME:21:07
 */
@Service
public class MyDevicePropertyRecordServiceImpl implements DevicePropertyRecordService {
    @Autowired
    private DevicePropertyRecordMapper devicePropertyRecordMapper;
    @Override
    public List<DevicePropertyRecordVo> getRunCondition(Page<DevicePropertyRecordVo> page,DevicePropertyRecordVo devicePropertyRecordVo) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("id",devicePropertyRecordVo.getId());
        map.put("beginDate",devicePropertyRecordVo.getBeginDate());
        map.put("endDate",devicePropertyRecordVo.getEndDate());
        if(!UserUtil.ISADMIN.get()) {
            map.put("createUser",UserUtil.USERID.get());
        }
        return devicePropertyRecordMapper.getRunCondition(page,map);
    }
}

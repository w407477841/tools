package com.xywg.iot.modular.device.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.modular.device.dto.DeviceMethodRecordDTO;
import com.xywg.iot.modular.device.model.DeviceMethodRecord;
import com.xywg.iot.modular.device.dao.DeviceMethodRecordMapper;
import com.xywg.iot.modular.device.service.IDeviceMethodRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modular.device.vo.DevicePropertyRecordVo;
import com.xywg.iot.util.UserUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
@Service
public class DeviceMethodRecordServiceImpl extends ServiceImpl<DeviceMethodRecordMapper, DeviceMethodRecord> implements IDeviceMethodRecordService {

    @Override
    public void getRunCondition(Page<Map<String,Object>> page, DeviceMethodRecordDTO dto) {
        Wrapper<DeviceMethodRecord> wrapper =  new EntityWrapper<>();
        wrapper.setSqlSelect(" call_time as callTime",
                "status",
                "receive_time as receiveTime",
                "(select device_no from t_device_info where t_device_info.id=t_device_method_record.device_id) as deviceNo",
                "in_params as inParams",
                "out_params as outParams");
        wrapper.eq("device_id",dto.getId());
        wrapper.lt(null!=dto.getEndDate(),"call_time",DateUtil.format(dto.getEndDate(),"yyyy-MM-dd HH:mm:ss"));
        wrapper.gt(null!=dto.getBeginDate(),"call_time",DateUtil.format(dto.getBeginDate(),"yyyy-MM-dd HH:mm:ss"));
        if(!UserUtil.ISADMIN.get()) {
            wrapper.eq("(select create_user from t_device_info where t_device_info.id=t_device_method_record.device_id)", UserUtil.USERID.get());
        }
        wrapper.orderBy("call_time",false);
        this.selectMapsPage(page,wrapper);
    }
}

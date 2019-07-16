package com.xywg.iot.modular.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.dao.DeviceErrorLogMapper;
import com.xywg.iot.modular.device.model.DeviceErrorLog;
import com.xywg.iot.modular.device.service.IDeviceErrorLogService;
import com.xywg.iot.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z
 * @since 2019-03-01
 */
@Service
public class DeviceErrorLogServiceImpl extends ServiceImpl<DeviceErrorLogMapper, DeviceErrorLog> implements IDeviceErrorLogService {


    @Override
    public ResultPage<DeviceErrorLog> selectErrorLogPageList(QueryWhereDto<DeviceErrorLog> dto) {
        Page<DeviceErrorLog> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<DeviceErrorLog> ew = new EntityWrapper<>();
        ew.eq("is_del",0);
        ew.orderBy("create_time",false);
        if(StringUtils.isNotBlank(dto.getKeyWord())){
            // 需要  where is_del  = 0  and ( device_no like '%%' or product_type like '%%' )
            ew.andNew().like("device_no",dto.getKeyWord()).or().like("product_type",dto.getKeyWord());
        }

        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.andNew();
            ew.eq("create_user", UserUtil.USERID.get());
        }

        List<DeviceErrorLog> list = baseMapper.selectPage(page, ew);
        return new ResultPage<>(page.getTotal(), list);
    }
}

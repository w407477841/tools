package com.xywg.iot.modular.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.dao.DeviceConfigFileMapper;
import com.xywg.iot.modular.device.model.DeviceConfigFile;
import com.xywg.iot.modular.device.service.IDeviceConfigFileService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.upgrade.dao.DeviceUpgradePackageMapper;
import com.xywg.iot.util.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z
 * @since 2019-03-27
 */
@Service
public class DeviceConfigFileServiceImpl extends ServiceImpl<DeviceConfigFileMapper, DeviceConfigFile> implements IDeviceConfigFileService {
    @Autowired
    private DeviceUpgradePackageMapper deviceUpgradePackageMapper;


    @Override
    public ResultPage<DeviceInfoVo> selectDevicePageList(QueryWhereDto<DeviceConfigFile> dto) {
        Page<DeviceInfoVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<QueryWhereDto> ew = new EntityWrapper<>();
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("a.create_user", UserUtil.USERID.get());
        }
        ew.eq("a.product_id", dto.getBody().getProductId());
        ew.ne("a.status", 1);
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            ew.where("(a.name like '%"+dto.getKeyWord()+"%' or a.device_no like '%"+ dto.getKeyWord()+ "%')");
        }

        List<DeviceInfoVo> list = deviceUpgradePackageMapper.selectDevicePageList(page, ew);
        return new ResultPage<>(page.getTotal(), list);
    }
}

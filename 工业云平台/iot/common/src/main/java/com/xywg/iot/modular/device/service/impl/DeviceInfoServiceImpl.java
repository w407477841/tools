package com.xywg.iot.modular.device.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.dao.DeviceInfoMapper;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modular.device.vo.DeviceAddrVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jxj
 * @since 2019-01-04
 */
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements IDeviceInfoService {
    @Override
    @OpenCache(exp = 500, model = "device", clazz = DeviceInfo.class)
    public DeviceInfo selectCacheOne(String key) {
        //查询设备
        String pk = key.split(":")[0];
        String dn = key.split(":")[1];
        Wrapper<DeviceInfo> deviceWrapper = new EntityWrapper<>();
        deviceWrapper.where("product_id = (select id from t_product_info where product_key = {0})", pk);
        deviceWrapper.eq("device_no", dn);
        return this.selectOne(deviceWrapper);
    }

    @Override
    @RemoveCache(model = "device")
    public void removeCaches(String key) {

    }

    /**
     * 查询本人的设备所属地区的数量的前五
     * 本人没有设备
     * 本人有设备所属地区只有1-5个
     * 本人有设备所属地区大于5个
     *
     * @param user
     * @return
     */
    @Override
    public List<DeviceAddrVO> devceAddrTop5(String user) {



        List<DeviceAddrVO> list = baseMapper.deviceAddr(user);
        List<DeviceAddrVO> result = new ArrayList<>();
        int count  = 0 ;
        if (list != null && list.size() > 0) {
            int top = list.size();

                for (int i = 0; i < top ; i++) {
                    DeviceAddrVO deviceAddrVO = list.get(i);
                    if (StrUtil.isNotBlank(deviceAddrVO.getName())) {
                        result.add(list.get(i));
                    } else {
                        count += deviceAddrVO.getAccount();
                    }
                }

        }
        DeviceAddrVO vo = new DeviceAddrVO();
        vo.setAccount(count);
        vo.setName("其他地区");
        result.add(vo);
        return result;
    }
}

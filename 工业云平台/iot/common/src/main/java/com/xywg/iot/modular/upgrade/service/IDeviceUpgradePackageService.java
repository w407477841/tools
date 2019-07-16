package com.xywg.iot.modular.upgrade.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeSelectVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
public interface IDeviceUpgradePackageService extends IService<DeviceUpgradePackage> {

    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    ResultPage<DeviceUpgradePackage> selectPageList(QueryWhereDto<DeviceUpgradePackage> dto);

    /**
     * 获取设备列表
     * @param dto
     * @return
     */
    ResultPage<DeviceInfoVo> selectDevicePageList(QueryWhereDto<DeviceUpgradeSelectVo> dto);

    /**
     * @return
     */
    List<ProductInfo> getProductList();

    /**
     * 固件升级 以及下发指令
     * @param dto
     * @return
     *//*
    void upgradeHandle(@RequestBody QueryWhereDto<DeviceUpgradePackage> dto);*/

    /**
     * 固件升级 以及下发指令
     * @param dup
     * @return
     */
     void upgradeHandle(DeviceUpgradePackage dup) ;

}

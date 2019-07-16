package com.xywg.iot.modular.upgrade.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
public interface DeviceUpgradePackageMapper extends BaseMapper<DeviceUpgradePackage> {

  List<DeviceUpgradePackage> selectPageList(RowBounds rowBounds, @Param("ew") Wrapper<QueryWhereDto> wrapper);

  List<ProductInfo> getProductList(  @Param("ew") Wrapper<QueryWhereDto> wrapper);

  List<DeviceInfoVo> selectDevicePageList(RowBounds rowBounds, @Param("ew") Wrapper<QueryWhereDto> wrapper);

  /**
   * 从升级履历中查询 设备信息
   * @param rowBounds
   * @param wrapper
   * @return
   */
  List<DeviceInfoVo> selectDevicePageListFromUpgradeRecord(RowBounds rowBounds, @Param("ew") Wrapper<QueryWhereDto> wrapper);
}

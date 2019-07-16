package com.xywg.iot.modular.upgrade.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.upgrade.dao.DeviceUpgradePackageMapper;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeSelectVo;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradePackageService;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradeRecordService;
import com.xywg.iot.util.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
@Service
public class DeviceUpgradePackageServiceImpl extends ServiceImpl<DeviceUpgradePackageMapper, DeviceUpgradePackage> implements IDeviceUpgradePackageService {
    @Value("${netty.iotNettyUrl:#{null}}")
    private String iotNettyUrl;
    @Value("${netty.iotNettyWebPort:#{null}}")
    private String iotNettyWebPort;
    @Autowired
    private IDeviceUpgradeRecordService deviceUpgradeRecordService;
    @Autowired
    private IDeviceInfoService deviceInfoService;


    /**
     * 升级包列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResultPage<DeviceUpgradePackage> selectPageList(QueryWhereDto<DeviceUpgradePackage> dto) {
        Page<DeviceUpgradePackage> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<QueryWhereDto> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        if (StringUtils.isNotBlank(dto.getBody().getName())) {
            ew.like("a.name", dto.getBody().getName());
        }
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("a.create_user", UserUtil.USERID.get());
        }
        List<DeviceUpgradePackage> list = baseMapper.selectPageList(page, ew);
        return new ResultPage<>(page.getTotal(), list);
    }

    /**
     * 设备列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResultPage<DeviceInfoVo> selectDevicePageList(QueryWhereDto<DeviceUpgradeSelectVo> dto) {
        Page<DeviceUpgradePackage> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<QueryWhereDto> ew = new EntityWrapper<>();
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("a.create_user", UserUtil.USERID.get());
        }

        ew.eq("a.product_id", dto.getBody().getProductId());
        ew.ne("a.status",1);
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            ew.like("a.name", dto.getKeyWord());
        }
        //只要版本不同都是可升级
        List<DeviceInfoVo> list;
        if (dto.getBody().getStatus() == 0) {
            ew.where("(a.version !='"+dto.getBody().getProductVersion()+"' || a.version is null)");
            list = baseMapper.selectDevicePageList(page, ew);
        } else {
            ew.eq("c.package_id", dto.getBody().getId());
            //升级中状态的包含  已下发但是还没有返回结果的和未下发的
            if (dto.getBody().getStatus() == 1) {
                ew.where("(c.issued_flag= 0 || c.issued_flag =1)");
            } else {
                ew.eq("c.issued_flag", dto.getBody().getStatus());
            }
            list = baseMapper.selectDevicePageListFromUpgradeRecord(page, ew);
        }

        return new ResultPage<>(page.getTotal(), list);
    }

    @Override
    public List<ProductInfo> getProductList() {
        EntityWrapper<QueryWhereDto> ew = new EntityWrapper<>();
        ew.eq("a.is_del", 0);
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("a.create_user", UserUtil.USERID.get());
        }
        return baseMapper.getProductList(ew);
    }

  /*  *//**
     * 升级操作处理
     *
     * @param dto
     *//*
    @Override
    *//*@Transactional(rollbackFor = Exception.class)*//*
    public void upgradeHandle(QueryWhereDto<DeviceUpgradePackage> dto) {
        if (dto.getBody().getDeviceList() == null || dto.getBody().getDeviceList().size() == 0) {
            return;
        }
        List<DeviceUpgradeRecord> upgradeRecordList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (DeviceInfoVo device : dto.getBody().getDeviceList()) {
            ids.add(device.getId());
            //升级履历
            DeviceUpgradeRecord deviceUpgradeRecord = new DeviceUpgradeRecord();
            deviceUpgradeRecord.setDeviceId(device.getId());
            deviceUpgradeRecord.setProductId(dto.getBody().getProductId());
            deviceUpgradeRecord.setPackageId(dto.getBody().getId());
            //下发标识:0未下发,1已下发,2.操作结果失败,3操作结果成功
            deviceUpgradeRecord.setIssuedFlag(0);
            deviceUpgradeRecord.setIsDel(0);
            deviceUpgradeRecord.setCreateTime(new Date());
            deviceUpgradeRecord.setCreateUser(dto.getBody().getCreateUser());
            deviceUpgradeRecord.setCreateUserName(dto.getBody().getCreateUserName());
            upgradeRecordList.add(deviceUpgradeRecord);
        }
        //添加到升级履历表
        deviceUpgradeRecordService.insertBatch(upgradeRecordList);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setModifyTime(new Date());
        deviceInfo.setVersion(dto.getBody().getVersion());
        deviceInfo.setModifyUser(dto.getBody().getCreateUser());
        deviceInfo.setModifyUserName(dto.getBody().getCreateUserName());
        EntityWrapper<DeviceInfo> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.in("id", ids);
        //批量更新设备升级状态
        deviceInfoService.update(deviceInfo, ew);
        *//*HttpUtils.sendPost("http://" + iotNettyUrl + ":" + iotNettyWebPort + "/internalCommunication/upgrade", JSONUtil.toJsonStr(dto.getBody()));*//*

        }*/


    /**
     * 升级操作处理
     *
     * @param dup
     */
    @Override
    /*@Transactional(rollbackFor = Exception.class)*/
    public void upgradeHandle(DeviceUpgradePackage dup) {
        if (dup.getDeviceList() == null || dup.getDeviceList().size() == 0) {
            return;
        }
        List<DeviceUpgradeRecord> upgradeRecordList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (DeviceInfoVo device : dup.getDeviceList()) {
            ids.add(device.getId());
            //升级履历
            DeviceUpgradeRecord deviceUpgradeRecord = new DeviceUpgradeRecord();
            deviceUpgradeRecord.setDeviceId(device.getId());
            deviceUpgradeRecord.setProductId(dup.getProductId());
            deviceUpgradeRecord.setPackageId(dup.getId());
            //下发标识:0未下发,1已下发,2.操作结果失败,3操作结果成功
            deviceUpgradeRecord.setIssuedFlag(0);
            deviceUpgradeRecord.setIsDel(0);
            deviceUpgradeRecord.setCreateTime(new Date());
            deviceUpgradeRecord.setCreateUser(dup.getCreateUser());
            deviceUpgradeRecord.setCreateUserName(dup.getCreateUserName());
            upgradeRecordList.add(deviceUpgradeRecord);
        }
        //添加到升级履历表
        deviceUpgradeRecordService.insertBatch(upgradeRecordList);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setModifyTime(new Date());
        deviceInfo.setVersion(dup.getVersion());
        deviceInfo.setModifyUser(dup.getCreateUser());
        deviceInfo.setModifyUserName(dup.getCreateUserName());
        EntityWrapper<DeviceInfo> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.in("id", ids);
        //批量更新设备升级状态
        deviceInfoService.update(deviceInfo, ew);
    }
}

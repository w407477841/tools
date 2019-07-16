package com.xywg.iot.modular.colud.controller.upgrade;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeSelectVo;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradePackageService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
@RestController
@RequestMapping("/business/upgrade")
public class DeviceUpgradePackageController {
    @Autowired
    private IDeviceUpgradePackageService deviceUpgradePackageService;

    @PostMapping("selectPageList")
    @OpenLog
    public ResultPage<DeviceUpgradePackage> selectList(@RequestBody QueryWhereDto<DeviceUpgradePackage> dto) {
        return deviceUpgradePackageService.selectPageList(dto);
    }


    @PostMapping("getProductList")
    @OpenLog
    public ResultVO getProductList() {
        return new ResultVO(ResultCode.SUCCESS, deviceUpgradePackageService.getProductList());
    }


    @PostMapping("insert")
    @OpenLog
    public ResultVO insert(@RequestBody DeviceUpgradePackage dto) {
        EntityWrapper<DeviceUpgradePackage> ew = new EntityWrapper<>();
        ew.eq("product_id", dto.getProductId());
        ew.eq("name", dto.getName());
        List<DeviceUpgradePackage> list = deviceUpgradePackageService.selectList(ew);
        if (list.size() > 0) {
            return new ResultVO("新增失败,该名称已存在", 400, null);
        }
        dto.setIsDel(0);
        dto.setCreateTime(new Date());
        return new ResultVO(ResultCode.SUCCESS, deviceUpgradePackageService.insert(dto));
    }

    @PostMapping("updateById")
    @OpenLog
    public ResultVO updateById(@RequestBody DeviceUpgradePackage dto) {
        EntityWrapper<DeviceUpgradePackage> ew = new EntityWrapper<>();
        ew.eq("product_id", dto.getProductId());
        ew.eq("name", dto.getName());
        ew.ne("id", dto.getId());
        List<DeviceUpgradePackage> list = deviceUpgradePackageService.selectList(ew);
        if (list.size() > 0) {
            return new ResultVO("更新失败,该名称已存在", 400, null);
        }
        dto.setModifyTime(new Date());
        return new ResultVO(ResultCode.SUCCESS, deviceUpgradePackageService.updateById(dto));
    }

    @GetMapping("getById")
    @OpenLog
    public ResultVO getById(@RequestParam Integer id) {
        return new ResultVO(ResultCode.SUCCESS, deviceUpgradePackageService.selectById(id));
    }

    @PostMapping("deletes")
    @OpenLog
    public ResultVO deletes(@RequestBody QueryWhereDto<Object> dto) {
        return new ResultVO(ResultCode.SUCCESS, deviceUpgradePackageService.deleteBatchIds(dto.getIds()));
    }


    @PostMapping("selectDevicePageList")
    @OpenLog
    public ResultPage<DeviceInfoVo> selectDevicePageList(@RequestBody QueryWhereDto<DeviceUpgradeSelectVo> dto) {
        return deviceUpgradePackageService.selectDevicePageList(dto);
    }


   /* *//**
     * 固件升级操作
     *
     * @param dto
     * @return
     *//*
    @PostMapping("upgradeHandle")
    @OpenLog
    public ResultVO upgradeHandle(@RequestBody QueryWhereDto<DeviceUpgradePackage> dto) {
        deviceUpgradePackageService.upgradeHandle(dto);
        return new ResultVO(ResultCode.SUCCESS, null);
    }*/

}


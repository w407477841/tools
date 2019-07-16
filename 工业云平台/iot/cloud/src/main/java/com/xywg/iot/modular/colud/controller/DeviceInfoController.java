package com.xywg.iot.modular.colud.controller;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryDTO;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.dto.SelectByIdDTO;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.service.IProductInfoService;
import com.xywg.iot.util.UserUtil;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.DeleteDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.DeviceInfoService;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jxj
 * @since 2019-01-04
 */
@RestController
@RequestMapping("/business/device")
public class DeviceInfoController extends BaseControllerPlus<DeviceInfo,IDeviceInfoService> {
    public DeviceInfoController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD};
        this.setQueryRoles(roles);
        this.setDeleteRoles(roles);
    }

    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private IProductInfoService productInfoService;

    @ApiOperation("新增")
    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO insert(@RequestBody DeviceInfo deviceInfo) {
        try {
            deviceInfo.setDeviceSecret(RandomUtil.randomString(32));
            deviceInfo.setAddTime(new Date());
            Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("product_id",deviceInfo.getProductId()).eq("device_no",deviceInfo.getDeviceNo()).eq("create_user", UserUtil.USERID.get());
            List<DeviceInfo> list = service.selectList(wrapper);
            //判断重名
            if(list.size() > 0) {
                return new ResultVO(ResultCode.DEVICE_NO_REPEAT,null);
            }
            if(service.insert(deviceInfo)) {
                return new ResultVO(ResultCode.INSERT_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.INSERT_ERROR,null);
    }

    @ApiOperation("编辑")
    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO update(@RequestBody DeviceInfo deviceInfo) {
        try {
            if(deviceInfoService.updateDevice(deviceInfo)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if(ResultCode.DEVICE_NO_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.DEVICE_NO_REPEAT,null);
            }
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }

    @ApiOperation("启用")
    @PostMapping("/setUse")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO setUse(@RequestBody DeviceInfoVo deviceInfoVo) {
        try {
            if(service.updateBatchById(deviceInfoVo.getDeviceInfos())) {
                List<DeviceInfo> deviceInfoList = deviceInfoVo.getDeviceInfos();
                List<Integer> ids = new ArrayList<>(10);
                for(int i = 0;i < deviceInfoList.size();i++) {
                    ids.add(deviceInfoList.get(i).getId());
                }
                Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
                wrapper.in("id",ids);
                List<DeviceInfo> deviceInfos = service.selectList(wrapper);
                String key = "";
                //循环获取productKey
                for(int i = 0;i < deviceInfos.size();i++) {
                    if(i > 0) {
                        key += ",";
                    }
                    ProductInfo productInfo = productInfoService.selectById(deviceInfos.get(i).getProductId());
                    key += productInfo.getProductKey() + ":" + deviceInfos.get(i).getDeviceNo();
                }
                //删除redis缓存
                service.removeCaches(key);
                return new ResultVO(ResultCode.USE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.USE_ERROR,null);
    }

    @ApiOperation("禁用")
    @PostMapping("/setDis")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO setDis(@RequestBody DeviceInfoVo deviceInfoVo) {
        try {
            if(service.updateBatchById(deviceInfoVo.getDeviceInfos())) {
                List<DeviceInfo> deviceInfoList = deviceInfoVo.getDeviceInfos();
                List<Integer> ids = new ArrayList<>(10);
                for(int i = 0;i < deviceInfoList.size();i++) {
                    ids.add(deviceInfoList.get(i).getId());
                }
                Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
                wrapper.in("id",ids);
                List<DeviceInfo> deviceInfos = service.selectList(wrapper);
                String key = "";
                //循环获取productKey
                for(int i = 0;i < deviceInfos.size();i++) {
                    if(i > 0) {
                        key += ",";
                    }
                    ProductInfo productInfo = productInfoService.selectById(deviceInfos.get(i).getProductId());
                    key += productInfo.getProductKey() + ":" + deviceInfos.get(i).getDeviceNo();
                }
                //删除redis缓存
                service.removeCaches(key);
                return new ResultVO(ResultCode.DIS_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.DIS_ERROR,null);
    }

    @ApiOperation("激活")
    @PostMapping("/setActive")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO setActive(@RequestBody DeviceInfoVo deviceInfoVo) {
        try {
            List<DeviceInfo> list = deviceInfoVo.getDeviceInfos();
            for(int i = 0;i < list.size();i++) {
                list.get(i).setActivationTime(new Date());
            }
            if(service.updateBatchById(deviceInfoVo.getDeviceInfos())) {
                List<DeviceInfo> deviceInfoList = deviceInfoVo.getDeviceInfos();
                List<Integer> ids = new ArrayList<>(10);
                for(int i = 0;i < deviceInfoList.size();i++) {
                    ids.add(deviceInfoList.get(i).getId());
                }
                Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
                wrapper.in("id",ids);
                List<DeviceInfo> deviceInfos = service.selectList(wrapper);
                String key = "";
                //循环获取productKey
                for(int i = 0;i < deviceInfos.size();i++) {
                    if(i > 0) {
                        key += ",";
                    }
                    ProductInfo productInfo = productInfoService.selectById(deviceInfos.get(i).getProductId());
                    key += productInfo.getProductKey() + ":" + deviceInfos.get(i).getDeviceNo();
                }
                //删除redis缓存
                service.removeCaches(key);
                return new ResultVO(ResultCode.ACTIVE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.ACTIVE_ERROR,null);
    }

    @ApiOperation("批量导入")
    @PostMapping("/inputBatch")
  //  @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO inputBatch(@RequestParam Integer productId, @RequestParam MultipartFile file) {
        try {
            if(deviceInfoService.inputBatch(productId,file)) {
                return new ResultVO(ResultCode.INPUT_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if((e.getMessage().split(",")).length > 0) {
                return new ResultVO(e.getMessage(),500,null);
            }
            if(ResultCode.DEVICE_ALREADY_EXIST.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.DEVICE_ALREADY_EXIST,null);
            }
            if(ResultCode.EXCEL_DEVICE_NO_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.EXCEL_DEVICE_NO_REPEAT,null);
            }
        }
        return new ResultVO(ResultCode.INPUT_ERROR,null);
    }

    @ApiOperation("获取列表")
    @PostMapping("/selectDevice")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectDevice(@RequestBody QueryPageDTO queryPageDTO) {
        try {
            Page<DeviceInfoVo> page = new Page<>(queryPageDTO.getPageNum(),queryPageDTO.getPageSize());
            List<DeviceInfoVo> list = deviceInfoService.selectDevice(page,queryPageDTO);
            Map<String,Object> map = new HashMap<>(10);
            map.put("list",list);
            map.put("total",page.getTotal());
            return new ResultVO(ResultCode.SUCCESS,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }

    @ApiOperation("删除")
    @PostMapping("/deleteDevice")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO deleteDevice(@RequestBody DeleteDTO deleteDTO) {
        try {
            if(deviceInfoService.deleteDevice(deleteDTO.getIds())) {
                return new ResultVO(ResultCode.DELETE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.DELETE_ERROR,null);
    }

    @ApiOperation("单条查询")
    @PostMapping("/selectDeviceById")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectDeviceById(@RequestBody SelectByIdDTO selectByIdDTO) {
        try {
            return new ResultVO(ResultCode.SUCCESS,deviceInfoService.selectDeviceById(selectByIdDTO.getId()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }
    @ApiOperation("根据设备id查询 产品信息")
    @PostMapping("/selectMethodInfoByDeviceId")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectMethodInfoByDeviceId(@RequestBody SelectByIdDTO selectByIdDTO){

        try {
            return new ResultVO(ResultCode.SUCCESS,deviceInfoService.selectMethodInfoByDeviceId(selectByIdDTO.getId()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);

    }
    @ApiOperation("根据设备id查询 产品信息")
    @PostMapping("/selectParamsInfoByMethodId")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectParamsInfoByMethodId(@RequestBody SelectByIdDTO selectByIdDTO){

        try {
            return new ResultVO(ResultCode.SUCCESS,deviceInfoService.selectParamInfoByMethodId(selectByIdDTO.getId()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }


}


package com.xywg.iot.modular.colud.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.DeviceInfoService;
import com.xywg.iot.modular.device.dao.DeviceInfoMapper;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.model.ProductMethod;
import com.xywg.iot.modular.product.service.IProductInfoService;
import com.xywg.iot.modular.product.service.IProductMethodParamService;
import com.xywg.iot.modular.product.service.IProductMethodService;
import com.xywg.iot.modular.product.vo.MethodInfoVO;
import com.xywg.iot.modular.product.vo.ParamInfoVO;
import com.xywg.iot.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/***
 *@author:jixiaojun
 *DATE:2019/1/4
 *TIME:10:12
 */
@Service
public class MyDeviceInfoServiceImpl implements DeviceInfoService {
    @Autowired
    private IDeviceInfoService service;
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;
    @Autowired
    private IProductMethodService  productMethodService;
    @Autowired

    private IProductMethodParamService  productMethodParamService;


    private String deviceNo = "设备编号";

    private String deviceName = "设备名称";

    private String comments = "备注";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean inputBatch(Integer productId, MultipartFile file) throws Exception {
        //从字节流中解析出excelReader
        ExcelReader excelReader = ExcelUtil.getReader(file.getInputStream());
        //得到原始数据
        List<Map<String,Object>> originalMap = excelReader.readAll();
        List<DeviceInfo> deviceInfos = new ArrayList<>(10);
        Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
        //查询出当前所有的设备列表
        List<DeviceInfo> list = service.selectList(wrapper);
        //循环把原始数据转换成实体类
        for(int i = 0;i < originalMap.size();i++) {
            //获取每一条原始数据的keySet
            List<String> keys = new ArrayList<>();
            keys.addAll(originalMap.get(i).keySet());
            DeviceInfo deviceInfo = new DeviceInfo();
            //循环把每一条keySet对应的值赋值给实体类
            for(int j = 0;j < keys.size();j++) {
                deviceInfo.setStatus(1);
                deviceInfo.setProductId(productId);
                deviceInfo.setAddTime(new Date());
                deviceInfo.setDeviceSecret(RandomUtil.randomString(32));
                if(deviceNo.equals(keys.get(j))) {
                    Object deviceNo = originalMap.get(i).get(keys.get(j)) ;
                    if(deviceNo!=null){
                        String dn = deviceNo.toString();
                        if(!"".equals(dn)){
                            deviceInfo.setDeviceNo(dn);
                        }else{
                            throw  new RuntimeException("设备号不能为空");
                        }
                    }

                }
                if(deviceName.equals(keys.get(j))) {
                    Object name =  originalMap.get(i).get(keys.get(j));
                    if(name !=null){
                    deviceInfo.setName(name.toString());
                    }
                }
                if(comments.equals(keys.get(j))) {
                    Object comment = originalMap.get(i).get(keys.get(j));
                    if(comment!= null){
                        deviceInfo.setComments(comment.toString());
                    }

                }
            }
            deviceInfos.add(deviceInfo);
        }
        String existDevice = "";
        //循环判断导入的设备名称是否重复
        for(int i = 0;i < list.size();i++) {
            for(int j = 0;j < deviceInfos.size();j++) {
                if(deviceInfos.get(j).getProductId().equals(list.get(i).getProductId()) && deviceInfos.get(j).getDeviceNo().equals(list.get(i).getDeviceNo()) && UserUtil.USERID.get().equals(list.get(i).getCreateUser())) {
                    existDevice += "第" + (j + 2) + "行,设备号为:" + deviceInfos.get(j).getDeviceNo() + "的设备已存在<br>";
                }
            }
        }
        if(existDevice.length() > 0) {
            throw new RuntimeException(existDevice);
        }
        for(int i = 0;i < deviceInfos.size();i++) {
            for(int j = i+1;j < deviceInfos.size();j++) {
                if(deviceInfos.get(i).getDeviceNo().equals(deviceInfos.get(j).getDeviceNo())) {
                    throw new RuntimeException(ResultCode.EXCEL_DEVICE_NO_REPEAT.getMsg());
                }
            }
        }
        return service.insertBatch(deviceInfos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDevice(DeviceInfo deviceInfo) throws Exception {
        Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("product_id",deviceInfo.getProductId()).eq("device_no",deviceInfo.getDeviceNo()).ne("id",deviceInfo.getId()).eq("create_user",UserUtil.USERID.get());
        List<DeviceInfo> list = service.selectList(wrapper);
        //判断重名
        if(list.size() > 0) {
            throw new RuntimeException(ResultCode.DEVICE_NO_REPEAT.getMsg());
        }
        boolean isSuccess = service.updateById(deviceInfo);
        ProductInfo productInfo = productInfoService.selectById(deviceInfo.getProductId());
        //删除redis缓存
        service.removeCaches(productInfo.getProductKey() + ":" + deviceInfo.getDeviceNo());
        return isSuccess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDevice(List<Integer> ids) throws Exception {
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
        boolean isSuccess = service.deleteBatchIds(ids);
        return isSuccess;
    }

    @Override
    public List<DeviceInfoVo> selectDevice(Page<DeviceInfoVo> page,QueryPageDTO queryPageDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("deviceNo",queryPageDTO.getParams().get(0).getValue());
        if(!UserUtil.ISADMIN.get()) {
            map.put("createUser", UserUtil.USERID.get());
        }
        return deviceInfoMapper.selectDevice(page,map);
    }

    @Override
    public DeviceInfo selectDeviceById(Integer id) throws Exception {
        return deviceInfoMapper.selectDeviceById(id);
    }

    @Override
    public MethodInfoVO selectMethodInfoByDeviceId(Integer id) throws Exception {
        MethodInfoVO methodInfoVO = new MethodInfoVO();
        //查下 设备
        DeviceInfo deviceInfo = deviceInfoMapper.selectDeviceById(id);
        //查下 产品
        ProductInfo productInfo =  productInfoService.selectById(deviceInfo.getProductId());

        //查 方法
        Wrapper<ProductMethod> methodWrapper =new EntityWrapper<>();
        methodWrapper.eq("product_id",productInfo.getId());
        List<ProductMethod> methods =  productMethodService.selectList(methodWrapper);
        //塞就完事了
        methodInfoVO.setPk(productInfo.getProductKey());
        methodInfoVO.setName(productInfo.getName());
        methodInfoVO.setSn(deviceInfo.getDeviceNo());
        methodInfoVO.setProductMethods(methods);
        return methodInfoVO;
    }

    @Override
    public List<ParamInfoVO> selectParamInfoByMethodId(Integer id) throws Exception {
        List<ParamInfoVO> paramInfoVOS = new ArrayList<>();
        Wrapper wrapper  =new EntityWrapper();
        wrapper.setSqlSelect("`key`","`name`","data_type as type"," (select `name` from  t_master_measure_unit where t_master_measure_unit.id = t_product_method_param.unit) as unit ");
        wrapper.eq("param_type",1);
        wrapper.eq("is_del",0);
        wrapper.eq("method_id",id);
        List<Map<String,Object>> list = productMethodParamService.selectMaps(wrapper);
        list.forEach(item->{
            paramInfoVOS.add(JSONUtil.toBean(JSONUtil.toJsonStr(item),ParamInfoVO.class));
        });
        return paramInfoVOS;
    }
}

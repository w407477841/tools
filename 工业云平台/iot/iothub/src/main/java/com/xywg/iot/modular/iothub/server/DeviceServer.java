package com.xywg.iot.modular.iothub.server;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.dto.DataDTO;
import com.xywg.iot.enums.DeviceStatus;
import com.xywg.iot.enums.ExceptionType;
import com.xywg.iot.enums.MethodStatus;
import com.xywg.iot.enums.PropertyType;
import com.xywg.iot.exceptions.ExpressionException;
import com.xywg.iot.exceptions.MissingParamException;
import com.xywg.iot.exceptions.NotBooleanException;
import com.xywg.iot.modular.device.bean.Addr;
import com.xywg.iot.modular.device.model.*;
import com.xywg.iot.modular.device.service.*;
import com.xywg.iot.modular.iothub.netty.config.properties.BaiduProperty;
import com.xywg.iot.modular.iothub.netty.handler.IotHubHandler;
import com.xywg.iot.modular.product.model.*;
import com.xywg.iot.modular.product.service.*;
import com.xywg.iot.node.properties.XyNodeProperties;
import com.xywg.iot.node.service.XyNodeService;
import com.xywg.iot.util.ExpressionUtil;
import com.xywg.iot.util.NettyUtil;
import com.xywg.iot.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:02 2018/12/14
 * Modified By : wangyifei
 */
@Component
public class DeviceServer {
    /**
     * 方法执行超时时间
     */
    private static final long METHOD_ACCESS_TIMEOUT = 6000L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServer.class);


    /**
     * 产品
     */
    private IProductInfoService productService;
    /**
     * 设备
     */
    private IDeviceInfoService deviceService;
    /**
     * 属性
     */
    private IProductPropertyService productPropertyService;
    /**
     * 属性明细
     */
    private IDevicePropertyRecordDetailService propertyDetailService;
    /**
     * 事件
     */
    private IProductEventService productEventService;
    /**
     * 事件参数
     */

    private IProductEventParamService productEventParamService;
    /**
     * 事件明细
     */
    private IDeviceEventRecordService eventDetailService;
    /**
     * 事件参数明细
     */
    private IDeviceEventParamRecordService eventParamDetailService;

    /**
     * 方法
     */
    private IProductMethodService productMethodService;
    /**
     * 方法明细
     */
    private IDeviceMethodRecordService methodDetailService;
    /**
     * 方法参数
     */
    private IProductMethodParamService productMethodParamService;
    /**
     * 方法参数明细
     */
    private IDeviceMethodParamRecordService methodParamDetailService;
    /**
     * 采集明细
     */
    private IDevicePropertyRecordService collectDetailService;

    private NettyUtil  nettyUtil;

    /**
     * 百度参数
     */
    private BaiduProperty baiduProperty;


    private RedisUtil redisUtil;

    /**
     *
     * 错误信息
     */
    private ExceptionService  exceptionService;

    private XyNodeService xyNodeService;

    private XyNodeProperties xyNodeProperties;
    @Autowired
    public DeviceServer(IProductInfoService productService, IDeviceInfoService deviceService, IProductPropertyService productPropertyService, IDevicePropertyRecordDetailService propertyDetailService, IProductEventService productEventService, IProductEventParamService productEventParamService, IDeviceEventRecordService eventDetailService, IDeviceEventParamRecordService eventParamDetailService, IProductMethodService productMethodService, IDeviceMethodRecordService methodDetailService, IProductMethodParamService productMethodParamService, IDeviceMethodParamRecordService methodParamDetailService, IDevicePropertyRecordService collectDetailService, NettyUtil nettyUtil, BaiduProperty baiduProperty, RedisUtil redisUtil, ExceptionService exceptionService, XyNodeService xyNodeService, XyNodeProperties xyNodeProperties) {
        this.productService = productService;
        this.deviceService = deviceService;
        this.productPropertyService = productPropertyService;
        this.propertyDetailService = propertyDetailService;
        this.productEventService = productEventService;
        this.productEventParamService = productEventParamService;
        this.eventDetailService = eventDetailService;
        this.eventParamDetailService = eventParamDetailService;
        this.productMethodService = productMethodService;
        this.methodDetailService = methodDetailService;
        this.productMethodParamService = productMethodParamService;
        this.methodParamDetailService = methodParamDetailService;
        this.collectDetailService = collectDetailService;
        this.nettyUtil = nettyUtil;
        this.baiduProperty = baiduProperty;
        this.redisUtil = redisUtil;
        this.exceptionService = exceptionService;
        this.xyNodeService = xyNodeService;
        this.xyNodeProperties = xyNodeProperties;
    }

    /**
     * 修改设备状态
     *
     * @param pk     产品
     * @param ds     设备密钥
     * @param dn     设备号
     * @param status 状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeviceStatus(String pk, String ds, String dn, DeviceStatus status) {
        DeviceInfo device = new DeviceInfo();
        if(DeviceStatus.ONLINE.getStatus().equals(status.getStatus())){
            //上线 添加最后上线时间
            device.setLastTime(new Date());
        }
        device.setStatus(status.getStatus());
        Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
        wrapper.where("product_id = (select id from t_product_info where product_key = {0}) ", pk);
        wrapper.eq("device_no", dn);
        wrapper.eq("device_secret", ds);
        String cacheDeviceKey = pk + ":" + dn;
        deviceService.removeCaches(cacheDeviceKey);
        return deviceService.update(device, wrapper);
    }

    /**
     * 下线所有设备
     */
    @Transactional(rollbackFor = Exception.class)
    public void offlines(){
        DeviceInfo device = new DeviceInfo();
        device.setStatus(DeviceStatus.OFFLINE.getStatus());
        Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect("id","device_no as dn","(select product_key from t_product_info where t_product_info.id = t_device_info.product_id) as pk");
        wrapper.eq("status",DeviceStatus.ONLINE.getStatus());
        List<Map<String,Object>> deviceInfos =  deviceService.selectMaps(wrapper);
        List<DeviceInfo> offlines = new LinkedList<>();
        for(Map<String,Object> deviceMap:deviceInfos){
            String localKey = deviceMap.get("pk")+"."+deviceMap.get("dn");
            if(xyNodeProperties.getName().equals(xyNodeService.node(localKey))){
                DeviceInfo deviceInfo =new DeviceInfo();
                deviceInfo.setId((Integer) deviceMap.get("id"));
                deviceInfo.setStatus(DeviceStatus.OFFLINE.getStatus());
                offlines.add(deviceInfo);
            }
        }
        if(!CollectionUtils.isEmpty(offlines)){
            deviceService.updateBatchById(offlines);
        }
        redisUtil.remove("iot:device");

    }

    /**
     * @param pk     产品
     * @param dn     设备号
     * @param params 参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertProperty(String pk, String dn, Map<String, String> params,String data) {
        // 查询产品

        ProductInfo product = productService.selectCacheOne(pk);

        //查询设备

        String cacheDeviceKey = pk + ":" + dn;
        DeviceInfo device = deviceService.selectCacheOne(cacheDeviceKey);

        //查询产品属性
        Wrapper<ProductProperty> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.eq("product_id", product.getId());
        List<ProductProperty> productPropertys = productPropertyService.selectList(propertyWrapper);
        Date now = new Date();
        //新增采集明细
        DevicePropertyRecord collectDetail = new DevicePropertyRecord(now, device.getId());
        StringBuffer dataDetail =new  StringBuffer();
        StringBuffer alarmDetail= new StringBuffer();
        List<DevicePropertyRecordDetail> propertyDetails = new ArrayList<>();
        // 存放 转换后的参数数据
        Map<String, Object> convertorValues = new HashMap<>(10);
        // 存放 表达式
        Map<String, String> expressions = new HashMap<>(10);
        productPropertys.forEach(item -> {
            //组装数据
            String key = item.getKey();
            if (!params.containsKey(key)) {
                String content = "缺少属性<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk,dn,ExceptionType.PROPETY,content,data);
                throw new RuntimeException(content);
            }
            String value = params.get(key);

            String alarmExpression = item.getAlarmExpression();
            int type = item.getDataType();
            Object convertorParam;
            try {
                convertorParam = ExpressionUtil.parseObject(type, value);
            }catch ( Exception e){
                String content = "属性<" + item.getName() + "-" + key + ">类型错误，需要类型<"+PropertyType.getCommons(type) +"> ";
                exceptionService.insertError(pk,dn,ExceptionType.PROPETY,content,data);
                throw new RuntimeException(content);
            }

            convertorValues.put(key, convertorParam);
            expressions.put(key, alarmExpression);
            //明细
            DevicePropertyRecordDetail propertyDetail ;

            try {
                if (ExpressionUtil.test(convertorValues, expressions.get(key), convertorValues.get(key))) {
                    //报警
                    propertyDetail  = new DevicePropertyRecordDetail(item.getId(), value, collectDetail.getId(),1);
                    dataDetail.append(",").append(item.getName()).append(":").append(value);
                    alarmDetail.append(",").append(1);
                } else {
                    //未报警
                    propertyDetail  = new DevicePropertyRecordDetail(item.getId(), value, collectDetail.getId(),0);
                    dataDetail.append(",").append(item.getName()).append(":").append(value);
                    alarmDetail.append(",").append(0);
                }
            } catch (MissingParamException e) {
                String content = "属性<" + item.getName() + "-" + key + ">的报警表达式配置错误，产品中缺少<" + e.getMessage() + ">属性";
                exceptionService.insertError(pk,dn,ExceptionType.PROPETY,content,data);
                throw new RuntimeException(content);
            } catch (ExpressionException e) {
                String content = e.getMessage();
                exceptionService.insertError(pk,dn,ExceptionType.PROPETY,content,data);
                throw new RuntimeException(content);
            } catch (NotBooleanException e) {
                String content = e.getMessage();
                exceptionService.insertError(pk,dn,ExceptionType.PROPETY,content,data);
                throw new RuntimeException(content);
            }


            propertyDetails.add(propertyDetail);
        });
        if(dataDetail.length()>0){
            collectDetail.setDataDetail(dataDetail.substring(1));
            collectDetail.setAlarmDetail(alarmDetail.substring(1));
        }
        collectDetailService.insert(collectDetail);
        if (propertyDetails.size() > 0) {
            propertyDetailService.insertBatch(propertyDetails);
        }

    }

    /**
     * @param pk       产品
     * @param dn       设备号
     * @param eventKey 事件
     * @param params   参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertEvent(String pk, String dn, String eventKey, Map<String, String> params,String data) {

        //查询设备

        String cacheDeviceKey = pk + ":" + dn;
        //查询 缓存
        DeviceInfo device = deviceService.selectCacheOne(cacheDeviceKey);
        if (device == null) {
            throw new RuntimeException("设备不存在<" + dn + "> ");
        }
        //查询事件
        Wrapper<ProductEvent> eventWrapper = new EntityWrapper<>();
        eventWrapper.where("product_id = (select id from t_product_info where product_key = {0})", pk);
        eventWrapper.eq("`key`", eventKey);
        ProductEvent event = productEventService.selectOne(eventWrapper);
        if (event == null) {
            String content = "事件不存在<" + eventKey + "> ";
            exceptionService.insertError(pk,dn,ExceptionType.EVENT,content,data);
            throw new RuntimeException(content);
        }
        //查询事件参数

        Wrapper<ProductEventParam> eventParamWrapper = new EntityWrapper<>();
        eventParamWrapper.eq("event_id", event.getId());
        List<ProductEventParam> eventParams = productEventParamService.selectList(eventParamWrapper);

        // 添加事件明细
        Date now = new Date();
        StringBuffer inParams = new StringBuffer();
        DeviceEventRecord eventDetail = new DeviceEventRecord(event.getId(), now, device.getId());
        eventDetailService.insert(eventDetail);
        // 添加事件参数明细
        List<DeviceEventParamRecord> eventParamDetails = new ArrayList<>();

        eventParams.forEach(item -> {
            String key = item.getKey();
            if (!params.containsKey(key)) {
                String content = "事件<"+eventKey+"> 缺少参数<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk,dn,ExceptionType.EVENT,content,data);
                throw new RuntimeException(content);
            }
            String value = params.get(key);
            inParams.append(",").append(item.getName()).append(":").append(value);
            DeviceEventParamRecord paramDetail = new DeviceEventParamRecord(item.getId(), eventDetail.getId(), value);
            eventParamDetails.add(paramDetail);
        });
        if(inParams.length()>0){
            eventDetail.setInParams(inParams.substring(1));
        }
        eventDetailService.updateById(eventDetail);
        if (eventParamDetails.size() > 0) {
            eventParamDetailService.insertBatch(eventParamDetails);
        }
    }

    /**
     * @param pk        产品
     * @param dn        设备名
     * @param methodKey 方法
     * @param params    参数
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> insertInMethod(String pk, String dn, String methodKey, Map<String, String> params,String data) {
        Map<String, String> result = new HashMap<>(10);

        //查询设备

        String cacheDeviceKey = pk + ":" + dn;
        DeviceInfo device = deviceService.selectCacheOne(cacheDeviceKey);
        if (device == null) {
            throw new RuntimeException("设备不存在<" + dn + "> ");
        }
        //查询方法
        Wrapper<ProductMethod> methodWrapper = new EntityWrapper<>();
        methodWrapper.where("product_id = (select id from t_product_info where product_key = {0})", pk);
        methodWrapper.eq("`key`", methodKey);
        ProductMethod method = productMethodService.selectOne(methodWrapper);
        if (method == null) {
            String content = "方法不存在<" + methodKey + "> ";
            exceptionService.insertError(pk,dn,ExceptionType.METHOD,content,data);
            throw new RuntimeException(content);
        }
        result.put("type", method.getIsSync().toString());

        //查询方法入参
        Wrapper<ProductMethodParam> methodParamWrapper = new EntityWrapper<>();
        methodParamWrapper.eq("method_id", method.getId());
        methodParamWrapper.eq("param_type", 1);
        List<ProductMethodParam> methodParams = productMethodParamService.selectList(methodParamWrapper);

        Date now = new Date();


        StringBuffer inParams = new StringBuffer();

        //添加方法明细
        DeviceMethodRecord methodDetail = new DeviceMethodRecord(method.getId(), now, device.getId());
        methodDetailService.insert(methodDetail);
        result.put("uuid", methodDetail.getId().toString());
        // 添加方法参数明细
        List<DeviceMethodParamRecord> methodParamDetails = new ArrayList<>();
        methodParams.forEach(item -> {
            String key = item.getKey();
            if (!params.containsKey(key)) {
                String content = "方法<"+methodKey+"> 缺少参数<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk,dn,ExceptionType.METHOD,content,data);
                throw new RuntimeException(content);
            }
            inParams.append(",").append(item.getName()).append(":").append(params.get(key));
            DeviceMethodParamRecord methodParamDetail = new DeviceMethodParamRecord(item.getId(), methodDetail.getId(), params.get(key));
            methodParamDetails.add(methodParamDetail);

        });
        if(inParams.length()>0){
            methodDetail.setInParams(inParams.substring(1));
        }
        methodDetailService.updateById(methodDetail);

        if (methodParamDetails.size() > 0) {
            methodParamDetailService.insertBatch(methodParamDetails);
        }

        return result;
    }

    /**
     * @param pk        产品
     * @param dn        设备名
     * @param methodKey 方法
     * @param params    参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertOutMethod(String pk, String dn, String methodKey, String uuid, Map<String, String> params,String data) {
        //查询明细
        DeviceMethodRecord methodDetail = methodDetailService.selectById(Integer.parseInt(uuid));
        if (methodDetail == null) {
            String content = "方法<"+methodKey+"> 未找到记录<" +uuid + "> ";
            exceptionService.insertError(pk,dn,ExceptionType.METHOD,content,data);
            throw new RuntimeException(content);
        }
        if (methodDetail.getStatus() != MethodStatus.NO_RETURN.getStatus()) {
            String content = "方法<"+methodKey+"> 已返回 已返回记录<" +uuid + "> ";
            exceptionService.insertError(pk,dn,ExceptionType.METHOD,content,data);
            throw new RuntimeException(content);
        }
        Date now = new Date();
        methodDetail.setReceiveTime(now);
        long between = DateUtil.between(now, methodDetail.getCallTime(), DateUnit.MS);
        if (between > METHOD_ACCESS_TIMEOUT) {
            // 超过6秒算超时
            methodDetail.setStatus(MethodStatus.TIMEOUT.getStatus());
            LOGGER.info("方法执行超时");
        } else {
            methodDetail.setStatus(MethodStatus.RETURN.getStatus());
            LOGGER.info("方法执行完毕");
        }

        StringBuffer outParams = new StringBuffer();
        //查询设备

        String cacheDeviceKey = pk + ":" + dn;
        DeviceInfo device = deviceService.selectCacheOne(cacheDeviceKey);
        if (device == null) {
            throw new RuntimeException("设备不存在<" + dn + "> ");
        }
        //查询方法
        Wrapper<ProductMethod> methodWrapper = new EntityWrapper<>();
        methodWrapper.where("product_id = (select id from t_product_info where product_key = {0})", pk);
        methodWrapper.eq("`key`", methodKey);
        ProductMethod method = productMethodService.selectOne(methodWrapper);
        if (method == null) {
            String content = "方法不存在<" + methodKey + "> ";
            exceptionService.insertError(pk,dn,ExceptionType.METHOD,content,data);
            throw new RuntimeException(content);
        }


        //查询方法出参
        Wrapper<ProductMethodParam> methodParamWrapper = new EntityWrapper<>();
        methodParamWrapper.eq("method_id", method.getId());
        methodParamWrapper.eq("param_type", 2);
        List<ProductMethodParam> methodParams = productMethodParamService.selectList(methodParamWrapper);

        // 添加方法参数明细
        List<DeviceMethodParamRecord> methodParamDetails = new ArrayList<>();
        methodParams.forEach(item -> {
            String key = item.getKey();
            if (!params.containsKey(key)) {
                String content = "方法<"+methodKey+"> 缺少参数<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk,dn,ExceptionType.METHOD,content,data);
                throw new RuntimeException(content);
            }
            outParams.append(",").append(item.getName()).append(":").append(params.get(key));
            DeviceMethodParamRecord methodParamDetail = new DeviceMethodParamRecord(item.getId(), Integer.parseInt(uuid), params.get(key));
            methodParamDetails.add(methodParamDetail);
        });
        if(outParams.length()>0){
            methodDetail.setOutParams(outParams.substring(1));
        }
        methodDetailService.updateById(methodDetail);
        if (methodParamDetails.size() > 0) {
            methodParamDetailService.insertBatch(methodParamDetails);
        }

    }


    public void callMethod(String pk, String dn, String methodKey, Map<String, String> params,String data) {

        if (!IotHubHandler.isLogin(pk, dn)) {
            throw new RuntimeException("设备未上线");
        }
        //返回 是否同步，方法明细id
        Map<String, String> method = insertInMethod(pk, dn, methodKey, params,data);
        callDevice(pk, dn, methodKey, method.get("uuid"), params);

    }

    private void callDevice(String pk, String dn, String methodKey, String uuid, Map<String, String> params) {

        String localKey = pk + "." + dn;
        Channel channel = IotHubHandler.nettyChannels.get(localKey);
        if (channel == null) {
            throw new RuntimeException("设备未上线");
        }
//        DataDTO dataDTO = new DataDTO();
//        dataDTO.setMethod("method." + methodKey);
//        dataDTO.setUuid(uuid);
//        dataDTO.setParams(params);
//        nettyUtil.send(dataDTO, channel,true);
        // todo 发送设备数据

    }

    /**
     * @param pk
     * @param ds
     * @param dn
     * @param ctx
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceAddr(String pk, String ds, String dn, ChannelHandlerContext ctx) {
        String success = "0";
        String addr = ctx.channel().remoteAddress().toString();
        int start = 1;
        int end = addr.indexOf(":");
        String ip = StrUtil.sub(addr, start, end);
        //   ip = "58.221.137.62";
        String url = "http://api.map.baidu.com/location/ip?ip=" + ip + "&ak=" + baiduProperty.getAk() + "&coor=bd09ll";
        String json = HttpUtil.get(url, 1000);
        System.out.println(json);
        Addr addr1 = JSONUtil.toBean(json, Addr.class);
        if (success.equals(addr1.getStatus())) {
            DeviceInfo device = new DeviceInfo();
            device.setPosition(addr1.getContent().getPoint().getX() + "," + addr1.getContent().getPoint().getY());
            device.setAddress(addr1.getContent().getAddress_detail().getProvince() + "|" + addr1.getContent().getAddress_detail().getCity());
            Wrapper<DeviceInfo> wrapper = new EntityWrapper<>();
            wrapper.where("product_id = (select id from t_product_info where product_key = {0}) ", pk);
            wrapper.eq("device_no", dn);
            wrapper.eq("device_secret", ds);
            deviceService.update(device, wrapper);
        } else {

        }


    }

}

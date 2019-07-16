package com.xywg.iot.netty.handler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.common.global.GlobalStaticConstant;
import com.xywg.iot.common.utils.Crc16Util;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.FtpUtils;
import com.xywg.iot.dto.DataDTO;
import com.xywg.iot.enums.ExceptionType;
import com.xywg.iot.enums.MethodStatus;
import com.xywg.iot.enums.PropertyType;
import com.xywg.iot.exceptions.ExpressionException;
import com.xywg.iot.exceptions.MissingParamException;
import com.xywg.iot.exceptions.NotBooleanException;
import com.xywg.iot.modular.device.model.*;
import com.xywg.iot.modular.device.service.*;
import com.xywg.iot.modular.product.model.*;
import com.xywg.iot.modular.product.service.*;
import com.xywg.iot.netty.model.CompleteDataPojo;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.node.service.XyNodeService;
import com.xywg.iot.util.ExpressionUtil;
import com.xywg.iot.util.NettyUtil;
import com.xywg.iot.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.xywg.iot.common.global.GlobalStaticConstant.*;
import static com.xywg.iot.netty.handler.CommonStaticMethod.decode;
import static com.xywg.iot.netty.handler.CommonStaticMethod.stringToHexString;

/**
 * @author hjy
 * @date 2019/3/7
 */
@Component
public class BusinessHandle {

    /**
     * 方法执行超时时间
     */
    private static final long METHOD_ACCESS_TIMEOUT = 6000L;

    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private IDeviceInfoService deviceInfoService;
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private IProductPropertyService productPropertyService;
    @Autowired
    private IProductEventService productEventService;
    @Autowired
    private IProductEventParamService productEventParamService;
    @Autowired
    private IDeviceEventRecordService eventDetailService;
    @Autowired
    private IDeviceEventParamRecordService eventParamDetailService;
    @Autowired
    private IProductMethodService productMethodService;

    /**
     * 方法明细
     */
    @Autowired
    private IDeviceMethodRecordService methodDetailService;
    /**
     * 方法参数
     */
    @Autowired
    private IProductMethodParamService productMethodParamService;
    /**
     * 方法参数明细
     */
    @Autowired
    private IDeviceMethodParamRecordService methodParamDetailService;

    @Autowired
    private IDevicePropertyRecordService collectDetailService;
    @Autowired
    private ExceptionService exceptionService;
    @Autowired
    private IDevicePropertyRecordDetailService propertyDetailService;
    @Autowired
    private DeviceConfigFileRecordService configFileRecordService;
    @Autowired
    private XyNodeService xyNodeService;
    @Autowired
    private RedisUtil redisUtil;


    @Value("${xywg.upgrade-file-base-path}")
    private String upgradeFileBasePath;
    @Value("${xywg.redisHead}")
    private String redisHead;
    private Logger logger = LoggerFactory.getLogger(BusinessHandle.class);


    /**
     * 业务处理
     */
    void businessDataHandle(ChannelHandlerContext ctx, CompleteDataPojo dataDomain) {
        Channel channel = ctx.channel();
        switch (dataDomain.getCommand()) {
            //激活
            case "0001":
                logger.info("激活");
                activate(channel, dataDomain);
                break;
            //登录
            case "0002":
                logger.info("登录");
                login(channel, dataDomain);
                break;
            //心跳
            case "0003":
                logger.info("心跳");
                heartbeat(channel, dataDomain);
                break;
            //同步时间
            case "0004":
                logger.info("同步时间");
                synchronizationTime(channel, dataDomain);
                break;
            //上传监控数据
            case "0005":
                logger.info("监控数据");
                uploadMonitorData(channel, dataDomain);
                break;
            //升级请求(主动下发)
            case "0006":
                //attendanceBusinessLogicService.upgradeTransferData(channel, completeDataPojo);
                break;
            //发送接收文件请求
            case "0007":
                logger.info("升级中");
                commonMethod.upgradeTransfer(channel, dataDomain, REDIS_UPGRADE_HEAD);
                break;
            //升级文件接收完毕
            case "0008":
                logger.info("设备回复中心升级文件接收完毕.");
                commonMethod.upgradeCompleted(channel, dataDomain, REDIS_UPGRADE_HEAD);
                break;
            //0009为重启  主动下发操作
            case "0009":
                //logger.info("设备回复中心重启指令收到.");
                break;
            //发送异常情况
            case "000A":
                exceptionalAlarm(channel, dataDomain);
                break;
            //发送第三方设备通信命令文件请求
            case "000B":
                //receiveSendConfigCommand(channel, dataDomain);
                break;
            //回复文件内容传输
            case "000C":
                commonMethod.upgradeTransfer(channel, dataDomain, REDIS_SEND_CONFIG_HEAD);
                break;
            //发送升级文件传输完成
            case "000D":
                logger.info("发送配置文件传输完成.");
                commonMethod.upgradeCompletedConfig(channel, dataDomain);
                break;
            case "000E":
                uploadMonitorEvent(channel, dataDomain);
                break;
            case "000F":
                uploadMonitorMethod(channel, dataDomain);
                break;
            case "0010":
                deleteConfigFile(channel, dataDomain);
                break;

            default:
                break;

        }

    }

    /**
     *  删除配置文件
     * @param channel
     * @param monitorPojo
     */
    public void deleteConfigFile(Channel channel, CompleteDataPojo monitorPojo){

    }

    /**
     * 激活 只包含(sn,firmware)
     *
     * @param monitorPojo 数据
     */
    public void activate(Channel channel, CompleteDataPojo monitorPojo) {
        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01"));
        //查询企业信息是否已经填写过了
        EntityWrapper<DeviceInfo> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("device_no", sn);
        DeviceInfo deviceInfo = deviceInfoService.selectOne(ew);

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (deviceInfo == null || deviceInfo.getProductId() == null) {
            map.put("02", "00");
            map.put("03", "00");
            map.put("04", "00");
        } else {
            ProductInfo productInfo = productInfoService.selectById(deviceInfo.getProductId());
            map.put("02", "01");
            map.put("03", stringToHexString(productInfo.getProductKey()));
            map.put("04", stringToHexString(deviceInfo.getDeviceSecret()));

//            xyNodeService.bindChannel(NettyUtil.localKey(productInfo.getProductKey(),sn));
//            //TODO 没有登录，暂时用这边做登录
//            loginSuccess(channel,deviceInfo,productInfo);


        }
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);


    }

    /**
     * 登录 只包含(sn,firmware)
     *
     * @param monitorPojo 数据
     */
    private void login(Channel channel, CompleteDataPojo monitorPojo) {
        //序列号:
        String sn = decode(monitorPojo.getDataMap().get("01"));

        EntityWrapper<DeviceInfo> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("device_no", sn);
        ew.eq("device_secret", decode(monitorPojo.getDataMap().get("03")));

        DeviceInfo deviceInfo = deviceInfoService.selectOne(ew);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (deviceInfo == null || deviceInfo.getProductId() == null) {
            map.put("02", "02");
        } else {
            String pk = decode(monitorPojo.getDataMap().get("02"));
            ProductInfo productInfo = productInfoService.selectById(deviceInfo.getProductId());
            if (pk.equals(productInfo.getProductKey())) {
                //登录成功
                map.put("02", "01");
                //绑定通道
                xyNodeService.bindChannel(NettyUtil.localKey(pk, sn));
                //收到消息时回复给客户端
                commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

                loginSuccess(channel, deviceInfo, productInfo);

                  // sendConfig(deviceInfo.getDeviceNo());
            } else {
                map.put("02", "02");
                //收到消息时回复给客户端
                commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
            }
        }


    }


    /**
     * 心跳
     *
     * @param monitorPojo 数据
     */
    public void heartbeat(Channel channel, CompleteDataPojo monitorPojo) {
        //收到消息时回复给客户端

        if (NettyChannelManage.getDeviceCodeByChannel(channel) == null) {
            throw new RuntimeException("没登录");
        }

        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        /*//序列号
        String sn = decode(monitorPojo.getDataMap().get("01").replaceAll(" ", ""));*/

    }


    /**
     * 同步时间
     *
     * @param monitorPojo 数据
     */
    public void synchronizationTime(Channel channel, CompleteDataPojo monitorPojo) {
        //序列号 16进制的:
        String sn = monitorPojo.getDataMap().get("01");
        commonMethod.synchronizationTime(channel, sn, monitorPojo.getCommand(), monitorPojo.getVersion());
    }


    /**
     * 上传监控数据
     *
     * @param monitorPojo 完整的原始数据实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void uploadMonitorData(Channel channel, CompleteDataPojo monitorPojo) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);

        //查询产品属性
        Wrapper<ProductProperty> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.eq("product_id", deviceConnectInfo.getProductInfo().getId());
        List<ProductProperty> productPropertys = productPropertyService.selectList(propertyWrapper);
        Date now = new Date();
        //新增采集明细
        DevicePropertyRecord collectDetail = new DevicePropertyRecord(now, deviceConnectInfo.getDeviceInfo().getId());
        StringBuffer dataDetail = new StringBuffer();
        StringBuffer alarmDetail = new StringBuffer();

        List<DevicePropertyRecordDetail> propertyDetails = new ArrayList<>();
        // 存放 转换后的参数数据
        Map<String, Object> convertorValues = new HashMap<>(10);
        // 存放 表达式
        Map<String, String> expressions = new HashMap<>(10);

        productPropertys.forEach(item -> {
            String pk = deviceConnectInfo.getProductInfo().getProductKey();
            String dn = deviceConnectInfo.getSn();
            //组装数据
            String key = item.getKey();
            //todo
            String innerKey = getInnerKey(monitorPojo, key);
            if (innerKey == null) {
                String content = "缺少属性<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk, dn, ExceptionType.PROPETY, content, monitorPojo.toString());
                throw new RuntimeException(content);
            }

            String hexValue = monitorPojo.getDataMap().get(innerKey);
            String value = new String(DataUtil.hexStringToBytes(hexValue.replaceAll(" ", "")));
            value = value.split(":")[1];
            String alarmExpression = item.getAlarmExpression();
            int type = item.getDataType();
            Object convertorParam;
            try {
                convertorParam = ExpressionUtil.parseObject(type, value);
            } catch (Exception e) {
                String content = "属性<" + item.getName() + "-" + key + ">类型错误，需要类型<" + PropertyType.getCommons(type) + "> ";
                exceptionService.insertError(pk, dn, ExceptionType.PROPETY, content, monitorPojo.toString());
                throw new RuntimeException(content);
            }

            convertorValues.put(key, convertorParam);
            expressions.put(key, alarmExpression);
            //明细
            DevicePropertyRecordDetail propertyDetail;
            try {
                if (ExpressionUtil.test(convertorValues, expressions.get(key), convertorValues.get(key))) {
                    //报警
                    propertyDetail = new DevicePropertyRecordDetail(item.getId(), value, collectDetail.getId(), 1);
                    dataDetail.append(",").append(item.getName()).append(":").append(value);
                    alarmDetail.append(",").append(1);
                } else {
                    //未报警
                    propertyDetail = new DevicePropertyRecordDetail(item.getId(), value, collectDetail.getId(), 0);
                    dataDetail.append(",").append(item.getName()).append(":").append(value);
                    alarmDetail.append(",").append(0);
                }
            } catch (MissingParamException e) {
                String content = "属性<" + item.getName() + "-" + key + ">的报警表达式配置错误，产品中缺少<" + e.getMessage() + ">属性";
                exceptionService.insertError(pk, dn, ExceptionType.PROPETY, content, monitorPojo.toString());
                throw new RuntimeException(content);
            } catch (ExpressionException | NotBooleanException e) {
                String content = e.getMessage();
                exceptionService.insertError(pk, dn, ExceptionType.PROPETY, content, monitorPojo.toString());
                throw new RuntimeException(content);
            }

            propertyDetails.add(propertyDetail);
        });
        if (dataDetail.length() > 0) {
            collectDetail.setDataDetail(dataDetail.substring(1));
            collectDetail.setAlarmDetail(alarmDetail.substring(1));
        }

        collectDetailService.insert(collectDetail);
        if (propertyDetails.size() > 0) {
            propertyDetailService.insertBatch(propertyDetails);
        }
    }

    /**
     * 上传 事件
     *
     * @param monitorPojo 完整的原始数据实体
     */
    public void uploadMonitorEvent(Channel channel, CompleteDataPojo monitorPojo) {
        //收到消息时回复给客户端
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //查询设备
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        String pk = deviceConnectInfo.getProductInfo().getProductKey();
        String dn = deviceConnectInfo.getDeviceInfo().getDeviceNo();
        String cacheDeviceKey = pk + ":" + dn;
        //查询 缓存
        DeviceInfo device = deviceInfoService.selectCacheOne(cacheDeviceKey);
        if (device == null) {
            throw new RuntimeException("设备不存在<" + deviceConnectInfo.getDeviceInfo().getDeviceNo() + "> ");
        }
        String hexEeventKey = monitorPojo.getDataMap().get("02");
        String eventKey = new String(DataUtil.hexStringToBytes(hexEeventKey.replaceAll(" ", "")));
        //查询事件
        Wrapper<ProductEvent> eventWrapper = new EntityWrapper<>();
        eventWrapper.where("product_id = (select id from t_product_info where product_key = {0})", pk);
        eventWrapper.eq("`key`", eventKey);
        ProductEvent event = productEventService.selectOne(eventWrapper);
        if (event == null) {
            String content = "事件不存在<" + eventKey + "> ";
            exceptionService.insertError(pk, dn, ExceptionType.EVENT, content, monitorPojo.toString());
            throw new RuntimeException(content);
        }
        //查询事件参数

        Wrapper<ProductEventParam> eventParamWrapper = new EntityWrapper<>();
        eventParamWrapper.eq("event_id", event.getId());
        List<ProductEventParam> eventParams = productEventParamService.selectList(eventParamWrapper);


        StringBuffer inParams = new StringBuffer();
        // 添加事件明细
        Date now = new Date();
        DeviceEventRecord eventDetail = new DeviceEventRecord(event.getId(), now, device.getId());
        eventDetailService.insert(eventDetail);
        // 添加事件参数明细
        List<DeviceEventParamRecord> eventParamDetails = new ArrayList<>();


        eventParams.forEach(item -> {
            String key = item.getKey();

            String innerKey = getInnerKey(monitorPojo, key);
            //配置的事件参数与 接收到的参数列表进行比对
            if (innerKey == null) {
                String content = "事件<" + eventKey + "> 缺少参数<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk, dn, ExceptionType.EVENT, content, monitorPojo.toString());
                throw new RuntimeException(content);
            }
            String value = new String(DataUtil.hexStringToBytes(monitorPojo.getDataMap().get(innerKey)));
            value = value.split(":")[1];
            inParams.append(",").append(item.getName()).append(":").append(value);
            DeviceEventParamRecord paramDetail = new DeviceEventParamRecord(item.getId(), eventDetail.getId(), value);
            eventParamDetails.add(paramDetail);
        });
        if (inParams.length() > 0) {
            eventDetail.setInParams(inParams.substring(1));
        }
        eventDetailService.updateById(eventDetail);
        if (eventParamDetails.size() > 0) {
            eventParamDetailService.insertBatch(eventParamDetails);
        }
    }


    /**
     * 上传 方法执行结果
     *
     * @param monitorPojo 完整的原始数据实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void uploadMonitorMethod(Channel channel, CompleteDataPojo monitorPojo) {


        //收到消息时回复给客户端
        //   commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);

        //查询设备
        DeviceConnectInfo deviceConnectInfo = NettyChannelManage.getDeviceCodeByChannel(channel);
        String pk = deviceConnectInfo.getProductInfo().getProductKey();
        String dn = deviceConnectInfo.getDeviceInfo().getDeviceNo();
        String cacheDeviceKey = pk + ":" + dn;

        String hexUuid = monitorPojo.getDataMap().get("03");
        Integer uuid = Integer.parseInt(new String(DataUtil.hexStringToBytes(hexUuid)));


        String hexMethodKey = monitorPojo.getDataMap().get("02");
        String methodKey = new String(DataUtil.hexStringToBytes(hexMethodKey.replaceAll(" ", "")));

        //查询明细
        DeviceMethodRecord methodDetail = methodDetailService.selectById(uuid);
        if (methodDetail == null) {
            String content = "方法<" + methodKey + "> 未找到记录<" + uuid + "> ";
            exceptionService.insertError(pk, dn, ExceptionType.METHOD, content, monitorPojo.toString());
            throw new RuntimeException(content);
        }
        if (methodDetail.getStatus() != MethodStatus.NO_RETURN.getStatus()) {
            String content = "方法<" + methodKey + "> 已返回 已返回记录<" + uuid + "> ";
            exceptionService.insertError(pk, dn, ExceptionType.METHOD, content, monitorPojo.toString());
            throw new RuntimeException(content);
        }
        Date now = new Date();
        methodDetail.setReceiveTime(now);
        long between = DateUtil.between(now, methodDetail.getCallTime(), DateUnit.MS);
        if (between > METHOD_ACCESS_TIMEOUT) {
            // 超过6秒算超时
            methodDetail.setStatus(MethodStatus.TIMEOUT.getStatus());
        } else {
            methodDetail.setStatus(MethodStatus.RETURN.getStatus());
        }


        //查询设备

        StringBuffer outParams = new StringBuffer();


        DeviceInfo device = deviceInfoService.selectCacheOne(cacheDeviceKey);
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
            exceptionService.insertError(pk, dn, ExceptionType.METHOD, content, monitorPojo.toString());
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
            String innerKey = getInnerKey(monitorPojo, key);
            if (innerKey == null) {
                String content = "方法<" + methodKey + "> 缺少参数<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk, dn, ExceptionType.METHOD, content, monitorPojo.toString());
                throw new RuntimeException(content);
            }
            String value = new String(DataUtil.hexStringToBytes(monitorPojo.getDataMap().get(innerKey)));
            value = value.split(":")[1];
            outParams.append(",").append(item.getName()).append(":").append(value);
            DeviceMethodParamRecord methodParamDetail = new DeviceMethodParamRecord(item.getId(), uuid, value);
            methodParamDetails.add(methodParamDetail);
        });
        if (outParams.length() > 0) {
            methodDetail.setOutParams(outParams.substring(1));
        }
        methodDetailService.updateById(methodDetail);

        if (methodParamDetails.size() > 0) {
            methodParamDetailService.insertBatch(methodParamDetails);
        }


    }

    /**
     * 入参
     *
     * @param pk        产品
     * @param dn        设备名
     * @param methodKey 方法
     * @param params    参数
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> insertInMethod(String pk, String dn, String methodKey, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>(10);

        //查询设备
        String cacheDeviceKey = pk + ":" + dn;
        DeviceInfo device = deviceInfoService.selectCacheOne(cacheDeviceKey);
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
            exceptionService.insertError(pk, dn, ExceptionType.METHOD, content, JSONUtil.toJsonStr(params));
            throw new RuntimeException(content);
        }
        result.put("type", method.getIsSync().toString());

        //查询方法入参
        Wrapper<ProductMethodParam> methodParamWrapper = new EntityWrapper<>();
        methodParamWrapper.eq("method_id", method.getId());
        methodParamWrapper.eq("param_type", 1);
        List<ProductMethodParam> methodParams = productMethodParamService.selectList(methodParamWrapper);
        result.put("methodParams", methodParams);
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
                String content = "事件<" + methodKey + "> 缺少参数<" + item.getName() + "-" + key + "> ";
                exceptionService.insertError(pk, dn, ExceptionType.METHOD, content, JSONUtil.toJsonStr(params));
                throw new RuntimeException(content);
            }
            inParams.append(",").append(item.getName()).append(":").append(params.get(key));
            DeviceMethodParamRecord methodParamDetail = new DeviceMethodParamRecord(item.getId(), methodDetail.getId(), params.get(key));
            methodParamDetails.add(methodParamDetail);

        });
        if (inParams.length() > 0) {
            methodDetail.setInParams(inParams.substring(1));
        }
        methodDetailService.updateById(methodDetail);
        if (methodParamDetails.size() > 0) {
            methodParamDetailService.insertBatch(methodParamDetails);
        }

        return result;
    }

    /**
     * 服务端调用设备方法
     *
     * @param pk
     * @param dn
     * @param methodKey
     * @param params
     */
    public void callMethod(String pk, String dn, String methodKey, Map<String, String> params) {

//        if (!isLogin(pk, dn)) {
//            throw new RuntimeException("设备未上线");
//        }
        //返回 是否同步，方法明细id
        Map<String, Object> result = insertInMethod(pk, dn, methodKey, params);
        callDevice(pk, dn, methodKey, (String) result.get("uuid"), params, (List<ProductMethodParam>) result.get("methodParams"));

    }

    private void callDevice(String pk, String dn, String methodKey, String uuid, Map<String, String> params, List<ProductMethodParam> methodParams) {

        String localKey = dn;
        Channel channel = NettyChannelManage.CHANNEL_MAP.get(localKey);
        if (channel == null) {
            throw new RuntimeException("设备未上线");
        }
        DataDTO dataDTO = new DataDTO();
        dataDTO.setMethod("method." + methodKey);
        dataDTO.setUuid(uuid);
        dataDTO.setParams(params);

        LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
        String hexMethodKey = DataUtil.stringToHexString(methodKey).replaceAll(" ", "");
        linkedMap.put("02", hexMethodKey);
        uuid = StrUtil.fillBefore(uuid, '0', 16);
        String hexUuid = StrUtil.fillBefore(DataUtil.stringToHexString(uuid).replaceAll(" ", ""), '0', 32);
        linkedMap.put("03", hexUuid);


        AtomicReference<Integer> count = new AtomicReference<>(4);
        //塞参数
        methodParams.forEach(item -> {
            String key = item.getKey();
            if (!params.containsKey(key)) {
                throw new RuntimeException("缺少入参<" + item.getName() + "-" + key + "> ");
            }
            Integer type = item.getDataType();
            String value = params.get(key);
            //todo
            String content = key + ":" + value + ":" + type;

            //计算id
            String hexId = StrUtil.fillBefore(Integer.toHexString(count.get()), '0', 2);
            //计算值
            String hexContent = DataUtil.stringToHexString(content);
            count.set(count.get() + 1);
            linkedMap.put(hexId, hexContent);
        });

        commonMethod.resMessageJoint(channel, DataUtil.stringToHexString(dn), linkedMap, "000F", "01", GlobalStaticConstant.DATA_PACKEAGE_REQUEST);
//        String  data =  packageMethodData(pk,dn,methodKey,uuid,methodParams,params).replaceAll(" ","");
//        channel.writeAndFlush(Unpooled.copiedBuffer(DataUtil.hexStringToBytes(data)));


    }

    /**
     * 传输升级数据
     *
     * @param monitorPojo 数据
     */
    public void upgradeTransferData(Channel channel, CompleteDataPojo monitorPojo) {
        commonMethod.upgradeTransfer(channel, monitorPojo, REDIS_UPGRADE_HEAD);
    }


    /**
     * 发送异常情况  该功能暂时还没有定好具体业务
     *
     * @param monitorPojo 数据
     */
    public void exceptionalAlarm(Channel channel, CompleteDataPojo monitorPojo) {
        //回复
        commonMethod.resMessageJoint(channel, monitorPojo.getDataMap().get("01"), null, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);


    }

    /**
     * 设备回复的发送配置命令
     *
     * @param channel
     * @param monitorPojo
     */
    public void receiveSendConfigCommand(Channel channel, CompleteDataPojo monitorPojo) {
        String status = monitorPojo.getDataMap().get("04");
        if ("01".equals(status)) {
            //开始发送文件
            commonMethod.upgradeTransfer(channel, monitorPojo, REDIS_SEND_CONFIG_HEAD);
        } else {
            //设备未准备好接收配置文件

        }

    }


    /**
     * 登录成功时执行
     */
    private void loginSuccess(Channel channel, DeviceInfo deviceInfo, ProductInfo productInfo) {
        String sn = deviceInfo.getDeviceNo();
        DeviceInfo device = new DeviceInfo();
        device.setId(deviceInfo.getId());
        device.setStatus(3);
        //更新设备状态为在线
        deviceInfoService.updateById(device);

        //    if (!NettyChannelManage.CHANNEL_MAP.containsKey(sn)) {
        //更新map有效的链接
        NettyChannelManage.CHANNEL_MAP.put(sn, channel);
        String uuid = DataUtil.getUUID();
        DeviceConnectInfo deviceConnectInfo = new DeviceConnectInfo(uuid, sn);
        deviceConnectInfo.setDeviceInfo(deviceInfo);
        deviceConnectInfo.setProductInfo(productInfo);
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(deviceConnectInfo);
        //  }

        EntityWrapper<DeviceConfigFileRecord> ew = new EntityWrapper<>();
        ew.eq("device_id", deviceInfo.getId());
        ew.eq("product_id", productInfo.getId());
        ew.eq("issued_flag", 0);
        ew.eq("is_del", 0);
        ew.orderBy("id",false);
        //登录成功时,查看是否有需要下发的配置文件
        List<DeviceConfigFileRecord> recordList = configFileRecordService.selectList(ew);
        if (recordList.size() > 0) {
            sendConfig(deviceInfo.getDeviceNo(), recordList.get(0).getPath());
        }


    }

    /**
     * 返回 数据体的 ID
     *
     * @param monitorPojo
     * @param key
     * @return
     */
    private static String getInnerKey(CompleteDataPojo monitorPojo, String key) {

        for (Map.Entry<String, String> entry : monitorPojo.getDataMap().entrySet()) {
            String value = new String(DataUtil.hexStringToBytes(entry.getValue()));
            if (value.contains(":")) {
                if (key.equals(value.split(":")[0])) {
                    return entry.getKey();
                }
            }

        }

        return null;
    }


    /**
     * 发送配置
     * path: industrial_iot/firmware/2019/03/06/1551840643249.txt
     */
    public void sendConfig(String sn, String path) {
        String ftpPath = path.substring(0, path.lastIndexOf("/"));
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        FtpUtils ftpUtils = new FtpUtils();
        boolean b = ftpUtils.downloadFile(ftpPath, fileName, upgradeFileBasePath + ftpPath);
        if (!b) {
            logger.info("<<从ftp下载文件失败,path:" + path + ">>");
        }
        long fileLength = new File(upgradeFileBasePath + path).length();
        //文件长度转16进制
        String fileLength16 = Long.toHexString(fileLength).length() % 2 == 0 ?
                Long.toHexString(fileLength) : ("0" + Long.toHexString(fileLength));
        //序列号转16进制字节码
        String snCode = stringToHexString(sn);
        Channel channel = NettyChannelManage.getChannel(sn);
        if (channel != null) {
            LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
            linkedMap.put("02", fileLength16);
            commonMethod.resMessageJoint(channel, snCode, linkedMap, "000B", "01", "00");
            redisUtil.set(redisHead + ":" + REDIS_SEND_CONFIG_HEAD + sn, path);
        }

    }

    private boolean isLogin(ChannelHandlerContext ctx) {
        DeviceConnectInfo sn = ctx.channel().attr(NettyChannelManage.NETTY_CHANNEL_KEY).get();
        return sn != null;
    }

    public static boolean isLogin(String pk, String dn) {
        String localkey = dn;
        Channel channel = NettyChannelManage.CHANNEL_MAP.get(localkey);
        if (channel == null) {
            return false;
        }
        DeviceConnectInfo sn = channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).get();
        return sn != null;

    }

    /**
     * 包装 方法数据
     *
     * @param params
     * @return
     */
    public String packageMethodData(String pk, String sn, String methodKey, String uuid, List<ProductMethodParam> methodParams, Map<String, String> params) {

        String paramsData = packageParams(methodParams, params);
        String methodData = packageMethod(methodKey);

        StringBuffer sb = new StringBuffer();
        /**头部*/
        //固定码
        sb.append("FF FF FE ")
                //version
                .append("01 ")
                // cmd
                .append("00 0F ")
                //AQ
                .append(GlobalStaticConstant.DATA_PACKEAGE_REQUEST + " ");

        // data length
        String datalength = StrUtil.fillBefore(Integer.toHexString(12 + paramsData.length() / 3 + methodData.length() / 3), '0', 4);
        sb.append(datalength.substring(0, 2) + " " + datalength.substring(2, 4) + " ")
                //AQ status
                .append("00 ")
                // crc16
                .append("## ## ")
                /**设备号块*/
                .append("00 13 ")
                .append("01 ")
                .append(Crc16Util.byteTo16String(sn.getBytes()));
        /**方法名*/
        sb.append(methodData);
        /**uuid*/
        sb.append("00 13 ")
                .append("03 ")
                .append(fillSpace(StrUtil.fillBefore(Integer.toHexString(Integer.parseInt(uuid)), '0', 32)));
        /** 参数 */
        sb.append(paramsData);
        String crc;
        String data;
        if (" ".equals(sb.substring(sb.length() - 1))) {
            data = sb.substring(0, sb.length());
        } else {
            data = sb.toString();
        }
        crc = doCrc(data);
        return changeCrc(sb.toString(), crc);
    }

    /**
     * 计算校验码
     *
     * @param responsedata
     * @return
     */
    protected static String doCrc(String responsedata) {
        responsedata = responsedata.substring(36).replaceAll(" ", "");
        String crc = getCRC(DataUtil.hexStringToBytes(responsedata));
        return StrUtil.fillBefore(crc, '0', 4);
    }

    /**
     * 替换校验码
     *
     * @param responsedata
     * @param crcCode
     * @return
     */
    protected static String changeCrc(String responsedata, String crcCode) {
        //原始校验字符串
        String crcCheckCode = responsedata.replaceAll(" ", "");
        StringBuffer sb = new StringBuffer(crcCheckCode);
        //改变协议头中的AQ    0：应答包；1：请求包
        sb.replace(22, 24, crcCode.substring(2, 4));
        //错误类型位置
        sb.replace(20, 22, crcCode.substring(0, 2));
        return sb.toString();
    }

    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC);
    }

    public static String fillSpace(String hexStr) {

        int length = hexStr.length() / 2;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(StrUtil.sub(hexStr, 2 * i, 2 * (i + 1)));
            sb.append(" ");
        }


        return sb.toString();
    }

    public String packageMethod(String method) {
        StringBuffer sb = new StringBuffer();
        String content = method;
        //计算长度
        String hexLength = fillSpace(StrUtil.fillBefore(Integer.toHexString(3 + content.length()), '0', 4));
        //计算id
        String hexId = fillSpace(StrUtil.fillBefore(Integer.toHexString(2), '0', 2));
        //计算值
        String hexContent = fillSpace(DataUtil.stringToHexString(content));
        sb.append(hexLength)
                .append(hexId)
                .append(hexContent);
        return sb.toString();
    }

    public String packageParams(List<ProductMethodParam> methodParams, Map<String, String> params) {
        AtomicReference<Integer> count = new AtomicReference<>(4);
        StringBuffer sb = new StringBuffer();
        //塞参数
        methodParams.forEach(item -> {
            String key = item.getKey();
            if (!params.containsKey(key)) {
                throw new RuntimeException("缺少入参<" + item.getName() + "-" + key + "> ");
            }
            Integer type = item.getDataType();
            String value = params.get(key);
            //todo
            String content = key + ":" + value + ":" + type;
            //计算长度
            String hexLength = fillSpace(StrUtil.fillBefore(Integer.toHexString(3 + content.length()), '0', 4));
            //计算id
            String hexId = fillSpace(StrUtil.fillBefore(Integer.toHexString(count.get()), '0', 2));
            //计算值
            String hexContent = fillSpace(DataUtil.stringToHexString(content));
            count.set(count.get() + 1);

            sb.append(hexLength)
                    .append(hexId)
                    .append(hexContent);
        });
        return sb.toString();
    }

    public static void main(String[] args) {


        System.out.println(Integer.parseInt("7", 16));


    }

}

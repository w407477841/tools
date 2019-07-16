package com.xywg.iot.netty.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.common.utils.Crc16Util;
import com.xywg.iot.common.utils.DateUtils;
import com.xywg.iot.common.utils.FileUtil;
import com.xywg.iot.enums.DeviceStatus;
import com.xywg.iot.modular.device.model.DeviceConfigFileRecord;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.DeviceConfigFileRecordService;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradePackageService;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradeRecordService;
import com.xywg.iot.netty.model.CompleteDataPojo;
import com.xywg.iot.netty.model.DeviceConnectInfo;
import com.xywg.iot.node.properties.XyNodeProperties;
import com.xywg.iot.node.service.XyNodeService;
import com.xywg.iot.util.RedisUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;

import static com.xywg.iot.common.global.GlobalStaticConstant.*;
import static com.xywg.iot.netty.handler.CommonStaticMethod.*;

/**
 * @author hjy
 * @date 2018/10/26
 */
@Component
public class CommonMethod {
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired
    private IDeviceInfoService deviceInfoService;
    @Autowired
    private IDeviceUpgradePackageService deviceUpgradePackageService;
    @Autowired
    private IDeviceUpgradeRecordService deviceUpgradeRecordService;
    @Autowired
    private DeviceConfigFileRecordService configFileRecordService;
    @Autowired
    private XyNodeProperties xyNodeProperties;
    @Autowired
    private XyNodeService xyNodeService;

    @Value("${xywg.redisHead}")
    private String redisHead;
    @Value("${xywg.upgrade-file-base-path}")
    private String upgradeFileBasePath;


    @Autowired
    private RedisUtil redisUtil;
    private Logger LOGGER = LoggerFactory.getLogger(CommonMethod.class);

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
        List<Map<String,Object>> deviceInfos =  deviceInfoService.selectMaps(wrapper);
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
            deviceInfoService.updateBatchById(offlines);
        }
        redisUtil.remove("iot:device");

    }


    /**
     * 传输升级数据
     *
     * @param monitorPojo
     */
    public void upgradeTransfer(Channel channel, CompleteDataPojo monitorPojo, String command) {
        String sn = monitorPojo.getDataMap().get("01").replaceAll(" ", "");
        String deviceCode = decode(sn);
        if(!"00".equals(monitorPojo.getErrorStatus())){
            //  升级记录修改
         DeviceConnectInfo deviceConnectInfo =  NettyChannelManage.getDeviceCodeByChannel(channel);
            updateUpgradeStatue((Integer)redisUtil.get(redisHead + ":package:" + command + deviceCode),deviceConnectInfo.getDeviceInfo().getId(),false);
            return ;
        }
        String path = redisUtil.get(redisHead + ":" + command + deviceCode).toString();
        File file = new File(upgradeFileBasePath + path);
        byte[] bytes = FileUtil.file2Byte(file);

        //已发送的文件的内容长度
        Integer length = Integer.parseInt(monitorPojo.getDataMap().get("04")
                .replaceAll(" ", ""), 16);
        byte[] newBytes;
        //最大只能发送1024个字节
        Integer everyTimeMaxLength = 1024;
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //本次发送刚好1024字节
        assert bytes != null;
        if (length + everyTimeMaxLength < bytes.length) {
            newBytes = Arrays.copyOfRange(bytes, length, everyTimeMaxLength + length);
            String message16 = toHexString(newBytes);
            map.put("03", message16);
            map.put("05", Integer.toHexString(everyTimeMaxLength));
            resMessageJoint(channel, sn, map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
            //本次发送不足1024字节
        } else if (length < bytes.length && (length + everyTimeMaxLength) > bytes.length) {
            newBytes = Arrays.copyOfRange(bytes, length, bytes.length);
            String message16 = toHexString(newBytes);
            map.put("03", message16);
            map.put("05", Integer.toHexString(bytes.length - length));
            resMessageJoint(channel, sn, map, monitorPojo.getCommand(), monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
            //发送完成
        } else {
            String resCommandCode ="";
            //升级功能的文件
            if(command.equals(REDIS_UPGRADE_HEAD)){
                resCommandCode = "0008";
             //发送配置文件
            }else if(command.equals(REDIS_SEND_CONFIG_HEAD)){
                resCommandCode = "000D";
            }

            //升级文件发送完毕
            resMessageJoint(channel, sn, map, resCommandCode, monitorPojo.getVersion(), DATA_PACKAGE_RESPONSE);
        }
    }

    /**
     *   完成升级
      * @param channel
     * @param monitorPojo
     * @param command
     */
    public void upgradeCompleted(Channel channel, CompleteDataPojo monitorPojo, String command){
        String sn = monitorPojo.getDataMap().get("01").replaceAll(" ", "");
        String deviceCode = decode(sn);
        DeviceConnectInfo deviceConnectInfo =  NettyChannelManage.getDeviceCodeByChannel(channel);
        updateUpgradeStatue((Integer)redisUtil.get(redisHead + ":packageRecord:" + command + deviceCode),deviceConnectInfo.getDeviceInfo().getId(),"00".equals(monitorPojo.getErrorStatus()));
    }

    /**
     *   配置文件发送完成
     * @param channel
     * @param monitorPojo
     */
    public void upgradeCompletedConfig(Channel channel, CompleteDataPojo monitorPojo){
        String sn = monitorPojo.getDataMap().get("01").replaceAll(" ", "");
        String deviceCode = decode(sn);
        Integer configRecordId  = Integer.parseInt(redisHead + ":" + REDIS_SEND_CONFIG_HEAD+"_record_"+deviceCode);
        DeviceConfigFileRecord configFileRecord = new  DeviceConfigFileRecord();
        configFileRecord.setIssuedFlag(2);
        configFileRecord.setId(configRecordId);
        configFileRecord.setModifyTime(new Date());
        configFileRecordService.updateById(configFileRecord);
    }

    /**
     *  修改 配置文件记录
     * @param packageId
     * @param deviceId
     * @param success
     */
    public void updateConfigStatus(Integer packageId,Integer deviceId,boolean success){



    }


    /**
     * 修改 升级记录
     */
    public void updateUpgradeStatue(Integer packageId,Integer deviceId,boolean success){

        //查询升级履历的数据
        DeviceUpgradeRecord upgradeRecord = deviceUpgradeRecordService.selectById(packageId);
        if (upgradeRecord == null) {
            LOGGER.info("设备升级结果中设备返回的id字段无效");
            return;
        }



        //根据升级包ID  查询升级包信息
        DeviceUpgradePackage deviceUpgradePackage = deviceUpgradePackageService.selectById(upgradeRecord.getPackageId());


            DeviceUpgradeRecord deviceUpgradeRecord = new DeviceUpgradeRecord();
            deviceUpgradeRecord.setId(upgradeRecord.getId());

            if (success) {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setId(deviceId);
                deviceInfo.setVersion(deviceUpgradePackage.getVersion());
                //升级成功时更新设备的版本号
                deviceInfoService.updateById(deviceInfo);
                deviceUpgradeRecord.setIssuedFlag(2);
            } else {
                deviceUpgradeRecord.setIssuedFlag(3);
            }
            //更新升级履历表中的升级结果
            deviceUpgradeRecordService.updateById(deviceUpgradeRecord);
    }

    /**
     * 实时数据 推入redis
     *
     * @param redisUtil  工具
     * @param deviceNo   设备编号
     * @param deviceType 设备类型
     * @param uuid       项目编号
     * @param data       数据
     */
    public void push(RedisUtil redisUtil, String deviceNo, String deviceType, String uuid, Object data, String projectId) {
        String key = redisHead + ":current:" + uuid + ":" + deviceType + ":" + deviceNo;
        String topic = "/topic/current/" + deviceType + "/" + uuid;
        String tempTopic = "/topic/current/" + deviceType + "/" + projectId + "/" + deviceNo;
        if (!deviceType.endsWith("alarm")) {
            topic = "/topic/current/" + deviceType + "/" + uuid + "/" + deviceNo;
            redisUtil.set(key, JSONUtil.toJsonStr(data));
            if (StringUtils.isNotEmpty(projectId)) {
                String tempKey = redisHead + ":current:" + projectId + ":" + deviceType + ":" + deviceNo;
                redisUtil.set(tempKey, JSONUtil.toJsonStr(data));
                simpMessageSendingOperations.convertAndSend(tempTopic, data);
            }
        }
        //System.out.println("订阅路径：" + topic);
        // 推送至 websocket
        simpMessageSendingOperations.convertAndSend(topic, data);
    }


    /**
     * 回复消息的拼接(数据体有变动时使用该方法)
     *
     * @param channel
     * @param sn16            设备序列号 16进制串
     * @param map             key 消息的id  value: 一个回复消息的消息内容 16进制字符串
     * @param command         功能码 16进制串
     * @param protocolVersion 协议版本   16进制串
     * @param aQ              00 为应答包,01为请求包
     */
    public void resMessageJoint(Channel channel, String sn16, LinkedHashMap<String, String> map, String command, String protocolVersion, String aQ) {
        //消息错误类型,00 表示正常数据,其他代表有错  在这里只会是正确的数据
        String messageStatus = "00";
        //31个固定长度
        String total;
        if (map != null && map.size() > 0) {
            int length = 0;
            for (String key : map.keySet()) {
                int messageLength = complement2(map.get(key).replaceAll(" ", "")).length() / 2 + 3;
                length += messageLength;
            }
            total = Integer.toHexString(31 + length);
        } else {
            total = Integer.toHexString(31);
        }
        //总长度(总长度不可能比10小,所以不考虑长度是1位的情况)
        String totalLength = complement(total);

        //设备序列号数据体 :  0013 为长度,01 为消息id
        String dev = "001301" + sn16;

        //回复的数据体
        StringBuilder resMessage = new StringBuilder();
        if (map != null && map.size() > 0) {
            for (String key : map.keySet()) {
                //回复的当前id消息内容(消息长度是奇数位时高位补0)
                String message = complement2(map.get(key).replaceAll(" ", ""));
                //回复的数据体长度
                String resMessageLength = Integer.toHexString(3 + message.length() / 2);
                //因为长度位一共占2个字节,故需要补0操作
                String messageLength = complement(resMessageLength);
                //回复的数据体(长度+id+内容)
                resMessage.append(messageLength).append(key).append(message);
            }
        }
        //数据体
        String data = dev + resMessage.toString();

        //得到crc16
        String crc = Crc16Util.getCRC16(data);

        //拼接最终16进制字符串
        String message = XYWG_PROTOCOL_HEAD + protocolVersion + command + aQ + totalLength + messageStatus + crc + data;
        /*System.out.println("回复:" + message);*/
        channel.writeAndFlush(Unpooled.copiedBuffer(toBytes(message)));

        LOGGER.info("System Reply:<<Device:" + decode(sn16) + " Message:" + message + ">>");
    }


    /**
     * 数据长度不够时高位补位  一共四位
     *
     * @return
     */
    private static String complement(String message) {
        if (message.length() == 1) {
            message = ("000" + message);
        } else if (message.length() == 2) {
            message = ("00" + message);
        } else if (message.length() == 3) {
            message = ("0" + message);
        }
        return message;
    }

    private static String complement2(String message) {
        if (message.length() % 2 == 0) {
            return message;
        } else {
            return "0" + message;
        }
    }

    /**
     * 转换版本号
     *
     * @return
     */
    public String getDeviceVersion(String version) {
        StringBuilder sb = new StringBuilder();
        sb.append("v");
        for (int i = 0; i < version.length(); i = i + 2) {
            sb.append(Integer.parseInt(version.substring(i, i + 2), 16)).append(".");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 更新设备为在线离线状态
     */
    public void handleDeviceIsOnline(Integer deviceId, Integer state) {
        DeviceInfo device = new DeviceInfo();
        device.setId(deviceId);
        device.setStatus(state);
        //更新设备状态
        deviceInfoService.updateById(device);
    }

    /**
     * 同步时间
     *
     * @param channel 上下文
     * @param sn      序列号
     * @param command 所属命令块
     * @param version 协议版本
     */
    public void synchronizationTime(Channel channel, String sn, String command, String version) {
        //获取服务器当前时间
        Date now = new Date();
        String dateTimeStr = DateUtils.formatDate(now, "yyyyMMddHHmmss");
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        //02 代表功能数据的id  即回复的时间
        linkedHashMap.put("02", dateTimeStr+("0"+(DateUtil.dayOfWeek(now)+6)%7));
        resMessageJoint(channel, sn, linkedHashMap, command, version, DATA_PACKAGE_RESPONSE);
    }

}

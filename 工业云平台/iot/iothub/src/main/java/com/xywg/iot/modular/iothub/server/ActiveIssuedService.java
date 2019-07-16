package com.xywg.iot.modular.iothub.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.iot.common.service.WebsocketSendMessageService;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.XySession;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.iothub.netty.handler.IotHubHandler;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfig;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfigRecord;
import com.xywg.iot.modular.remote.model.OnlineDebug;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigRecordVO;
import com.xywg.iot.modular.remote.service.DeviceRemoteConfigRecordService;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecordData;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradeRecordService;
import com.xywg.iot.util.NettyUtil;
import com.xywg.iot.vo.ResultVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xywg.iot.modular.iothub.netty.handler.IotHubHandler.NETTY_CHANNEL_KEY;

/**
 * @author hjy
 * @date 2019/1/18
 * 设备服务信息主动下发业务处理类
 */
@Service
@Slf4j
public class ActiveIssuedService {
    @Autowired
    DeviceRemoteConfigRecordService remoteConfigRecordService;
    @Autowired
    private WebsocketSendMessageService websocketSendMessageService;
    @Autowired
    private IDeviceUpgradeRecordService deviceUpgradeRecordService;
    @Autowired
    private IDeviceInfoService deviceInfoService;
    @Autowired
    private NettyUtil nettyUtil;

    /**
     * 处理远程配置下发
     *
     * @param deviceRemoteConfig 队列中监听到的消息
     */
    public ResultVO handleConfig(DeviceRemoteConfig deviceRemoteConfig) {
        //DeviceRemoteConfig deviceRemoteConfig = JSONObject.parseObject(message, DeviceRemoteConfig.class);
        QueryWhereDto queryWhereDto = new QueryWhereDto();
        queryWhereDto.setId(deviceRemoteConfig.getId());
        //获取此次下发配置受影响的设备
        List<DeviceRemoteConfigRecordVO> deviceConfigList = remoteConfigRecordService.getDeviceConfigHistory(queryWhereDto);

        List<DeviceRemoteConfigRecord> recordIdList = new ArrayList<>();
        for (DeviceRemoteConfigRecordVO vo : deviceConfigList) {
            String localKey = vo.getProductKey() + "." + vo.getDeviceNo();
            Channel channel = IotHubHandler.nettyChannels.get(localKey);
            //如果设备不在线  直接忽略
            if (channel == null) {
                continue;
            }
            Map<Object, Object> map = new HashMap<>(4);
            map.put("method", "config");
            map.put("uuid", System.currentTimeMillis());
            map.put("version", "1.0");
            map.put("params", JSONObject.parseObject(deviceRemoteConfig.getJsonString()));
            nettyUtil.send(JSON.toJSONString(map), channel,true);

            DeviceRemoteConfigRecord deviceRemoteConfigRecord = new DeviceRemoteConfigRecord();
            deviceRemoteConfigRecord.setId(vo.getId());
            deviceRemoteConfigRecord.setIssuedFlag(1);
            recordIdList.add(deviceRemoteConfigRecord);
        }
        //更新数据下发状态
        if (recordIdList.size() > 0) {
            remoteConfigRecordService.updateBatchById(recordIdList);
        }
        return new ResultVO("成功",200, null);
    }


    /**
     * 在线调试
     *
     * @param onlineDebug
     */
    public ResultVO onlineDebug(OnlineDebug onlineDebug) {
        String localKey = onlineDebug.getProductKey() + "." + onlineDebug.getDeviceNo();
        Channel channel = IotHubHandler.nettyChannels.get(localKey);
        //如果设备不在线  直接忽略
        if (channel == null) {
            return new ResultVO(ResultCode.DEVICE_OFF_LINE, null);
        }
        Map<Object, Object> map = new HashMap<>(4);
        //功能定义:1 方法,2配置
        map.put("method", onlineDebug.getFunctionId());
        map.put("uuid", System.currentTimeMillis());
        map.put("version", "1.0");
        map.put("params", JSONObject.parseObject(onlineDebug.getJsonString()));
        nettyUtil.send(JSON.toJSONString(map), channel,true);

       // websocketSendMessageService.sendWebsocket(JSONUtil.toJsonStr(map).replaceAll("\"", ""), channel);
        return new ResultVO("成功",200, null);
    }


    /**
     * 固件升级
     *
     * @param upgradeData 升级包信息
     */
    public ResultVO deviceUpgrade(DeviceUpgradePackage upgradeData) {
        if (upgradeData == null || upgradeData.getId() == null) {
            return new ResultVO(ResultCode.SUCCESS, null);
        }
        DeviceUpgradeRecord deviceUpgradeRecord = new DeviceUpgradeRecord();
        deviceUpgradeRecord.setPackageId(upgradeData.getId());
        QueryWhereDto<DeviceUpgradeRecord> dto = new QueryWhereDto<>();
        dto.setBody(deviceUpgradeRecord);
        //根据升级包id 获取设备升级履历信息
        List<DeviceUpgradeRecordData> recordDataList = deviceUpgradeRecordService.getDeviceUpgradeRecordList(dto);

        List<DeviceUpgradeRecord> toDbDeviceUpgradeRecordList = new ArrayList<>();
        for (DeviceUpgradeRecordData vo : recordDataList) {
            if(3==vo.getIssuedFlag()||2==vo.getIssuedFlag()){
                continue;
            }
            String localKey = vo.getProductKey() + "." + vo.getDeviceNo();
            Channel channel = IotHubHandler.nettyChannels.get(localKey);
            //如果设备不在线  直接忽略
            if (channel == null) {
                log.info("设备不在线 {}",localKey);
                continue;
            }
            Map<Object, Object> mapPath = new HashMap<>(1);
            mapPath.put("id", vo.getId());
            mapPath.put("path", vo.getPath());
            mapPath.put("version", vo.getVersion());
            Map<Object, Object> map = new HashMap<>(4);
            map.put("method", "upgrade");
            map.put("uuid", System.currentTimeMillis());
            map.put("version", "1.0");
            map.put("params", mapPath);
            nettyUtil.send(JSON.toJSONString(map), channel,true);

            DeviceUpgradeRecord upgradeRecord = new DeviceUpgradeRecord();
            upgradeRecord.setId(vo.getId());
            upgradeRecord.setIssuedFlag(1);
            toDbDeviceUpgradeRecordList.add(upgradeRecord);
        }

        //更新已经下发了的标识为已下发(1)
        if (toDbDeviceUpgradeRecordList.size() > 0) {
            deviceUpgradeRecordService.updateBatchById(toDbDeviceUpgradeRecordList);
        }
        return new ResultVO("成功",200, null);
    }


    /**
     * 处理离线时操作的信息(如:远程配置,升级)  设备上线后主动下发
     */
    public void handleOfflineFunction(ChannelHandlerContext ctx) {
        handleConfig(ctx);
        handleFirmwareUpdate(ctx);
    }

    /**
     * 处理 离线远程配置
     */
    private void handleConfig(ChannelHandlerContext ctx) {
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        DeviceInfo deviceInfoDb = deviceInfoService.selectCacheOne(currSession.getPk() + ":" + currSession.getDn());
        EntityWrapper<DeviceRemoteConfigRecord> ew = new EntityWrapper<>();
        ew.eq("device_id", deviceInfoDb.getId());
        ew.eq("product_id", deviceInfoDb.getProductId());
        ew.eq("issued_flag", 0);
        ew.eq("is_del", 0);
        ew.orderBy("create_time", true);
        List<DeviceRemoteConfigRecord> listDb = remoteConfigRecordService.selectList(ew);
        List<DeviceRemoteConfigRecord> recordIdList = new ArrayList<>();
        for (DeviceRemoteConfigRecord configRecord : listDb) {
            Map<Object, Object> map = new HashMap<>(4);
            map.put("method", "config");
            map.put("uuid", System.currentTimeMillis());
            map.put("version", "1.0");
            map.put("params", JSONObject.parseObject(configRecord.getJsonString()));
            nettyUtil.send(JSON.toJSONString(map), ctx.channel(),true);
            DeviceRemoteConfigRecord deviceRemoteConfigRecord = new DeviceRemoteConfigRecord();
            deviceRemoteConfigRecord.setId(configRecord.getId());
            deviceRemoteConfigRecord.setIssuedFlag(1);
            recordIdList.add(deviceRemoteConfigRecord);
        }
        //更新数据下发状态
        if (recordIdList.size() > 0) {
            remoteConfigRecordService.updateBatchById(recordIdList);
        }
    }

    /**
     * 处理离线固件升级
     */
    private void handleFirmwareUpdate(ChannelHandlerContext ctx) {
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        DeviceInfo deviceInfoDb = deviceInfoService.selectCacheOne(currSession.getPk() + ":" + currSession.getDn());
        EntityWrapper<DeviceUpgradeRecord> ew = new EntityWrapper<>();
        ew.eq("a.device_id", deviceInfoDb.getId());
        ew.eq("a.product_id", deviceInfoDb.getProductId());
        ew.eq("a.issued_flag", 0);
        ew.eq("a.is_del", 0);
        ew.orderBy("a.create_time", true);
        List<DeviceUpgradeRecordData> dbList = deviceUpgradeRecordService.getDeviceUpgradeRecordList(ew);


        List<DeviceUpgradeRecord> toDbDeviceUpgradeRecordList = new ArrayList<>();
        for (DeviceUpgradeRecordData vo : dbList) {
            Map<Object, Object> mapPath = new HashMap<>(1);
            mapPath.put("id", vo.getId());
            mapPath.put("path", vo.getPath());
            mapPath.put("version", vo.getVersion());
            Map<Object, Object> map = new HashMap<>(4);
            map.put("method", "upgrade");
            map.put("uuid", System.currentTimeMillis());
            map.put("version", "1.0");
            map.put("params", mapPath);
            nettyUtil.send(JSON.toJSONString(map), ctx.channel(),true);

            DeviceUpgradeRecord upgradeRecord = new DeviceUpgradeRecord();
            upgradeRecord.setId(vo.getId());
            upgradeRecord.setIssuedFlag(1);
            toDbDeviceUpgradeRecordList.add(upgradeRecord);
        }

        //更新已经下发了的标识为已下发(1)
        if (toDbDeviceUpgradeRecordList.size() > 0) {
            deviceUpgradeRecordService.updateBatchById(toDbDeviceUpgradeRecordList);
        }
    }



    /**
     * 重启
     *
     * @param deviceNoList
     */
    public ResultVO rebootDevice(List<DeviceInfoVo> deviceNoList) {
        for(DeviceInfoVo deviceInfoVo:deviceNoList){
            String localKey = deviceInfoVo.getProductKey() + "." + deviceInfoVo.getDeviceNo();
            Channel channel = IotHubHandler.nettyChannels.get(localKey);
            if(channel==null){
                continue;
            }
            Map<Object, Object> map = new HashMap<>(4);
            //功能定义:1 方法,2配置
            map.put("method", "reboot");
            map.put("version", "1.0");
            nettyUtil.send(JSON.toJSONString(map), channel,true);
        }
        return new ResultVO("成功",200, null);
    }

}

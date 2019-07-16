package com.xywg.iot.modular.iothub.controller;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.iothub.netty.handler.IotHubHandler;
import com.xywg.iot.modular.iothub.server.ActiveIssuedService;
import com.xywg.iot.modular.iothub.server.DeviceServer;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfig;
import com.xywg.iot.modular.remote.model.OnlineDebug;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.vo.ResultVO;
import com.xywg.log.annotations.OpenLog;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:32 2018/12/11
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/internalCommunication")
@Api
public class NettyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyController.class);
    @Autowired
    private ActiveIssuedService remoteConfigService;



    private DeviceServer  deviceServer;
    @Autowired
    public void setDeviceServer(DeviceServer deviceServer) {
        this.deviceServer = deviceServer;
    }



    @PostMapping("/{pk}/{dn}/method/{method}")
    @OpenLog
    public Object callMethod(@PathVariable(value="pk") String pk,@PathVariable(value = "dn") String dn,@PathVariable(value = "method") String method, @RequestBody Map<String,String> params){
        System.out.println(JSONUtil.toJsonStr(params));
        Map<String,Object> result =new HashMap<>(10);
        String localKey = pk+"."+dn;
        Channel channel =  IotHubHandler.nettyChannels.get(localKey);
        if(channel==null){
            result.put("msg","设备未上线");
            LOGGER.info("设备未上线");
            return result;
        }
        try {
            deviceServer.callMethod(pk,dn,method,params, JSONUtil.toJsonStr(result));
            result.put("msg","操作成功");
            LOGGER.info("操作成功");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            result.put("msg",e.getMessage());
        }
        // 判断设备方法是同步还是异步
        // 同步需要等待设备返回，异步直接返回调用者
        return result;
    }

    /**
     * 在线调试
     * @param onlineDebug
     * @return
     */
    @PostMapping("/onlineDebug")
    public ResultVO onlineDebug(@RequestBody OnlineDebug onlineDebug) {
        return  remoteConfigService.onlineDebug(onlineDebug);
    }

    /**
     * 在线升级
     * @param deviceUpgradePackage
     * @return
     */
    @PostMapping("/upgrade")
    public ResultVO upgrade(@RequestBody DeviceUpgradePackage deviceUpgradePackage) {
        return remoteConfigService.deviceUpgrade(deviceUpgradePackage);
    }

    /**
     * 远程配置
     * @param deviceRemoteConfig
     * @return
     */
    @PostMapping("/config")
    public ResultVO config(@RequestBody DeviceRemoteConfig deviceRemoteConfig) {
        return remoteConfigService.handleConfig(deviceRemoteConfig);
    }

    /**
     * 设备重启指令下发
     *
     * @return
     */
    @PostMapping("/rebootDevice")
    public ResultVO rebootDevice(@RequestBody DeviceUpgradePackage deviceUpgradePackage) {
            return remoteConfigService.rebootDevice(deviceUpgradePackage.getDeviceList());
    }


}

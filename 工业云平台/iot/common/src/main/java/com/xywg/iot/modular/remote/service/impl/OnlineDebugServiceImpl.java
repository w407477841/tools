package com.xywg.iot.modular.remote.service.impl;

import com.alibaba.fastjson.JSON;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.service.IProductInfoService;
import com.xywg.iot.modular.remote.model.OnlineDebug;
import com.xywg.iot.modular.remote.service.OnlineDebugService;
import com.xywg.iot.util.HttpUtils;
import com.xywg.iot.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author hjy
 * @date 2019/1/21
 */
@Service
public class OnlineDebugServiceImpl implements OnlineDebugService {
    @Value("${netty.iotNettyUrl:#{null}}")
    private String iotNettyUrl;
    @Value("${netty.iotNettyWebPort:#{null}}")
    private String iotNettyWebPort;

    @Autowired
    private IDeviceInfoService deviceInfoService;
    @Autowired
    private IProductInfoService productInfoService;


    @Override
    public ResultVO sendOrder(OnlineDebug onlineDebug) {
        ProductInfo productInfo = productInfoService.selectById(onlineDebug.getProductId());
        DeviceInfo deviceInfo = deviceInfoService.selectById(onlineDebug.getDeviceId());
        //设备状态 1：未激活 2：离线 3：在线
        if(deviceInfo.getStatus()!=3){
            return new ResultVO("设备离线或未激活,无法发送指令",400,null);
        }
        onlineDebug.setDeviceNo(deviceInfo.getDeviceNo());
        onlineDebug.setProductKey(productInfo.getProductKey());
        //rabbitMQService.amqpSendMessage(RabbitConfig.RABBITMQ_TOPIC_DEVICE_ONLINE_DEBUG,JSON.toJSONString(onlineDebug));
        HttpUtils.sendPost("http://"+iotNettyUrl+":"+iotNettyWebPort+"/internalCommunication/onlineDebug",JSON.toJSONString(onlineDebug));
        return new ResultVO("操作成功",200,null);
    }


}

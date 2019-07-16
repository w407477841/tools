package com.xywg.iot.modulers.controller;

import com.xywg.iot.netty.handler.NettyChannelManage;
import com.xywg.iot.netty.handler.MessageHandleHandler;

import io.netty.channel.Channel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:20 2019/2/27
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
public class UpgradeController {

    @GetMapping("upgrade")
    public Object upgrade(){
       /* Channel channel  = NettyChannelManage.getChannel("XY20190212020001") ;
        DeviceConnectInfo connectInfo =  NettyChannelManage. getDeviceCodeByChannel(channel);

        FilePojo filePojo =new FilePojo();

        // 下载网络文件
        int bytesum = 0;

            File file =  new File("E:/data/TC_upgrade_v1.x.x.bin");
           // FileOutputStream fs = new FileOutputStream("c:/abc.gif");
            //bytesum =   conn.getContentLength();
            bytesum = (int) file.length();

        filePojo.setLength(bytesum);
        filePojo.setUrl("E:/data/TC_upgrade_v1.x.x.bin");
        connectInfo.setFile(filePojo);
        channel.attr(NettyChannelManage.NETTY_CHANNEL_KEY).set(connectInfo);
       String data =  SendReceiveFileRequestAction.packageUpgradeData(connectInfo.getSn(),connectInfo.getStationId(),bytesum);
        MessageHandleHandler.responseMessage(channel,data);*/
        return "发送完毕";
    }

}

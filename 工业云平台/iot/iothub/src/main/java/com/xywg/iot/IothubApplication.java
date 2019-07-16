package com.xywg.iot;

import com.xywg.iot.modular.iothub.netty.config.NettyConfig;
import com.xywg.iot.modular.iothub.server.DeviceServer;
import com.xywg.iot.util.ApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IothubApplication {

    public static void main(String[] args)  {


        SpringApplication.run(IothubApplication.class, args);

        DeviceServer deviceServer  = ApplicationContextProvider.getBean(DeviceServer.class);

        NettyConfig nettyConfig =  ApplicationContextProvider.getBean(NettyConfig.class);

        try {
            deviceServer.offlines();
            System.out.println("设备全部下线");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("设备全部下线 失败");
        }
        try {
            nettyConfig.start();
            System.out.println("netty启动成功");
        } catch (InterruptedException e) {
            System.out.println("netty启动失败");
        }




    }

}


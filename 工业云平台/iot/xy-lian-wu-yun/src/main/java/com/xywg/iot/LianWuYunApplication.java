package com.xywg.iot;


import com.xywg.iot.netty.NettyServer;
import com.xywg.iot.netty.handler.CommonMethod;
import com.xywg.iot.util.ApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hjy
 */
@SpringBootApplication
public class LianWuYunApplication {

    public static void main(String[] args) {
        SpringApplication.run(LianWuYunApplication.class, args);
        try {
            try {
                ApplicationContextProvider.getBean(CommonMethod.class).offlines();
                System.out.println("设备全部下线");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("设备全部下线 失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NettyServer netty =  ApplicationContextProvider.getBean(NettyServer.class);
        netty.startNetty();
       NettyServer.threadPoolExecutor.shutdown();
    }
}

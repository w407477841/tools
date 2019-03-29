package com.wyf.iot.auto;

import com.wyf.iot.properties.NettyProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动化配置初始化服务
 *
 * @author lxr
 * @create 2017-11-29 19:52
 **/
@Configuration
@ConditionalOnClass
@EnableConfigurationProperties({NettyProperties.class})
public class ServerAutoConfigure {


    private static  final  int _BLACKLOG =   1024;

    private static final  int  CPU =Runtime.getRuntime().availableProcessors();

    private static final  int  SEDU_DAY =10;

    private static final  int TIMEOUT =120;

    private static final  int BUF_SIZE=10*1024*1024;


    public ServerAutoConfigure(){

    }




    @Bean(initMethod = "open", destroyMethod = "close")
    @ConditionalOnMissingBean
    public InitServer initServer( NettyProperties serverBean){
        if(serverBean.getPort()==0){
            throw  new NullPointerException("not set port");
        }
        if(serverBean.getBacklog()<1){
            serverBean.setBacklog(_BLACKLOG);
        }
        if(serverBean.getBossThread()<1){
            serverBean.setBossThread(CPU);
        }

        if(serverBean.getHeart()<1){
            serverBean.setHeart(TIMEOUT);
        }
        if(serverBean.getRevbuf()<1){
            serverBean.setRevbuf(BUF_SIZE);
        }
        if(serverBean.getWorkThread()<1){
            serverBean.setWorkThread(CPU*2);
        }

        return new InitServer(serverBean);
    }


}

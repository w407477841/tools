package com.xywg.tools.rpc.netty.properties;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:41 2019/2/22
 * Modified By : wangyifei
 */
@ConfigurationProperties("rpc")
@Configuration
@Data
public class RpcProperties {

    private Integer post = 8108;
    /**
     *  心跳周期
     */
    private Integer cycle = 90 ;

}

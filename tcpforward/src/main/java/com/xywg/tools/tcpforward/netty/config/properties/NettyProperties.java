package com.xywg.tools.tcpforward.netty.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:26 2018/12/11
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "netty")
@Data
public class NettyProperties {
    /**
     * tcp监听端口端口
     */
    private Integer port;

    /**
     * 超时时间
     */
    private Long  timeout;


}

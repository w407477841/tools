package com.xywg.iot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:07 2018/12/14
 * Modified By : wangyifei
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "zbus")
public class ZbusProperties {
    /**
     *  zbus 服务地址
     */
    private String host;
    /**
     *  物联网中心
     */
    private String topicIothub;
    /**
     * 云平台
     */
    private String topicCloud;





}

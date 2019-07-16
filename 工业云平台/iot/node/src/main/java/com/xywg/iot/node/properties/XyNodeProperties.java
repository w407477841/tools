package com.xywg.iot.node.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:22 2019/3/8
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = XyNodeProperties.prefix)
@Data
public class XyNodeProperties {
    public static final String prefix = "xywg.node";
    private String name ;
    private String url;


}

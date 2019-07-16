package com.xywg.iot.modular.iothub.netty.config.properties;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:54 2019/1/15
 * Modified By : wangyifei
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "baidu")
public class BaiduProperty {

    private String ak;

}

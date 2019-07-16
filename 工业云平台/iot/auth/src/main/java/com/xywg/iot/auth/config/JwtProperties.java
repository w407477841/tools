package com.xywg.iot.auth.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:44 2018/12/19
 * Modified By : wangyifei
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String tokenHeader = "xywgiot";

    private String userHeader = "user";

    private String randomHeader = "random" ;

}

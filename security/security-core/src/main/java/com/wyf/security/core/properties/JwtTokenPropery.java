package com.wyf.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:38 2019/7/18
 * Modified By : wangyifei
 */
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtTokenPropery {
    /** 过期时间 默认 2小时 */
    private Long expiration = 2*60*60*100L ;

}

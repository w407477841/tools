package com.honghu.oauth2.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "honghu.oauth2")
public class OAuthPropeties {
    /** 客户端ID */
    private String clientId;
    /** token秘钥 */
    private String secret;
    /** 服务器地址 */
    private String serverUrl;

}

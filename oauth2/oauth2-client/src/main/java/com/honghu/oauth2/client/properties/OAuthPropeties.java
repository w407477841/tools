package com.honghu.oauth2.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "hong.oauth2")
public class OAuthPropeties {
    /** 客户端首页地址 */
    private String indexUrl;
    /** 服务端登录地址 */
    private String serverUrl;

}

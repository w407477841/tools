package com.wyf.security.core;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:23 2019/7/18
 * Modified By : wangyifei
 */
public class Const {
    /** 认证键名 */
    public static final String AUTH_HEADER_NAME = "auth_token";
    /** 刷新键名 */
    public static final String AUTH_REFREASH_NAME = "refresh_token";
    /** 刷新令牌过期时间 7天 */
    public static final long  AUTH_REFREASH_EXPIRATION = 7 * 24 * 60 * 60 * 1000L ;
    /** 签名 */
    public static final String AUTH_SECRET = "xkwla" ;
}

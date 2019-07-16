package com.xywg.iot.util;

import com.xywg.iot.modular.cloud.model.CloudUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:47 2018/12/25
 * Modified By : wangyifei
 */
public class UserUtil {
    /**
     * 当前用户
     */
    public static ThreadLocal<String> USERID = new ThreadLocal<>();
    /**
     * 用户名
     */
    public static ThreadLocal<String> USERNAME = new ThreadLocal<>();

    /**
     * 管理员
     */
    public static ThreadLocal<Boolean> ISADMIN = new ThreadLocal<>();

    /**
     *  语言
     */
    public static ThreadLocal<Locale> localLocale= new ThreadLocal<>();


    public static ThreadLocal<String> accessToken= new ThreadLocal<>();


}

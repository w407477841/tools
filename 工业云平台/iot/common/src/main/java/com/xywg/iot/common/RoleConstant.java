package com.xywg.iot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:58 2019/1/17
 * Modified By : wangyifei
 */
public class RoleConstant {
    /**基础权限*/
    public static final String BASE = "BASE";
    /**管理员权限*/
    public static final String ADMIN = "ADMIN";
    /**认证后权限*/
    public static final String AUTH = "AUTH";
    /**第三方权限*/
    public static final String THIRD = "THIRD";

    /**管理员级别*/
    public static final String [] LEVEL_ADMIN = {ADMIN};
    /**认证级别的权限*/
    public static final String [] LEVEL_AUTH = {ADMIN,AUTH};
    /**三方级别的权限 ， */
    public static final String [] LEVEL_THIRD = {ADMIN,AUTH,THIRD};
    /**所有都能访问*/
    public static final String [] LEVEL_ALL = {BASE,ADMIN,AUTH,THIRD};
    /**只能本系统访问*/
    public static final String [] LEVEL_PRIVATE = {ADMIN,AUTH,BASE};
    /**三方系统访问*/
    public static final String [] LEVEL_PUBLIC = {THIRD};

}

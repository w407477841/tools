package com.xywg.iot.enums;

import com.xywg.iot.util.ApplicationContextProvider;
import com.xywg.iot.util.UserUtil;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:21 2018/12/20
 * Modified By : wangyifei
 */
public enum ResultCode {
    /**成功*/
    SUCCESS("SUCCESS",200),
    /**
     *  添加成功
     */
    INSERT_SUCCESS("INSERT_SUCCESS",200),
    INSERT_ERROR("INSERT_ERROR",400),

    UPDATE_SUCCESS("UPDATE_SUCCESS",200),
    UPDATE_ERROR("UPDATE_ERROR",400),

    DELETE_SUCCESS("DELETE_SUCCESS",200),
    DELETE_ERROR("DELETE_ERROR",400),

    USE_SUCCESS("USE_SUCCESS",200),
    USE_ERROR("USE_ERROR",400),

    DIS_SUCCESS("DIS_SUCCESS",200),
    DIS_ERROR("DIS_ERROR",400),

    ACTIVE_SUCCESS("ACTIVE_SUCCESS",200),
    ACTIVE_ERROR("ACTIVE_ERROR",400),

    INPUT_SUCCESS("INPUT_SUCCESS",200),
    INPUT_ERROR("INPUT_ERROR",400),

    AGREE_SUCCESS("AGREE_SUCCESS",200),
    AGREE_ERROR("AGREE_ERROR",400),

    DISAGREE_SUCCESS("DISAGREE_SUCCESS",200),
    DISAGREE_ERROR("DISAGREE_ERROR",400),

    TYPE_NAME_REPEAT("TYPE_NAME_REPEAT",2001),

    OPERATE_NAME_REPEAT("OPERATE_NAME_REPEAT",2002),

    DEVICE_NO_REPEAT("DEVICE_NO_REPEAT",2003),

    DEVICE_ALREADY_EXIST("DEVICE_ALREADY_EXIST",2004),

    FUNCTION_KEY_REPEAT("FUNCTION_KEY_REPEAT",2005),

    PRODUCT_NAME_REPEAT("PRODUCT_NAME_REPEAT",2006),

    APP_NAME_REPEAT("APP_NAME_REPEAT",2007),

    EXCEL_DEVICE_NO_REPEAT("EXCEL_DEVICE_NO_REPEAT",2008),

    CHILDREN_IS_EXIST("CHILDREN_IS_EXIST",2009),


    ERROR("ERROR",400),
    /**登录成功*/
    LOGIN_SUCCESS("LOGIN_SUCCESS",200),

    /**TOKEN刷新成功*/
    TOKEN_REFLESH_SUCCESS("TOKEN_REFLESH_SUCCESS",200),
    /**认证失败*/
    AUTH_ERROR("AUTH_ERROR",401),
    /**认证失败*/
    NO_USER("NO_USER",4011),
    /**认证过期*/
    NON_EXPIRED("NON_EXPIRED",4012),
    /**用户被锁定*/
    LOCKED("LOCKED",4013),
    /**用户名/密码错误*/
    NO_AUTH("NO_AUTH",4014),
    /**没有权限*/
    NO_PERMISSION("NO_PERMISSION",403),
    /**TOKEN刷新失败*/
    TOKEN_REFLESH_ERROR("TOKEN_REFLESH_ERROR",402),
    /** 其他异常 */
    SYS_ERROR("SYS_ERROR",500),

    /**
     * 基本信息未填写
     */
    BASIC_INFORMATION_IS_NOT_FILLED_IN("BASIC_INFORMATION_IS_NOT_FILLED_IN",400),
    /**
     * 企业基础信息已存在
     */
    BASIC_ENTERPRISE_INFORMATION_ALREADY_EXISTS("BASIC_ENTERPRISE_INFORMATION_ALREADY_EXISTS",400),
    /**
     * 名称已存在
     *
     */
    NAME_ALREADY_EXISTS("NAME_ALREADY_EXISTS",400),

    /**
     * 联网方式已存在
     *
     */
    LINK_TYPE_EXISTS("LINK_TYPE_EXISTS",400),
    /**
     * 重复提交
     */
    RESUBMIT("RESUBMIT",400),
    /**
     *
     */
    DEVICE_OFF_LINE("DEVICE_OFF_LINE",400),

    /**
     * 非法操作!!
     *
     */
    ILLEGAL("ILLEGAL",400)
    ;

    private String msg;
    private Integer code;

    ResultCode(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return ApplicationContextProvider.getMessageSource().getMessage(msg,null,UserUtil.localLocale.get());
    }

    public Integer getCode() {
        return code;
    }
}

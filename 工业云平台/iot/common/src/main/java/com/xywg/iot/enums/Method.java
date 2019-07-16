package com.xywg.iot.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:32 2018/12/18
 * Modified By : wangyifei
 */
public enum  Method {
    /**
     *  登录
     */
    LOGIN("LOGIN"),
    /**
     * 属性
     */
    PROPERTY("PROPERTY"),
    /**
     * 方法
     */
    METHOD("METHOD"),
    /**
     * 事件
     */
    EVENT("EVENT")
    ;

    Method(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value.toLowerCase();
    }
}

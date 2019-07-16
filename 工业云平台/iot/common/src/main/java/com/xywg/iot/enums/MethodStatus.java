package com.xywg.iot.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:39 2018/12/19
 * Modified By : wangyifei
 */

public enum MethodStatus {
    /**
     *  未返回
     */
    NO_RETURN(0),
    /**
     * 已返回
     */
    RETURN(1),
    /**
     * 超时返回
     */
    TIMEOUT(2),
    ;

    private int status;

    MethodStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

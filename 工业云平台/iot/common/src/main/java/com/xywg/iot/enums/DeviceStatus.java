package com.xywg.iot.enums;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:36 2018/12/14
 * Modified By : wangyifei
 */
public enum DeviceStatus {
    /**
     * 未激活
     */
    INACTIVE(1),
    /**
     * 下线
     */
        OFFLINE(2),
    /**
     * 在线
     */
        ONLINE(3),
    ;

    private Integer  status;

    DeviceStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}

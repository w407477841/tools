package com.xywg.iot.enums;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:32 2018/12/14
 * Modified By : wangyifei
 */
public enum DeviceLoginStatus {
    /**成功*/
    SUCCESS("1"),
    /**失败*/
    ERROR("0"),
    /**重复*/
    REPEAT("2")


    ;

    private String status;

    DeviceLoginStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

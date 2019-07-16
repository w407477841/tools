package com.xywg.iot.modular.remote.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/1/21
 */
@Data
public class OnlineDebugVirtualDevice {
    private Integer productId;

    private Integer deviceId;

    private String productKey;

    private String deviceNo;

    private String jsonString;

    /**'
     * 虚拟设备开启时的,数据上报的类型(login,property,event)
     */
    private String functionVirtual;
}

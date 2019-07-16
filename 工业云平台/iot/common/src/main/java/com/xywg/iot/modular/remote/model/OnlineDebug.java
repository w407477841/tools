package com.xywg.iot.modular.remote.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/1/21
 */
@Data
public class OnlineDebug {
    private Integer productId;

    private Integer deviceId;

    private String productKey;

    private String deviceNo;

    private String jsonString;

    /**
     * 功能定义:1 方法,2配置
     */
    private String functionId;

    /**'
     * 虚拟设备开启时的,数据上报的类型(login,property,event)
     */
    private String functionVirtual;
}

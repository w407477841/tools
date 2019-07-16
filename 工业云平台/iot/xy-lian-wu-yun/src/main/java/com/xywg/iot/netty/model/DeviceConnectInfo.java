package com.xywg.iot.netty.model;

import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.product.model.ProductInfo;
import lombok.Data;

/**
 * @author hjy
 * @date 2019/3/7
 */
@Data
public class DeviceConnectInfo {
    private String uuid;

    private String sn;

    private DeviceInfo deviceInfo;

    private ProductInfo productInfo;

    public DeviceConnectInfo(String uuid, String sn) {
        this.uuid = uuid;
        this.sn = sn;
    }

    public DeviceConnectInfo() {
    }
}

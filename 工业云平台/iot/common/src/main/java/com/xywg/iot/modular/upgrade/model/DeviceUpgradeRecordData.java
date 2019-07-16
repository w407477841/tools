package com.xywg.iot.modular.upgrade.model;

import lombok.Data;

/**
 * @author hjy
 * @since 2019-01-04
 */
@Data
public class DeviceUpgradeRecordData {

    private static final long serialVersionUID = 1L;

    /**
     * 升级履历id
     */
    private Integer id;

    private Integer productId;

    private Integer packageId;

    private String productKey;

    private Integer deviceId;

    private Integer issuedFlag;

    private String deviceNo;
    /**
     * 版本
     */
    private String version;

    /**
     * 升级包地址
     */
    private String path;

}

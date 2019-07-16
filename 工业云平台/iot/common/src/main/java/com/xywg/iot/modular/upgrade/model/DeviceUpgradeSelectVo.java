package com.xywg.iot.modular.upgrade.model;

import com.xywg.iot.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hjy
 * @since 2019-01-04
 */
@Data
public class DeviceUpgradeSelectVo extends BaseEntity<DeviceUpgradeSelectVo> {
    /**
     * 自增
     */
	private Integer id;
    /**
     * 升级包名称
     */
	private String name;
    /**
     * 产品名称
     */
	private Integer productId;
    /**
     * 设备版本
     */
	private String deviceVersion;

	/**
	 * 产品版本
	 */
	private String productVersion;
    /**
     * 状态
     */
	private Integer status;

	private String statusVal;
    /**
     * 升级包地址
     */
	private String path;

    /**
     * 备注
     */
	private String comments;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}

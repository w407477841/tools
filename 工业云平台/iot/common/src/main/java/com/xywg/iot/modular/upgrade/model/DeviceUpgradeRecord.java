package com.xywg.iot.modular.upgrade.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.iot.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @author hjy
 * @since 2019-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_upgrade_record")
public class DeviceUpgradeRecord extends BaseEntity<DeviceUpgradeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	private Integer id;
    /**
     * 设备名称
     */
	@TableField("device_id")
	private Integer deviceId;
    /**
     * 产品名称
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 升级包ID
     */
	@TableField("package_id")
	private Integer packageId;
    /**
     * 下发标识
     */
	@TableField("issued_flag")
	private Integer issuedFlag;

    /**
     * 备注
     */
	private String comments;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

package com.xywg.iot.modular.upgrade.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.iot.base.BaseEntity;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author hjy
 * @since 2019-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_upgrade_package")
public class DeviceUpgradePackage extends BaseEntity<DeviceUpgradePackage> {

    private static final long serialVersionUID = 1L;

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
	@TableField("product_id")
	private Integer productId;
    /**
     * 版本
     */
	private String version;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 升级包地址
     */
	private String path;
	/**
	 * 文件名称
	 */
	@TableField("file_name")
	private String fileName;


    /**
     * 备注
     */
	private String comments;


	@TableField(exist = false)
	private String productName;

	@TableField(exist = false)
	private List<DeviceInfoVo> deviceList;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

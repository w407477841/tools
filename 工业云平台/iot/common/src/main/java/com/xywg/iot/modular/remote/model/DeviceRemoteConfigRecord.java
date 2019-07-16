package com.xywg.iot.modular.remote.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.iot.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author hjy
 * @since 2019-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_remote_config_record")
public class DeviceRemoteConfigRecord extends BaseEntity<DeviceRemoteConfigRecord> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 所属产品id
     */
	@TableField("product_id")
	private Integer productId;

	@TableField("config_id")
	private Integer configId;

    /**
     * 设备id
     */
	@TableField("device_id")
	private Integer deviceId;
    /**
     * 配置的json字符串
     */
	@TableField("json_string")
	private String jsonString;

	/**
	 * 下发标识
	 */
	@TableField("issued_flag")
	private Integer issuedFlag;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

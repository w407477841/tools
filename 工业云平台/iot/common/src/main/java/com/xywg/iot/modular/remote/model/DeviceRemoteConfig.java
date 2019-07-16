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
 * <p>
 * 
 * </p>
 *
 * @author z
 * @since 2019-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_remote_config")
public class DeviceRemoteConfig extends BaseEntity<DeviceRemoteConfig> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 所属产品id
     */
	@TableField("product_id")
	private Integer productId;

    /**
     * 配置的json字符串
     */
	@TableField("json_string")
	private String jsonString;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

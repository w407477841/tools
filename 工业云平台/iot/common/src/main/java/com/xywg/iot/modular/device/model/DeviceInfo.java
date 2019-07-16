package com.xywg.iot.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xywg.iot.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author jxj
 * @since 2019-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_info")
public class DeviceInfo extends BaseModel<DeviceInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 名字
     */
	private String name;
	/**
	 * 设备编号
	 */
	@TableField("device_no")
	private String deviceNo;
    /**
     * 产品id
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 设备密钥
     */
	@TableField("device_secret")
	private String deviceSecret;
    /**
     * 设备状态 1：未激活 2：离线 3：在线
     */
	private Integer status;
    /**
     * ip地址
     */
	private String ip;
    /**
     * 固件版本
     */
	private String version;
    /**
     * 添加日期
     */
	@TableField("add_time")
	private Date addTime;
    /**
     * 激活日期
     */
	@TableField("activation_time")
	private Date activationTime;
	/**
	 * 激活日期
	 */
	private String position;
	/**
	 *  地址  省|市
	 */
	private String address ;
    /**
     * 备注
     */
	private String comments;
	/**
	 * 最后上线时间
	 */
	@TableField(value = "last_time")
	private Date lastTime;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

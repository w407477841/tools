package com.xywg.iot.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xywg.iot.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangyf
 * @since 2019-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_property_record")
public class DevicePropertyRecord extends BaseModel<DevicePropertyRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 采集时间
     */
	@TableField("collect_time")
	private Date collectTime;
    /**
     * 设备id
     */
	@TableField("device_id")
	private Integer deviceId;
	/**
	 * 备注
	 */
	private String comments;

	/**
	 * 值
	 */
	@TableField("data_detail")
	private String dataDetail;

	/**
	 * 值
	 */
	@TableField("alarm_detail")
	private String alarmDetail;




	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public DevicePropertyRecord() {
	}

	public DevicePropertyRecord(Date collectTime, Integer deviceId) {
		this.collectTime = collectTime;
		this.deviceId = deviceId;
	}
}

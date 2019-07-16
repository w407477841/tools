package com.xywg.iot.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
@TableName("t_device_method_record")
@Data
public class DeviceMethodRecord extends Model<DeviceMethodRecord> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 方法id
     */
	@TableField("method_id")
	private Integer methodId;
    /**
     * 调用时间
     */
	@TableField("call_time")
	private Date callTime;
    /**
     * 设备id
     */
	@TableField("device_id")
	private Integer deviceId;
	@TableField("in_params")
	private String inParams;
	@TableField("out_params")
	private String outParams;

	/**
	 * 返回时间
	 */
	@TableField("receive_time")
	private Date receiveTime;
	/**
	 * 状态
	 */
	@TableField("status")
	private Integer status;
	/**
	 * 备注
	 */
	private String comments;
	@TableLogic
	@TableField(value = "is_del",fill = FieldFill.INSERT)
	private Integer isDel;
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField(value = "create_user",fill = FieldFill.INSERT)
	private String createUser;
	/**
	 * 创建人姓名
	 */
	@TableField(value = "create_user_name",fill = FieldFill.INSERT)
	private String createUserName;
	/**
	 * 更新时间
	 */
	@TableField(value = "modify_time",fill = FieldFill.UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date modifyTime;
	/**
	 * 更新人
	 */
	@TableField(value = "modify_user",fill = FieldFill.UPDATE)
	private String modifyUser;
	/**
	 * 更新人姓名
	 */
	@TableField(value = "modify_user_name",fill = FieldFill.UPDATE)
	private String modifyUserName;
	public DeviceMethodRecord() {
	}

	public DeviceMethodRecord(Integer methodId, Date callTime, Integer deviceId) {
		this.methodId = methodId;
		this.callTime = callTime;
		this.deviceId = deviceId;
		this.status = 0;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MethodDetail{" +
			"id=" + id +
			", methodId=" + methodId +
			", callTime=" + callTime +
			", deviceId=" + deviceId +
			"}";
	}
}

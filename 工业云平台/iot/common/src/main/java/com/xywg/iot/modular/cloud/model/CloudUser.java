package com.xywg.iot.modular.cloud.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

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
 * @since 2018-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_cloud_user")
public class CloudUser extends Model<CloudUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户名(不能全为数字不能有特殊字符)
     */
	private String username;
    /**
     * 手机号
     */
	private String phone;
	private String password;
    /**
     * 密钥(解密token)
     */
	@TableField("secret_key")
	private String secretKey;
    /**
     * 创建时间
     */
	@TableField(value="create_time",fill = FieldFill.INSERT)
	private Date createTime;

	@TableLogic
	@TableField(value = "is_del",fill = FieldFill.INSERT)
	private Integer isDel;

	@TableField("active_time")
	private Date activeTime;
    /**
     * 激活状态(0 未激活  ; 1 已激活)
     */
	@TableField("active_status")
	private Integer activeStatus;
    /**
     * 认证状态（0未认证  1已认证）
     */
	@TableField("auth_status")
	private Integer authStatus;
    /**
     * 登录时间
     */
	@TableField("login_time")
	private Date loginTime;
    /**
     * 用户状态(0 正常 1锁定)
     */
	private Integer status;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

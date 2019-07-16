package com.xywg.iot.modular.system.model;

import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;


import com.xywg.iot.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author hjy
 * @since 2019-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * ID雪花算法
     */
	private String id;
    /**
     * 创建人
     */
	@TableField("create_by")
	private String createBy;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 更新人
     */
	@TableField("update_by")
	private String updateBy;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 地址
     */
	private String address;
    /**
     * 头像路径
     */
	private String avatar;
    /**
     * 描述
     */
	private String description;
    /**
     * 邮件
     */
	private String email;
    /**
     * 手机号码
     */
	private String mobile;
    /**
     * 昵称
     */
	@TableField("nick_name")
	private String nickName;
    /**
     * 密码
     */
	private String password;
    /**
     * 性别
     */
	private Integer sex;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 类型
     */
	private Integer type;
    /**
     * 账号
     */
	private String username;
    /**
     * 删除标志，0：未删除，1：删除
     */
	@TableField("del_flag")
	@TableLogic
	private Integer delFlag;
    /**
     * 部门ID
     */
	@TableField("dept_id")
	private String deptId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

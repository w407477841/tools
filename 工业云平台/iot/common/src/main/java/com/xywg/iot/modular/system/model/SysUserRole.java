package com.xywg.iot.modular.system.model;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import com.baomidou.mybatisplus.annotations.Version;

import com.xywg.iot.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author jxj
 * @since 2019-01-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user_role")
public class SysUserRole extends Model<SysUserRole> {

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
     * 删除标志：0：未删除，1：删除
     */
	@TableField("del_flag")
	private Integer delFlag;
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
     * 角色ID
     */
	@TableField("role_id")
	private String roleId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

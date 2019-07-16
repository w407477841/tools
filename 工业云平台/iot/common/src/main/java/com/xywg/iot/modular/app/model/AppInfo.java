package com.xywg.iot.modular.app.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import com.baomidou.mybatisplus.annotations.Version;

import com.xywg.iot.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jxj
 * @since 2019-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_app_info")
public class AppInfo extends BaseModel<AppInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 应用名称
     */
	@TableField("app_name")
	private String appName;
    /**
     * 应用标识符
     */
	@TableField("app_key")
	private String appKey;
    /**
     * 授权密钥
     */
	@TableField("access_secret")
	private String accessSecret;
	/**
	 * 备注
	 */
	private String comments;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

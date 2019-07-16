package com.xywg.iot.modular.device.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.iot.base.BaseModel;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author z
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_config_file")
public class DeviceConfigFile extends BaseModel<DeviceConfigFile> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	@TableId(value="id", type= IdType.AUTO)
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
     * 文件名
     */
	@TableField("file_name")
	private String fileName;
    /**
     * 备注
     */
	private String comments;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

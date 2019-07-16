package com.xywg.iot.modular.dictionary.model;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


import com.baomidou.mybatisplus.enums.FieldFill;
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
 * @since 2019-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_master_method")
public class MasterMethod extends BaseModel<MasterMethod> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	private Integer id;
    /**
     * 产品类型
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 名字
     */
	private String name;
    /**
     * 标识符
     */
	private String key;
    /**
     * 是否同步 1:同步 2：异步
     */
	@TableField("is_sync")
	private Integer isSync;

    /**
     * 备注
     */
	private String comments;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

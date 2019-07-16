package com.xywg.iot.modular.dictionary.model;

import java.io.Serializable;

import java.util.Date;
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
 * @since 2019-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_master_measure_unit")
public class MasterMeasureUnit extends BaseModel<MasterMeasureUnit> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	private Integer id;
    /**
     * 名字
     */
	private String name;
    /**
     * 备注
     */
	private String comments;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

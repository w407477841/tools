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
@TableName("t_master_method_param")
public class MasterMethodParam extends BaseModel<MasterMethodParam> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	private Integer id;
    /**
     * 方法id
     */
	@TableField("method_id")
	private Integer methodId;
    /**
     * 参数类型 1：输入 2:输出
     */
	@TableField("param_type")
	private Integer paramType;
    /**
     * 名字
     */
	private String name;
    /**
     * 标识符
     */
	private String key;
    /**
     * 数据类型(1:int 2:float 3:double 4:bool 5:text 6:timestamps)
     */
	@TableField("data_type")
	private Integer dataType;
    /**
     * 最小值
     */
	private String min;
    /**
     * 最大值
     */
	private String max;
    /**
     * 步长 
     */
	private String step;
    /**
     * false对用的含义
     */
	@TableField("bool_false")
	private String boolFalse;
    /**
     * true对用的含义
     */
	@TableField("bool_true")
	private String boolTrue;
    /**
     * 数据长度
     */
	@TableField("data_length")
	private String dataLength;
    /**
     * 单位
     */
	private Integer unit;

    /**
     * 备注
     */
	private String comments;





	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

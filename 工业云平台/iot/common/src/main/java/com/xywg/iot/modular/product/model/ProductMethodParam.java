package com.xywg.iot.modular.product.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xywg.iot.base.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
@TableName("t_product_method_param")
@Data
public class ProductMethodParam extends BaseModel<ProductMethodParam> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 属性名称
     */
	private String name;
    /**
     * 标识符(json对应的key)
     */
	private String key;
    /**
     * 数据类型
  1:int32
  2:float
  3:double
  4:bool
  5:text
  6:date

     */
	@TableField("data_type")
	private Integer dataType;
    /**
     * 最小值
  为int32时 取值为整形
  为float/double 取值可为浮点型
     */
	private String min;
    /**
     * 最大值
     */
	private String max;
    /**
     * 步长
  当为int32时 步长为整形
  当为float/double时 步长为浮点型
     */
	private String step;
    /**
     * bool0代表的含义
     */
	@TableField("bool_false")
	private String boolFalse;
    /**
     * bool1代表的含义
     */
	@TableField("bool_true")
	private String boolTrue;
    /**
     * 数据长度
  当选择text类型时必填
     */
	@TableField("data_length")
	private String dataLength;
    /**
     * 参数类型
  0:入参
  1:出参
     */
	@TableField("param_type")
	private Integer paramType;
    /**
     * 产品方法id
     */
	@TableField("method_id")
	private Integer methodId;
    /**
     * 描述
     */
	private String comments;
    /**
     * 单位
     */
	private Integer unit;
	/**
	 * 创建日期
	 */
	@TableField(value = "create_time",fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField(value = "create_user",fill = FieldFill.INSERT)
	private String createUser;
	/**
	 * 创建人
	 */
	@TableField(value = "create_user_name",fill = FieldFill.INSERT)
	private String createUserName;
	/**
	 * 修改日期
	 */
	@TableField(value = "modify_time",fill = FieldFill.UPDATE)
	private Date modifyTime;
	/**
	 * 修改人
	 */
	@TableField(value = "modify_user",fill = FieldFill.UPDATE)
	private String modifyUser;
	/**
	 * 修改人
	 */
	@TableField(value = "modify_user_name",fill = FieldFill.UPDATE)
	private String modifyUserName;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}

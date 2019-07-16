package com.xywg.iot.modular.product.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xywg.iot.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
@EqualsAndHashCode(callSuper = true)
@TableName("t_product_method")
@Data
public class ProductMethod extends BaseModel<ProductMethod> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 方法名称
     */
	private String name;
    /**
     * 标识符（json对应的key）
     */
	private String key;
    /**
     * 同步
  0：异步
  1：同步
     */
	@TableField("is_sync")
	private Integer isSync;



    /**
     * 产品id
     */
	@TableField("product_id")
	private Integer productId;

	/**
	 * 备注
	 */
	private String comments;
	/**
	 * 创建时间
	 */
	@TableField(value="create_time",fill = FieldFill.INSERT)
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
	@TableField(value="modify_time",fill=FieldFill.UPDATE)
	private Date modifyTime;
	/**
	 * 更新人
	 */
	@TableField(value = "modify_user",fill=FieldFill.UPDATE)
	private String modifyUser;
	/**
	 * 更新人姓名
	 */
	@TableField(value = "modify_user_name",fill=FieldFill.UPDATE)
	private String modifyUserName;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

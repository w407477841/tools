package com.xywg.iot.modular.product.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
@TableName("t_product_info")
@Data
public class ProductInfo extends BaseModel<ProductInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 产品名
     */
	private String name;
    /**
     * 分类
     */
	private Integer type;
    /**
     * 产品唯一编号(由服务端生成)
     */
	@TableField("product_key")
	private String productKey;
    /**
     * 产品密钥(随机生成)
     */
	@TableField("product_secret")
	private String productSecret;
    /**
     * 动态注册
     */
	@TableField("dynamic_registration")
	private Integer dynamicRegistration;
    /**
     * 产品状态(0 开发中   1已发布  )
     */
	@TableField("status")
	private Integer status;
	/**
	 * 版本
	 */
	private String version;
    /**
     * 创建时间
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
     * 修改时间
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
    /**
     * 产品描述
     */
	private String comments;
    /**
     * 节点类型 0 设备 1 网关
     */
	@TableField("node_type")
	private Integer nodeType;
    /**
     * 联网方式
     */
	@TableField("link_type")
	private Integer linkType;

	@TableField("reader_idle_time")
	private Long  readerIdleTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}


}

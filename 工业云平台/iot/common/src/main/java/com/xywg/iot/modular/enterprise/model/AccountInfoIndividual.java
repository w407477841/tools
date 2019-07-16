package com.xywg.iot.modular.enterprise.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.iot.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 个人用户基本信息
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_account_info_individual")
public class AccountInfoIndividual extends BaseEntity<AccountInfoIndividual> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 会员身份
     */
	private Integer type;
    /**
     * 真实姓名
     */
	private String name;
	/**
	 * 职务
	 */
	private String staff;

    /**
     * 行业应用
     */
	@TableField("product_type")
	private Integer productType;
    /**
     * 主营业务
     */
	@TableField("major_business")
	private String majorBusiness;
	/**
	 * 身份证号码
	 */
	@TableField("identity_no")
	private String identityNo;
	/**
	 * 身份证图片地址  正面地址在前,反面地址在后,以逗号分隔
	 */
	@TableField("identity_photo")
	private String identityPhoto;


	/**
     * 省份
     */
	private Integer province;
    /**
     * 城市
     */
	private Integer city;
    /**
     * 区
     */
	private Integer district;
    /**
     * 街道
     */
	private String street;
    /**
     * 联系电话
     */
	private String tel;
    /**
     * 传真
     */
	private String fax;
	/**
	 * 邮箱
	 */
	private String email;

    /**
     * 备注
     */
	private String comments;
	/**
	 * 来源
	 */
	private Integer source;

	/**
	 * 审核状态
	 */
	@TableField("audit_status")
	private Integer auditStatus;

	/**
	 * 所属用户
	 */
	@TableField("user_id")
	private String userId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

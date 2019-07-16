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
 * 公司基本信息
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_account_info_company")
public class AccountInfoCompany extends BaseEntity<AccountInfoCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 申请人id
     */
	@TableField("individual_id")
	private Integer individualId;
    /**
     * 企业名称
     */
	private String name;
    /**
     * 企业简称
     */
	@TableField("short_name")
	private String shortName;

	/**
	 * 官网
	 */
	private String website;

    /**
     * 统一设备信用代码
     */
	@TableField("credit_code")
	private String creditCode;
    /**
     * 企业营业执照
     */
	@TableField("credit_photo")
	private String creditPhoto;
    /**
     * 企业描述
     */
	@TableField("company_description")
	private String companyDescription;
    /**
     * 年营业额
     */
	@TableField("annual_turnover")
	private String annualTurnover;
    /**
     * 软件研发团队
     */
	private Integer team;
    /**
     * 以往明星产品
     */
	@TableField("star_product")
	private String starProduct;
    /**
     * 产品名称
     */
	@TableField("product_name")
	private String productName;
    /**
     * 产品类型
     */
	@TableField("product_type")
	private Integer productType;
    /**
     * 联网方式
     */
	@TableField("link_type")
	private Integer linkType;
    /**
     * 操作系统
     */
	@TableField("operate_system")
	private Integer operateSystem;
    /**
     * 产品描述
     */
	@TableField("product_description")
	private String productDescription;
	/**
	 * 驳回理由
	 */
	@TableField("reject_reason")
	private String rejectReason;

    /**
     * 备注
     */
	private String comments;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

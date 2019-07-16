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
 * 产品描述附件
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_account_info_company_product_description")
public class AccountInfoCompanyProductDescription extends BaseEntity<AccountInfoCompanyProductDescription> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 公司id
     */
	@TableField("company_id")
	private Integer companyId;
    /**
     * 附件
     */
	private String attachment;

    /**
     * 备注
     */
	private String comments;
	/**
	 * 附件名称
	 */
	private String name;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

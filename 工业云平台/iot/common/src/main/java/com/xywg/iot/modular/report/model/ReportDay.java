package com.xywg.iot.modular.report.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
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
 * @author wangyifei
 * @since 2019-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_report_day")
public class ReportDay extends BaseModel<ReportDay> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 数据量
     */
    @TableField(value = "data_account")
	private Integer dataAccount;
    /**
     * 新增数量
     */
	@TableField(value = "add_account")
	private Integer addAccount;
	/**
	 * 累计数量
	 */
	private Integer total;
	/**
	 * 统计日期
	 */
	@TableField(value = "report_date")
	private Date reportDate;



	/**
	 * 统计日期
	 */
	@TableField(value = "report_user")
	private String reportUser;

    /**
     * 备注
     */
	private String comments;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

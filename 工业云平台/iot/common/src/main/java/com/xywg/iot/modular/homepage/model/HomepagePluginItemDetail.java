package com.xywg.iot.modular.homepage.model;

import java.io.Serializable;

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
 * @since 2018-12-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_homepage_plugin_item_detail")
public class HomepagePluginItemDetail extends BaseModel<HomepagePluginItemDetail> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 组件
     */
	@TableField("item_id")
	private Integer itemId;
    /**
     * 模板
     */
	@TableField("template_id")
	private Integer templateId;
    /**
     * 位置+大小
     */
	private String position;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

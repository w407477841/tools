package com.xywg.iot.modular.homepage.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("t_homepage_plugin_item")
public class HomepagePluginItem extends BaseModel<HomepagePluginItem> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 组件名称
     */
	private String name;
    /**
     * 编号
     */
	private String sn;
    /**
     * 初始位置
     */
	private String position;

    /**
     * 组件等级( 0-99)
0 所有用户都可以使用
1 认证后
2 只有管理员可用
     */
	private Integer level;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

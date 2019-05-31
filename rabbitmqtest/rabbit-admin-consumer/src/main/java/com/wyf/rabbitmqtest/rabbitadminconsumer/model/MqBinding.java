package com.wyf.rabbitmqtest.rabbitadminconsumer.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2019-05-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_mq_binding")
public class MqBinding extends Model<MqBinding> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 描述
     */
	private String description;
    /**
     * 交换机
     */
	private Integer exchange;
    /**
     * 队列
     */
	private Integer queue;
    /**
     * 路由规则
     */
	private String routing;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

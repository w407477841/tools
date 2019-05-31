package com.wyf.rabbitmqtest.rabbitadmin.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("t_mq_exchange")
public class MqExchange extends Model<MqExchange> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 交换机名称
     */
	private String name;
    /**
     * 交换机类型
     */
	private String type;
    /**
     * 描述
     */
	private String description;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

package com.honghu.oauth2.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wyf
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_client")
public class UserClient implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id",type =IdType.ID_WORKER )
    private Long id;

    private Long userId;

    private Long clientId;


}

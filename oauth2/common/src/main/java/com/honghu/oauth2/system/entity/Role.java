package com.honghu.oauth2.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.istack.internal.NotNull;
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
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id",type =IdType.ID_WORKER )
    private Long id;

    /**
     * 角色名
     */
    @NotNull
    private String roleName;

    /**
     * 角色编号
     */
    @NotNull
    private String roleCode;

    /**
     * 客户端
     */
    @NotNull
    private Integer clientId;


}

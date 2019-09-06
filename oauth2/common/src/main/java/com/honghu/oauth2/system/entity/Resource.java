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
@TableName("sys_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id",type =IdType.ID_WORKER )
    private Long id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源编号，用于配合前端控制页面显示
     */
    private String resourceCode;

    /**
     * 所属客户端
     */
    private Long clientId;

    /**
     * 资源类型[1:目录,2:菜单,3:按钮 ]  
     */
    private Integer resourceType;


}

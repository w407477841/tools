package com.honghu.oauth2.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.honghu.oauth2.Const;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
@TableName("sys_client")
public class Client implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id",type =IdType.ID_WORKER )
    @NotNull(groups = {Const.GroupUpdate.class})
    @Null(groups = {Const.GroupInsert.class})
    private Long id;

    /**
     * 客户端名称
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    private String clientName;

    /**
     * 客户端标识
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    private String clientCode;

    /**
     * 秘钥
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    private String secret;

    /**
     * 时效(毫秒)
     */
    @NotNull(groups = {Const.GroupInsert.class})
    private Long expiration;

    /**
     * 客户端主页
     */
    private String mainUrl;


}

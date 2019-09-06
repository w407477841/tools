package com.honghu.oauth2.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(value = "id",type =IdType.ID_WORKER )
    @NotNull(groups = {Const.GroupUpdate.class})
    @Null(groups = {Const.GroupInsert.class})
    private Long id;

    /**
     * 用户名(字母开头)
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    @Null(groups = {Const.GroupUpdate.class})
    private String username;

    /**
     * 手机号
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    private String phone;

    /**
     * 手机地区编号
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    private String areaCode;

    /**
     * 邮箱
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    private String email;

    /**
     * 昵称
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    private String nickname;

    /**
     * 密码
     */
    @NotBlank(groups = {Const.GroupInsert.class})
    @Null(groups = {Const.GroupUpdate.class})
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 管理员(本系统自带用户)
     */
    private String adminFlag;


}

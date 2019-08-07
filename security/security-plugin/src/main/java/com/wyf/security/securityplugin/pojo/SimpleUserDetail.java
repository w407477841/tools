package com.wyf.security.securityplugin.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:46 2019/7/19
 * Modified By : wangyifei
 */
@Getter
@Setter
@ToString
public class SimpleUserDetail implements UserDetails {

    //==== 属性 ====

    //==== 接口所需 ====

    private List<? extends GrantedAuthority> authorities;
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    //===== 业务新增 ====

    //==== 构造器 ====


    public SimpleUserDetail() {
    }

    public SimpleUserDetail(List<? extends GrantedAuthority> authorities, String password, String username) {
         this(authorities,password,username,true,true,true,true);
    }

    public SimpleUserDetail(List<? extends GrantedAuthority> authorities, String password, String username, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.authorities = authorities;
        this.password = password;
        this.username = username;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    // ==== 方法 ===


}

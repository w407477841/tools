package com.wyf.security.securityplugin.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:05 2019/7/19
 * Modified By : wangyifei
 */
@Getter
public class UsernameAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;


    public UsernameAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    public UsernameAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        this.credentials = null;
        super.eraseCredentials();
    }
}

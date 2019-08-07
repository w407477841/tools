package com.wyf.security.securityplugin.service;

import com.wyf.security.securityplugin.pojo.SimpleUserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:42 2019/7/19
 * Modified By : wangyifei
 */
@Slf4j
public class SimpleUserDetailService implements UserDetailsService {

    public SimpleUserDetailService() {
        log.info("执行初始化SimpleUserDetailService");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        list.add(grantedAuthority);
        String password = "123456";
        return new SimpleUserDetail(list,username,password);
    }
}

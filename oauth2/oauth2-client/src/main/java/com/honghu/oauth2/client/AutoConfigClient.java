package com.honghu.oauth2.client;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.honghu.oauth2.client.filter.JwtAuthenticationFilter;
import com.honghu.oauth2.client.properties.OAuthPropeties;
import com.honghu.oauth2.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Configuration
@EnableConfigurationProperties(value=OAuthPropeties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true)
@Slf4j
@Controller
@RequestMapping
public class AutoConfigClient extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuthPropeties propeties;
    @Autowired
    private Ignore ignore;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(propeties);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //配置策略
                  List<String> ignores  = ignore.ignore();
                for (String s : ignores) {
                    http.authorizeRequests().antMatchers(s).permitAll();
                }

                    http
                .cors().and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/static/**").permitAll()
                            .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and().addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestBody LoginDTO loginDTO){
        log.info(loginDTO.toString());
        return HttpUtil.post(propeties.getServerUrl(),JSONUtil.toJsonStr(loginDTO));
    }

}

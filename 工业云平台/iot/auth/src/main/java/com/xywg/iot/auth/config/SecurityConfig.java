package com.xywg.iot.auth.config;

import com.xywg.iot.auth.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true)
@EnableWebSecurity
@Order(value = SecurityProperties.BASIC_AUTH_ORDER)
/**
 * @author: wangyifei
 * Description:
 * Date: 13:30 2018/12/19
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
    }

    @Override
    public void configure(WebSecurity web) {

        web.ignoring().antMatchers(HttpMethod.POST, "/users").
                antMatchers("/", "/auth/**", "/resources/**",
                "/static/**", "/public/**", "/webui/**", "/h2-console/**",
                "/configuration/**", "/swagger-ui/**","/swagger-resources/**",
                "/api-docs", "/api-docs/**", "/v2/api-docs/**",
                "/*.html", "/**/*.html","/**/*.css",
                "/**/*.js", "/**/*.png", "/**/*.jpg",
                "/**/*.gif", "/**/*.svg", "/**/*.ico",
                "/**/*.ttf","/**/*.woff"
        )

        ;


    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // .exceptionHandling().disable() //去掉异常处理
                // 原本403异常会被处理掉，现在会抛出异常。并且只能在为进入controler的全局异常处理捕获
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                // 允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/login").permitAll()
                .antMatchers(HttpMethod.GET,"/refresh").permitAll()
                .antMatchers(HttpMethod.POST,"/repassword").permitAll()
                //webSocket 推送地址
                .antMatchers(HttpMethod.GET,"/webSocketServer/**").permitAll()
                //服务器之间内部通信地址
                .antMatchers(HttpMethod.POST,"/internalCommunication/**").permitAll()
               // .antMatchers(HttpMethod.POST,"/business/homepage/*").permitAll()
               // .antMatchers(HttpMethod.POST,"/iot/**").permitAll()
                // .antMatchers("/other","/other/**").hasRole("ADMIN")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated().and()
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        // 添加JWT filter
        // httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
        // UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        /// httpSecurity.headers().cacheControl();

    }



    /**
     * 配置 密码器
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

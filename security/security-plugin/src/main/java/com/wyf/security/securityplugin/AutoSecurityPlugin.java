package com.wyf.security.securityplugin;


import com.wyf.security.core.properties.JwtTokenPropery;
import com.wyf.security.securityplugin.config.SecurityConfig;
import com.wyf.security.securityplugin.filter.JWTAuthenticationFilter;
import com.wyf.security.securityplugin.provider.UsernameAuthenticationProvider;
import com.wyf.security.securityplugin.service.SimpleUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:35 2019/7/18
 * Modified By : wangyifei
 */
@Configuration
@EnableConfigurationProperties(value  = {JwtTokenPropery.class})
@Import(value = {SimpleUserDetailService.class,UsernameAuthenticationProvider.class,SecurityConfig.class,JWTAuthenticationFilter.class})
public class AutoSecurityPlugin  extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    /**
     * 配置认证管理
     * 配置自定义登录认证器
     * 配置自定义用户加载
     * 配置密码加密器
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.
                // 配置授权器
                authenticationProvider(authenticationProvider).
                // 根据用户名拉取用户信息
                userDetailsService(userDetailsService).
                // 密码器
                passwordEncoder(passwordEncoder);
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/","/auth/**","/resources/**","/static/**", "/public/**",
                    "/*.html", "/**/*.html",
                    "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf",
                    "/**/*.woff");

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 添加白名单 .antMatchers().permitAll()
                .anyRequest().authenticated().and()
        ;


    }
}

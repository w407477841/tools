package com.xywg.iot.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.auth.config.JwtProperties;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.cloud.model.CloudUser;
import com.xywg.iot.modular.cloud.service.ICloudUserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.xywg.iot.auth.aop.GlobalExceptionHandler.UNAUTHORIZED;


/**
 * @author : wangyifei
 * Description 用于token认证
 * 不用了
 *
 * Date: Created in 13:36 2018/12/19
 * Modified By : wangyifei
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);
    private JwtProperties  jwtProperties;
    private ICloudUserService  cloudUserService;

    public JwtAuthFilter() {
    }
    @Autowired
    public JwtAuthFilter(JwtProperties jwtProperties,ICloudUserService  cloudUserService) {
        this.jwtProperties = jwtProperties;
        this.cloudUserService = cloudUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader(this.jwtProperties.getTokenHeader());
        String user = httpServletRequest.getHeader(this.jwtProperties.getUserHeader());
        if (StrUtil.isEmpty(token) || StrUtil.isEmpty(user)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // 将认证信息放入security上下文
        SecurityContextHolder.getContext().setAuthentication( getAuthentication(token,user));
        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }

        private UsernamePasswordAuthenticationToken getAuthentication( String token,String user) {

                // 解析 Authorization 得到用户名.
                Claims claims = null;
                CloudUser cloudUser  = cloudUser(user);
                try {

                    //根据用户名获得密钥
                    //解析获得 权鉴
                    claims = Jwts.parser()
                            .setSigningKey(secret(user,cloudUser).getBytes())
                            .parseClaimsJws(token)
                            .getBody();
                } catch (ExpiredJwtException e) {
                    UNAUTHORIZED.set(ResultCode.NON_EXPIRED);
                    LOGGER.error("token过期<{}>",e.getMessage());
                } catch (MalformedJwtException e) {
                    UNAUTHORIZED.set(ResultCode.AUTH_ERROR);
                    LOGGER.error("token格式异常<{}>",e.getMessage());
                } catch (SignatureException e) {
                    UNAUTHORIZED.set(ResultCode.AUTH_ERROR);
                    LOGGER.error("token与密钥不匹配");
                } catch (UsernameNotFoundException e){
                    UNAUTHORIZED.set(ResultCode.NO_USER);
                    LOGGER.error(e.getMessage());
                } catch (Exception e){
                    UNAUTHORIZED.set(ResultCode.AUTH_ERROR);
                    LOGGER.error(e.getMessage());
                }
                    if (claims == null) {
                        return null;
                    }
                return new UsernamePasswordAuthenticationToken(user, null,authorities(cloudUser) );
        }
        private CloudUser cloudUser(String user){
//            Wrapper wrapper  = new EntityWrapper();
//            wrapper.eq("username",user);
//            String key  = "iot:user:"+user;
            String key  = user;
            CloudUser  cloudUser  = cloudUserService.selectCacheOne(key);
            return cloudUser;
        }

    /**
     *  生成权限
     * @param cloudUser
     * @return
     */
        public Collection<? extends GrantedAuthority> authorities(CloudUser cloudUser){
            Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
            SimpleGrantedAuthority  simpleGrantedAuthority ;
            //基础
            simpleGrantedAuthority  =new SimpleGrantedAuthority("ROLE_BASE");
            authorities.add(simpleGrantedAuthority);

            if(cloudUser.getActiveStatus().equals(1)){
                //已激活
                simpleGrantedAuthority  =new SimpleGrantedAuthority("ROLE_ACTIVE");
                authorities.add(simpleGrantedAuthority);
            }

            if(cloudUser.getAuthStatus().equals(1)){
                //通过认证
                simpleGrantedAuthority  =new SimpleGrantedAuthority("ROLE_AUTH");
                authorities.add(simpleGrantedAuthority);
            }
            return authorities;
        }

        private  String secret(String user,CloudUser cloudUser){

            if(cloudUser==null){
                throw  new UsernameNotFoundException("用户<"+user+">不存在");
            }
            return cloudUser.getSecretKey();
        }



    }

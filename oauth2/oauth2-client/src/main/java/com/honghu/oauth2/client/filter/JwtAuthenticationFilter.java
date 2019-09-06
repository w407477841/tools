package com.honghu.oauth2.client.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.honghu.oauth2.Const;
import com.honghu.oauth2.client.properties.OAuthPropeties;
import com.honghu.oauth2.client.util.ResponseUtil;
import com.honghu.oauth2.system.entity.User;
import com.honghu.oauth2.util.UserUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author : wangyifei
 * Description 用于token认证
 *
 * Date: Created in 13:36 2018/12/19
 * Modified By : wangyifei
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    /**
     * token参数头
     */
    private static final  String HEADER = "accessToken";




    private final  OAuthPropeties propeties;

    public JwtAuthenticationFilter(OAuthPropeties propeties) {
        this.propeties = propeties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 获取认证信息
        String header = httpServletRequest.getHeader(HEADER);
        if (StrUtil.isBlank(header) ) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(header,httpServletResponse);
        if (authentication == null) {
            // 认证时出现异常，并已填报异常信息
            return ;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }






    /**
     * 用户权限
     * @return
     */
    private Collection<? extends GrantedAuthority> authorities(List<String> authList){
        Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
        authList.forEach(role->{
            SimpleGrantedAuthority  simpleGrantedAuthority  =new SimpleGrantedAuthority("ROLE_"+role);
            authorities.add(simpleGrantedAuthority);
        });

        //第三方

        return authorities;
    }


        private UsernamePasswordAuthenticationToken getAuthentication( String token,HttpServletResponse httpServletResponse) {

                // 解析 Authorization 得到用户名.
                Claims claims;
                try {
                    //根据用户名获得密钥
                    //解析获得 权鉴
                    claims = Jwts.parser()
                            .setSigningKey(propeties.getSecret())
                            .parseClaimsJws(token)
                            .getBody();
                    //获取用户名
                    String username = claims.getSubject();
                    List<String> clientList = claims.get(Const.CLIENTS,List.class);
                    if(!clientList.contains(propeties.getClientId())){
                        LOGGER.info("系统未授权该用户<{}>",username);
                        ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false,500,"系统未授权用户"));
                        return  null;
                    }
                    String pages = claims.get(propeties.getClientId(),String.class);


                    List<String> authList = claims.get(Const.AUTHORITIES,List.class);

                    User user =JSONUtil.toBean(claims.get(Const.SUB, String.class),User.class) ;

                    //获取权限
                    Collection<? extends GrantedAuthority> authorities = authorities(authList);


                    if(StrUtil.isNotBlank(username)) {
                        //此处password不能为null
                       // User principal = new User(username, "", authorities);

                        // 用户在该客户端的所有可见页面
                        UserUtil.USER_PAGES.set(pages);
                        // 当前用户信息
                        UserUtil.USER_INFO.set(user);

                        return new UsernamePasswordAuthenticationToken(username, null, authorities);
                    }
                }catch (ExpiredJwtException e) {
                    LOGGER.error("token过期<{}>",e.getMessage());
                    ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false,500,"token过期"));
                } catch (Exception e){
                    LOGGER.error("解析accesstoken错误<{}>",e.getMessage());
                    ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false,500,"解析accesstoken错误"));
                }
                        return null;
        }




    }

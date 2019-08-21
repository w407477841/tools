package com.honghu.oauth2.client.filter;

import cn.hutool.core.util.StrUtil;
import com.honghu.oauth2.Const;
import com.honghu.oauth2.client.properties.OAuthPropeties;
import com.honghu.oauth2.client.util.ResponseUtil;
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
import org.springframework.security.core.userdetails.User;
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
 * 不用了
 *
 * Date: Created in 13:36 2018/12/19
 * Modified By : wangyifei
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    /**
     * token分割
     */
    private static final  String TOKEN_SPLIT = "Bearer ";
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

        // 正常内部系统访问
        String header = httpServletRequest.getHeader(HEADER);
        if (StrUtil.isBlank(header) || !header.startsWith(TOKEN_SPLIT)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(header,httpServletResponse);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }catch (Exception e){
            System.out.println(e.getMessage());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    }






    /**
     * 用户权限
     * @return
     */
    private Collection<? extends GrantedAuthority> authorities(List<String> authList){
        Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
        authList.forEach(role->{
            SimpleGrantedAuthority  simpleGrantedAuthority  =new SimpleGrantedAuthority(role);
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
                            .parseClaimsJws(token.replace(TOKEN_SPLIT, ""))
                            .getBody();
                    //获取用户名
                    String username = claims.getSubject();
                    List<String> authList =   claims.get(Const.AUTHORITIES,List.class);
                    //获取权限
                    Collection<? extends GrantedAuthority> authorities = authorities(authList);


                    if(StrUtil.isNotBlank(username)) {
                        //此处password不能为null
                        User principal = new User(username, "", authorities);
                        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
                    }
                }catch (ExpiredJwtException e) {
                    LOGGER.error("token过期<{}>",e.getMessage());
                    ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false,500,"token过期"));
                } catch (Exception e){
                    e.printStackTrace();
                    ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false,500,"解析accesstoken错误"));
                }
                        return null;
        }




    }

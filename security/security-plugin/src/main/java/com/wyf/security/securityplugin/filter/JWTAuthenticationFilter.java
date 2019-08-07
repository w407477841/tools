package com.wyf.security.securityplugin.filter;

import cn.hutool.core.util.StrUtil;
import com.wyf.security.core.Const;
import com.wyf.security.securityplugin.pojo.SimpleUserDetail;
import com.wyf.security.securityplugin.service.SimpleUserDetailService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:50 2019/7/19
 * Modified By : wangyifei
 */
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private SimpleUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String  token ;
        log.info("x-forwarded-for：[{}]",request.getHeader("x-forwarded-for"));
        log.info("remoteAddr:[{}]" , request.getRemoteAddr());


        String headerToken = request.getHeader(Const.AUTH_HEADER_NAME);
        if(StrUtil.isBlank(headerToken)){
            log.info("Head中不存在认证头[{}]",Const.AUTH_HEADER_NAME);
            String paramterToken  =   request.getParameter(Const.AUTH_HEADER_NAME);
            if(StrUtil.isBlank(paramterToken)){
                log.info("Paramter中也不存在认证头[{}]",Const.AUTH_HEADER_NAME);
                chain.doFilter(request, response);
                return;
            }else{
                token = paramterToken;
                log.info("Paramter中认证信息 [{}],",token);
            }
        }else{
            token =  headerToken;
            log.info("Head中认证信息 [{}],",token);
        }
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
            // 解析 Authorization 得到用户名.
            Claims claims = null;
            try {
                //解析获得 权鉴
                claims = Jwts.parser().setSigningKey(Const.AUTH_SECRET)
                        .parseClaimsJws(token).getBody();

            } catch (ExpiredJwtException e) {
                log.error("认证Token 已超时");
                e.printStackTrace();
            } catch (UnsupportedJwtException e) {
                log.error("认证Token 格式不对");
                e.printStackTrace();
            } catch (MalformedJwtException e) {
                log.error("分隔符数量错误/载荷为空");
                e.printStackTrace();
            } catch (SignatureException e) {
                log.error("签名计算错误");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                log.error("参数错误");
                e.printStackTrace();
            }
            if(claims==null){
                return null;
            }
            String user = claims.getSubject();
            //将token保存在当前线程中
            // 查下用户信息
            SimpleUserDetail userDetail =(SimpleUserDetail) userDetailService.loadUserByUsername(user) ;

            return new UsernamePasswordAuthenticationToken(user, null,userDetail.getAuthorities() );
    }

}

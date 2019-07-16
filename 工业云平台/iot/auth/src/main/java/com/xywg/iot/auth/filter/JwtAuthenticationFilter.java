package com.xywg.iot.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.xywg.iot.auth.config.JwtProperties;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.app.model.AppInfo;
import com.xywg.iot.modular.app.service.IAppInfoService;
import com.xywg.iot.modular.system.model.SysRole;
import com.xywg.iot.modular.system.service.ISysUserService;
import com.xywg.iot.modular.system.vo.UserVO;
import com.xywg.iot.util.ResponseUtil;
import com.xywg.iot.util.UserUtil;
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
import java.util.Locale;

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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private JwtProperties  jwtProperties;
    private IAppInfoService appInfoService;
    private ISysUserService userService;
    /**
     * token分割
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "xywg";

    /**
     * token参数头
     */
    String HEADER = "accessToken";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    String ACCESS_SECRET_HEADER = "Access-Secret";
    String APP_KEY_HEADER = "App-Key";

    private static final String I18N_LANGUAGE = "Language";


    public JwtAuthenticationFilter() {
    }
    @Autowired
    public JwtAuthenticationFilter(JwtProperties jwtProperties, IAppInfoService  appInfoService,ISysUserService userService) {
        this.jwtProperties = jwtProperties;
        this.appInfoService = appInfoService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // i18n
        String i18n_language =  httpServletRequest.getHeader(I18N_LANGUAGE);
        Locale locale  = Locale.getDefault();
        if(!StrUtil.isEmpty(i18n_language)){
            String language []=  i18n_language.split("_");
            locale = new Locale(language[0], language[1]);
        }
        UserUtil.localLocale.set(locale);


        String accessSecret = httpServletRequest.getHeader(ACCESS_SECRET_HEADER);

        String appKey = httpServletRequest.getHeader(APP_KEY_HEADER);
        if(StrUtil.isNotBlank(appKey)){
            //判断为 第三方认证
            try {
                 AppInfo appInfo =  appInfoService.selectCacheOne(appKey);
                 if(appInfo !=null&&accessSecret.equals(appInfo.getAccessSecret())){
                     UserUtil.USERID.set(appInfo.getCreateUser());
                     UserUtil.USERNAME.set(appInfo.getCreateUserName());
                     UserUtil.ISADMIN.set(false);
                     UsernamePasswordAuthenticationToken authentication =  new UsernamePasswordAuthenticationToken(appInfo.getCreateUserName(), null, thirdAuthorities());
                     SecurityContextHolder.getContext().setAuthentication(authentication);
                     filterChain.doFilter(httpServletRequest, httpServletResponse);
                     return ;
                 }
            }catch (Exception e){
                System.out.println(e.getMessage());
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return ;
            }

        }
        // 正常内部系统访问
        String header = httpServletRequest.getHeader(HEADER);
        UserUtil.USERID.set(httpServletRequest.getHeader("userId"));
        UserUtil.USERNAME.set(httpServletRequest.getHeader("userName"));
        UserUtil.ISADMIN.set(false);
        if(StrUtil.isBlank(header)){
            header = httpServletRequest.getParameter(HEADER);
           UserUtil.accessToken.set(header);
        }
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
     *  生成第三方权限
     * @return
     */
    private Collection<? extends GrantedAuthority> thirdAuthorities(){
        Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
        SimpleGrantedAuthority  simpleGrantedAuthority ;
        //第三方
        simpleGrantedAuthority  =new SimpleGrantedAuthority("ROLE_THIRD");
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }


    /**
     * 用户权限
     * @return
     */
    private Collection<? extends GrantedAuthority> authorities(String username){
        Collection<SimpleGrantedAuthority> authorities =  new ArrayList<>();
        UserVO userVO =  userService.selectCacheOne(username);
        List<SysRole> roles  =  userVO.getRoles();
        roles.forEach(role->{
            SimpleGrantedAuthority  simpleGrantedAuthority  =new SimpleGrantedAuthority(role.getName());
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
                            .setSigningKey(JWT_SIGN_KEY)
                            .parseClaimsJws(token.replace(TOKEN_SPLIT, ""))
                            .getBody();
                    //获取用户名
                    String username = claims.getSubject();

                    //获取权限
                    Collection<? extends GrantedAuthority> authorities = authorities(username);

                    authorities.forEach(auth->{

                        if("ROLE_ADMIN".equals(auth.getAuthority())){
                            UserUtil.ISADMIN.set(true);
                        }

                    });


                    if(StrUtil.isNotBlank(username)) {
                        //此处password不能为null
                        User principal = new User(username, "", authorities);
                        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
                    }
                }catch (ExpiredJwtException e) {
                    UNAUTHORIZED.set(ResultCode.NON_EXPIRED);
                    LOGGER.error("token过期<{}>",e.getMessage());
                } catch (Exception e){
                    e.printStackTrace();
                    ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(false,500,"解析token错误"));
                }
                        return null;
        }




    }

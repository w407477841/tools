package com.honghu.oauth2.server.security.controller;

import cn.hutool.json.JSONUtil;
import com.honghu.oauth2.Const;
import com.honghu.oauth2.dto.LoginDTO;
import com.honghu.oauth2.server.security.server.ISecurityServer;
import com.honghu.oauth2.system.entity.Client;
import com.honghu.oauth2.system.entity.User;
import com.honghu.oauth2.vo.ResultVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    private ISecurityServer securityServer;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResultVO loginPage(@RequestBody LoginDTO login, HttpServletResponse response) throws IOException {
        // 根据 clientId 找到对应配置
        String clientId =  login.getClientId();
        Client client = securityServer.loadClient(clientId);
        if (client == null) {
            return ResultVO.error("客户端ID有误");
        }
         //
         User user = securityServer.loadUser(login.getUsername(),client.getId());
        if (user == null) {
            return ResultVO.error("用户不存在");
        }
        if(!passwordEncoder.matches(login.getPassword(),user.getPassword())){
            return ResultVO.error("密码错误");
        }

        Claims claims =new DefaultClaims();
        Long lastTime=System.currentTimeMillis() + client.getExpiration();

        //持有者
        claims.put(Const.SUB,JSONUtil.toJsonStr(user));
        // 设置授权后的客户端
        List<String> authorizedClients = securityServer.authorizedClient(user.getId());
        claims.put(Const.CLIENTS,authorizedClients);
        // 为每个客户端设置 页面;

        List<Map<String,Object>> pages = securityServer.loadPages(user.getId());

        pages.forEach(item->{
            claims.put((String) item.get("clientCode"),item.get("resourceCode"));
        });

        //过期时间
        claims.put(Const.EXP, new Date(lastTime));
        //权限
        if(Const.TRUE_FLAG.equals(user.getAdminFlag())){
            claims.put(Const.AUTHORITIES,Const.ADMIN_ROLES);
            claims.put(Const.ADMIN_FLAG,1);
        }else{
            claims.put(Const.AUTHORITIES,securityServer.loadRoles(user.getId()));
            claims.put(Const.ADMIN_FLAG,0);
        }

        String token  = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, client.getSecret())
                .setExpiration( new Date(lastTime))
                .compact();
        return ResultVO.success(token);
    }


}

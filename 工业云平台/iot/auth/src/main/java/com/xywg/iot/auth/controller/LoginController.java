package com.xywg.iot.auth.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.auth.dto.LoginDTO;
import com.xywg.iot.auth.dto.RePasswordDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.cloud.model.CloudUser;
import com.xywg.iot.modular.cloud.service.ICloudUserService;
import com.xywg.iot.vo.LoginVO;
import com.xywg.iot.vo.ResultVO;
import com.xywg.iot.vo.TokenVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:23 2018/12/20
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
@Api("认证相关")
public class LoginController {

    @Autowired
    private ICloudUserService  cloudUserService;
    @Autowired
    private BCryptPasswordEncoder   passwordEncoder ;

    @PostMapping("login")
    public Object login(@RequestBody LoginDTO user) {
        ResultVO resultVO ;
        //用户名
        String username = user.getUsername();
        //密码
        String password = user.getPassword();
//        Wrapper<CloudUser> wrapper = new EntityWrapper();
//        wrapper.eq("username", username);
//        String key  = "iot:user:"+username;
        String key  = username;
        CloudUser cloudUser = cloudUserService.selectCacheOne(key);
        if (cloudUser == null) {
            // 用户不存在
            resultVO = new ResultVO(ResultCode.NO_AUTH,null);
            return resultVO;
        }
        if (cloudUser.getActiveStatus().equals(0)) {
            //未激活
        }
        if (cloudUser.getAuthStatus().equals(0)) {
            //未认证
        }
        if (cloudUser.getStatus().equals(1)) {
            //锁定
            resultVO = new ResultVO(ResultCode.LOCKED,null);
            return resultVO;
        }
        if (passwordEncoder.matches(password, cloudUser.getPassword())) {
            // 登录成功

            //生成密钥


            String random = RandomUtil.randomString(16).toUpperCase();

            String secret = SecureUtil.md5(random);
            cloudUser . setSecretKey(secret);
            cloudUser.setLoginTime(new Date());
            //更新一波  删除缓存
            cloudUserService.removeCaches(key);
            cloudUserService.updateById(cloudUser);

            LoginVO loginVO  =new LoginVO(random,username);

           resultVO = new ResultVO(ResultCode.LOGIN_SUCCESS,loginVO);


        } else {
            resultVO = new ResultVO(ResultCode.NO_AUTH,null);
            // 密码错误
        }
        return resultVO;
    }

    /**
     *  使用secret + random + user 来刷新token
     *  减少密码使用次数，防止密码被盗
     *
     *
     * @param user
     * @param random
     * @return
     */
    @GetMapping("refresh")
    public Object refresh(@RequestParam(value = "user") String user,@RequestParam(value="random") String random){
        ResultVO resultVO ;

        String key  = user;
        CloudUser cloudUser = cloudUserService.selectCacheOne(key);
        if (cloudUser == null) {
            // 用户不存在
            resultVO = new ResultVO(ResultCode.TOKEN_REFLESH_ERROR,null);
            return resultVO;
        }
        if (cloudUser.getActiveStatus().equals(0)) {
            //未激活
        }
        if (cloudUser.getAuthStatus().equals(0)) {
            //未认证
        }
        if (cloudUser.getStatus().equals(1)) {
            //锁定
            resultVO = new ResultVO(ResultCode.LOCKED,null);
            return resultVO;
        }
         String secret = cloudUser.getSecretKey() ;


        if(secret.equals(SecureUtil.md5(random))){
            Claims claims =new DefaultClaims();

            Long lastTime=System.currentTimeMillis() + 60*60*1000L;

            //持有者
            claims.put("sub",user);
            //过期时间
            claims.put("exp", new Date(lastTime));
            //权限
            claims.put("auths",null);
            String token  = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                    .setExpiration( new Date(lastTime))
                    .compact();
            TokenVO tokenVO  =new TokenVO(random,user,token);
            resultVO = new ResultVO(ResultCode.TOKEN_REFLESH_SUCCESS,tokenVO);
        }else{
            resultVO = new ResultVO(ResultCode.TOKEN_REFLESH_ERROR,null);
        }


        return resultVO;
    }




    @PostMapping("repassword")
    public Object rePassword(@RequestBody RePasswordDTO user) {
        ResultVO resultVO ;
        //用户名
        String username = user.getUsername();
        //旧密码
        String password = user.getPassword();
        // 新密码
        String newPassword = user.getNewPassword();

//        Wrapper<CloudUser> wrapper = new EntityWrapper();
//        wrapper.eq("username", username);
//        String key  = "iot:user:"+username;
        String key  = username;
        CloudUser cloudUser = cloudUserService.selectCacheOne(key);
        if (cloudUser == null) {
            // 用户不存在
            resultVO = new ResultVO(ResultCode.ERROR,null);
            return resultVO;
        }

        if (passwordEncoder.matches(password, cloudUser.getPassword())) {
            // 原密码没问题

            // 让原密钥失效
            cloudUser . setSecretKey("ABC");
            cloudUser.setPassword(passwordEncoder.encode(newPassword));
            //更新一波  删除缓存
            cloudUserService.removeCaches(key);
            cloudUserService.updateById(cloudUser);
            resultVO = new ResultVO(ResultCode.SUCCESS,null);
        } else {
            resultVO = new ResultVO(ResultCode.ERROR,null);
            // 密码错误
        }
        return resultVO;
    }



}

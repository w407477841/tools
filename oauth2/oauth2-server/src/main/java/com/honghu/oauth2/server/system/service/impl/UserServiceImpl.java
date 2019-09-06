package com.honghu.oauth2.server.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honghu.oauth2.server.security.server.ISecurityServer;
import com.honghu.oauth2.system.entity.User;
import com.honghu.oauth2.server.system.mapper.UserMapper;
import com.honghu.oauth2.server.system.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honghu.oauth2.vo.ResultVO;
import com.honghu.oauth2.vo.user.ModifyPasswordV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2019-08-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    @Override
    public boolean save(User entity) {
        // 加密下密码;
        String   encoderPass  = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(encoderPass);
        return super.save(entity);
    }

    @Override
    public ResultVO modifyPasswordV1(ModifyPasswordV1 modifyPasswordV1) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",modifyPasswordV1.getUsername());
        User user = this.getOne(userQueryWrapper);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }
        if(!passwordEncoder.matches(modifyPasswordV1.getOldPassword(),user.getPassword())){
            return ResultVO.error("密码不正确");
        }
        boolean ok  = this.updateById(new User().setId(user.getId()).
                setPassword(passwordEncoder.encode(modifyPasswordV1.getPassword())));
        if(ok){
            return ResultVO.success();
        }else{
            return ResultVO.error("修改失败");
        }

    }
}

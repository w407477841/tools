package com.xywg.iot.modular.colud.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.util.UserUtil;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.app.model.AppInfo;
import com.xywg.iot.modular.app.service.IAppInfoService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/16
 *TIME:17:00
 */
@RestController
@RequestMapping("/business/app")
public class AppController extends BaseControllerPlus<AppInfo,IAppInfoService>{
    public AppController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH};
        this.setQueryRoles(roles);
        this.setDeleteRoles(roles);
    }

    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH})
    public ResultVO insert(@RequestBody AppInfo appInfo) {
        try {
            appInfo.setAccessSecret(RandomUtil.randomString(64));
            appInfo.setAppKey(RandomUtil.randomString(16));
            Wrapper<AppInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("app_name",appInfo.getAppName()).eq("create_user", UserUtil.USERID.get());
            List<AppInfo> list = service.selectList(wrapper);
            //判断重名
            if(list.size() > 0) {
                return new ResultVO(ResultCode.APP_NAME_REPEAT,null);
            }
            if(service.insert(appInfo)) {
                return new ResultVO(ResultCode.INSERT_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.INSERT_ERROR,null);
    }

    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH})
    public ResultVO update(@RequestBody AppInfo appInfo) {
        try {
            Wrapper<AppInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("app_name",appInfo.getAppName()).ne("id",appInfo.getId()).eq("create_user", UserUtil.USERID.get());
            List<AppInfo> list = service.selectList(wrapper);
            //判断重名
            if(list.size() > 0) {
                return new ResultVO(ResultCode.APP_NAME_REPEAT,null);
            }
            if(service.updateById(appInfo)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }
}

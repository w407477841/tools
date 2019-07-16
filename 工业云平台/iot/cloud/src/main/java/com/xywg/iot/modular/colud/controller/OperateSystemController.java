package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.dictionary.model.MasterOperateSystem;
import com.xywg.iot.modular.dictionary.service.IMasterOperateSystemService;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/3
 *TIME:13:57
 */
@RestController
@RequestMapping("/business/operateSystem")
public class OperateSystemController extends BaseControllerPlus<MasterOperateSystem,IMasterOperateSystemService> {
    public OperateSystemController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE};
        this.setQueryRoles(roles);
    }

    @ApiOperation("新增")
    @Override
    @OpenLog
    @RolesAllowed(RoleConstant.ADMIN)
    public ResultVO insert(@RequestBody MasterOperateSystem masterOperateSystem) {
        try {
            Wrapper<MasterOperateSystem> wrapper = new EntityWrapper<>();
            wrapper.eq("name",masterOperateSystem.getName());
            List<MasterOperateSystem> list = service.selectList(wrapper);
            //判断重名
            if(list.size() > 0) {
                return new ResultVO(ResultCode.OPERATE_NAME_REPEAT,null);
            }
            if(service.insert(masterOperateSystem)) {
                return new ResultVO(ResultCode.INSERT_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.INSERT_ERROR,null);
    }

    @ApiOperation("编辑")
    @Override
    @OpenLog
    @RolesAllowed(RoleConstant.ADMIN)
    public ResultVO update(@RequestBody MasterOperateSystem masterOperateSystem) {
        try {
            Wrapper<MasterOperateSystem> wrapper = new EntityWrapper<>();
            wrapper.eq("name",masterOperateSystem.getName()).ne("id",masterOperateSystem.getId());
            List<MasterOperateSystem> list = service.selectList(wrapper);
            //判断重名
            if(list.size() > 0) {
                return new ResultVO(ResultCode.OPERATE_NAME_REPEAT,null);
            }
            if(service.updateById(masterOperateSystem)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }
}

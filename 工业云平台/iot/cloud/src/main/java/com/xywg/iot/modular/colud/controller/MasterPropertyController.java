package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.dictionary.model.MasterProperty;
import com.xywg.iot.modular.dictionary.service.IMasterPropertyService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:20 2019/1/4
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/business/master/property")
public class MasterPropertyController extends BaseControllerPlus<MasterProperty,IMasterPropertyService> {
    public MasterPropertyController() {
        // 管理员和认证后的用户可以查询
        this.setQueryRoles(RoleConstant.LEVEL_AUTH);
    }


    @Override
    @OpenLog
    @RolesAllowed(value = RoleConstant.ADMIN)
    public ResultVO insert(@RequestBody MasterProperty masterProperty) {
        try {
            Wrapper<MasterProperty> wrapper = new EntityWrapper<>();
            wrapper.eq("`key`",masterProperty.getKey());
            List<MasterProperty> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            if(service.insert(masterProperty)) {
                return new ResultVO(ResultCode.INSERT_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.INSERT_ERROR,null);
    }

    @Override
    @OpenLog
    @RolesAllowed(value = RoleConstant.ADMIN)
    public ResultVO update(@RequestBody MasterProperty masterProperty) {
        try {
            Wrapper<MasterProperty> wrapper = new EntityWrapper<>();
            wrapper.eq("`key`",masterProperty.getKey()).ne("id",masterProperty.getId());
            List<MasterProperty> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            if(service.updateById(masterProperty)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }
}

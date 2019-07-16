package com.xywg.iot.modular.colud.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.dictionary.model.MasterLinkType;
import com.xywg.iot.modular.dictionary.service.IMasterLinkTypeService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据字典---联网方式 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-03
 */
@RestController
@RequestMapping("/business/dictionary/linkType")
public class MasterLinkTypeController extends BaseControllerPlus<MasterLinkType, IMasterLinkTypeService> {

    public MasterLinkTypeController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE};
        this.setQueryRoles(roles);
    }

    @Override
    @PostMapping("insert")
    @OpenLog
    public ResultVO insert(@RequestBody MasterLinkType dto) {
        EntityWrapper<MasterLinkType> ew = new EntityWrapper<>();
        ew.eq("name", dto.getName());
        List<MasterLinkType> list = service.selectList(ew);
        if (list.size() > 0) {
            return new ResultVO(ResultCode.LINK_TYPE_EXISTS, null);
        }
        dto.setIsDel(0);
        dto.setCreateTime(new Date());
        return new ResultVO(ResultCode.SUCCESS, service.insert(dto));
    }

    @Override
    @PostMapping("update")
    @OpenLog
    public ResultVO update(@RequestBody MasterLinkType dto) {
        EntityWrapper<MasterLinkType> ew = new EntityWrapper<>();
        ew.eq("name", dto.getName());
        ew.ne("id", dto.getId());
        List<MasterLinkType> list = service.selectList(ew);
        if (list.size() > 0) {
            return new ResultVO(ResultCode.LINK_TYPE_EXISTS,  null);
        }
        dto.setModifyTime(new Date());
        return new ResultVO(ResultCode.SUCCESS, service.updateById(dto));
    }


    @PostMapping("selectListNoWhere")
    @OpenLog
    public ResultVO selectList() {
        Wrapper<MasterLinkType> wrapper = new EntityWrapper<>();
        //如果不是管理员 那么数据需要过滤
       /* if (!UserUtil.ISADMIN.get()) {
            wrapper.eq("create_user", UserUtil.USERID.get());
        }*/
        return new ResultVO(ResultCode.SUCCESS, service.selectList(wrapper));
    }
}


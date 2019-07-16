package com.xywg.iot.modular.colud.controller.enterprise;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoIndividualVo;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BasalController;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.enterprise.model.AccountInfoIndividual;
import com.xywg.iot.modular.enterprise.service.AccountInfoIndividualService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * <p>
 * 个人用户基本信息 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/business/accountInfoIndividual")
public class AccountInfoIndividualController extends BasalController<AccountInfoIndividual, AccountInfoIndividualService> {
    @Autowired
    private AccountInfoIndividualService accountInfoIndividualService;

    @GetMapping("selectByUserId")
    @OpenLog
    @RolesAllowed({RoleConstant.AUTH, RoleConstant.THIRD, RoleConstant.BASE})
    public ResultVO selectByUserId(@RequestParam String userId) {
        //获取填写的申请人基础信息
        EntityWrapper<AccountInfoIndividualVo> infoIndividualEw = new EntityWrapper<>();
        infoIndividualEw.eq("a.user_id", userId);
        AccountInfoIndividualVo infoIndividualDb = accountInfoIndividualService.getAccountInfoIndividual(infoIndividualEw);
        return new ResultVO(ResultCode.SUCCESS, infoIndividualDb);
    }

    @Override
    @PostMapping("insert")
    @OpenLog
    @RolesAllowed({RoleConstant.AUTH, RoleConstant.THIRD, RoleConstant.BASE})
    public ResultVO insert(@RequestBody AccountInfoIndividual accountInfoIndividual) {
        //填写保存人员基本信息时 申请状态为0 表示此时只填写了申请人基本信息但是还没有申请企业认证
        accountInfoIndividual.setAuditStatus(0);
        //查询是否已经填写过
        EntityWrapper<AccountInfoIndividual> ew = new EntityWrapper<>();
        ew.eq("is_del", 0);
        ew.eq("user_id", accountInfoIndividual.getUserId());

        AccountInfoIndividual accountInfoIndividualDb = accountInfoIndividualService.selectOne(ew);
        if (accountInfoIndividualDb != null) {
            return new ResultVO(ResultCode.RESUBMIT, null);
        }
        if (service.insert(accountInfoIndividual)) {
            return new ResultVO(ResultCode.INSERT_SUCCESS, null);
        } else {
            return new ResultVO(ResultCode.SYS_ERROR, null);
        }
    }
    @PostMapping("updateBaseInfo")
    @OpenLog
    @RolesAllowed({RoleConstant.AUTH, RoleConstant.THIRD, RoleConstant.BASE})
    public ResultVO updateBaseInfo(@RequestBody AccountInfoIndividual accountInfoIndividual){
        //设成 null 防止多地修改 导致状态不一致
        accountInfoIndividual.setAuditStatus(null);

       if( accountInfoIndividualService.updateById(accountInfoIndividual)){
           return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
       }else{
           return new ResultVO(ResultCode.UPDATE_ERROR,null);
       }

    }


}


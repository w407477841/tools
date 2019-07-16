package com.xywg.iot.modular.colud.controller.enterprise;


import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BasalController;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.EnterpriseCertificationDTO;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompany;
import com.xywg.iot.modular.enterprise.service.AccountInfoCompanyService;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * <p>
 * 公司基本信息 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/business/accountInfoCompany")
public class AccountInfoCompanyController extends BasalController<AccountInfoCompany,AccountInfoCompanyService>{

    /**
     * 插入企业认证
     * @return
     */
    @PostMapping("insertEnterpriseCertification")
    @ApiOperation("插入企业认证")
    @OpenLog
    @RolesAllowed({RoleConstant.AUTH,RoleConstant.THIRD,RoleConstant.BASE})
    public ResultVO insertEnterpriseCertification(@RequestBody EnterpriseCertificationDTO enterpriseCertificationDTO){

        return service.insertEnterpriseCertification(enterpriseCertificationDTO);
    }


    /**
     * 获取企业认证
     * @return
     */
    @GetMapping("getEnterpriseCertification")
    @OpenLog
    @ApiOperation("获取企业认证")
    public ResultVO getEnterpriseCertification(@RequestParam String userId){
        return service.getEnterpriseCertification(userId);
    }

    /**
     * 修改企业认证
     * @return
     */
    @PostMapping("updateEnterpriseCertification")
    @ApiOperation("修改企业认证")
    @OpenLog
    public ResultVO updateEnterpriseCertification(@RequestBody EnterpriseCertificationDTO enterpriseCertificationDTO){

        return service.updateEnterpriseCertification(enterpriseCertificationDTO);
    }

}


package com.xywg.iot.modular.colud.controller;

import com.xywg.iot.enums.ResultCode;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.SelectByIdDTO;
import com.xywg.iot.modular.colud.service.MyProductFunctionService;
import com.xywg.iot.modular.product.model.ProductMethodParam;
import com.xywg.iot.modular.product.service.IProductMethodParamService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/***
 *@author:jixiaojun
 *DATE:2019/1/11
 *TIME:17:26
 */
@RestController
@RequestMapping("/business/productMethodParam")
public class ProductMethodParamController extends BaseControllerPlus<ProductMethodParam,IProductMethodParamService> {
    public ProductMethodParamController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD};
        this.setQueryRoles(roles);
        this.setDeleteRoles(roles);
        this.setInsertRoles(roles);
        this.setUpdateRoles(roles);
    }

    @Autowired
    private MyProductFunctionService myProductFunctionService;

    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public <T> ResultVO selectById(@RequestBody SelectByIdDTO dto) {
        try {
            return new ResultVO(ResultCode.SUCCESS,myProductFunctionService.selectProductMethodtById(dto.getId()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }
}

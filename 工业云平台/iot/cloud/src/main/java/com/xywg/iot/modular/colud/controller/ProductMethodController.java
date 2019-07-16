package com.xywg.iot.modular.colud.controller;

import com.xywg.iot.dto.DeleteDTO;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.MyProductFunctionService;
import com.xywg.iot.modular.product.model.ProductMethod;
import com.xywg.iot.modular.product.service.IProductMethodService;
import com.xywg.iot.modular.product.vo.FunctionVo;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/business/productMethod")
public class ProductMethodController extends BaseControllerPlus<ProductMethod,IProductMethodService>{
    public ProductMethodController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD};
        this.setQueryRoles(roles);
        this.setDeleteRoles(roles);
    }

    @Autowired
    private MyProductFunctionService myProductFunctionService;

    @ApiOperation("新增")
    @PostMapping("/insertProductMethod")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO insertProductMethod(@RequestBody FunctionVo functionVo) {
        try {
            if(myProductFunctionService.insertProductMethod(functionVo)) {
                return new ResultVO(ResultCode.INSERT_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if(ResultCode.FUNCTION_KEY_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
        }
        return new ResultVO(ResultCode.INSERT_ERROR,null);
    }

    @ApiOperation("编辑")
    @PostMapping("/updateProductMethod")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO updateProductMethod(@RequestBody FunctionVo functionVo) {
        try {
            if(myProductFunctionService.updateProductMethod(functionVo)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if(ResultCode.FUNCTION_KEY_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }

    @ApiOperation("删除")
    @PostMapping("/deleteProductMethod")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO deleteProductMethod(@RequestBody DeleteDTO deleteDTO) {
        try {
            if(myProductFunctionService.deleteMethod(deleteDTO.getIds())) {
                return new ResultVO(ResultCode.DELETE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.DELETE_ERROR,null);
    }
}

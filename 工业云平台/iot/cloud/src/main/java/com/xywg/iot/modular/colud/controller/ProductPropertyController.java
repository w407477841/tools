package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.product.model.ProductProperty;
import com.xywg.iot.modular.product.service.IProductPropertyService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/11
 *TIME:17:26
 */
@RestController
@RequestMapping("/business/productProperty")
public class ProductPropertyController extends BaseControllerPlus<ProductProperty,IProductPropertyService> {
    public ProductPropertyController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD};
        this.setDeleteRoles(roles);
        this.setQueryRoles(roles);
    }

    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO insert(@RequestBody ProductProperty productProperty) {
        try {
            Wrapper<ProductProperty> wrapper = new EntityWrapper<>();
            wrapper.eq("`key`",productProperty.getKey()).eq("product_id",productProperty.getProductId());
            List<ProductProperty> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            if(service.insert(productProperty)) {
                return new ResultVO(ResultCode.INSERT_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.INSERT_ERROR,null);
    }

    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO update(@RequestBody ProductProperty productProperty) {
        try {
            Wrapper<ProductProperty> wrapper = new EntityWrapper<>();
            wrapper.eq("`key`",productProperty.getKey()).eq("product_id",productProperty.getProductId()).ne("id",productProperty.getId());
            List<ProductProperty> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            if(service.updateById(productProperty)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }
}

package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.dto.DeleteDTO;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.ProductTypeService;
import com.xywg.iot.modular.dictionary.model.MasterType;
import com.xywg.iot.modular.dictionary.service.IMasterTypeService;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/12/27
 *TIME:16:44
 */
@RestController
@RequestMapping("/business/productType")
public class ProductTypeController extends BaseControllerPlus<MasterType,IMasterTypeService> {
    public ProductTypeController() {
        String[] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE};
        this.setQueryRoles(roles);
    }
    @Autowired
    private ProductTypeService productTypeService;

    @ApiOperation("获取产品类型列表")
    @PostMapping("/selectProductTypeList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH})
    public ResultVO selectProductTypeList(@RequestBody QueryDTO queryDTO) {
        try {
            return new ResultVO(ResultCode.SUCCESS,productTypeService.selectProductTypeList(queryDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }

    @ApiOperation("新增")
    @Override
    @OpenLog
    @RolesAllowed(RoleConstant.ADMIN)
    public ResultVO insert(@RequestBody MasterType masterType) {
        try {
            Wrapper<MasterType> wrapper = new EntityWrapper<>();
            wrapper.eq("name",masterType.getName());
            List<MasterType> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return new ResultVO(ResultCode.TYPE_NAME_REPEAT,null);
            }
            if(service.insert(masterType)) {
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
    public ResultVO update(@RequestBody MasterType masterType) {
        try {
            Wrapper<MasterType> wrapper = new EntityWrapper<>();
            wrapper.eq("name",masterType.getName()).ne("id",masterType.getId());
            List<MasterType> list = service.selectList(wrapper);
            if(list.size() > 0) {
                return new ResultVO(ResultCode.TYPE_NAME_REPEAT,null);
            }
            if(service.updateById(masterType)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }

    @ApiOperation("删除")
    @Override
    @RolesAllowed(RoleConstant.ADMIN)
    public ResultVO delete(@RequestBody DeleteDTO deleteDTO) {
        try {
            Wrapper<MasterType> wrapper = new EntityWrapper<>();
            wrapper.eq("parent_id",deleteDTO.getIds().get(0));
            if(service.selectList(wrapper).size() > 0) {
                return new ResultVO(ResultCode.CHILDREN_IS_EXIST,null);
            }
            if(service.deleteBatchIds(deleteDTO.getIds())) {
                return new ResultVO(ResultCode.DELETE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.DELETE_ERROR,null);
    }
}

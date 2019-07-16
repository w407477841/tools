package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.*;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.MyProductFunctionService;
import com.xywg.iot.modular.dictionary.dto.MasterEventDTO;
import com.xywg.iot.modular.dictionary.dto.MasterMethodDTO;
import com.xywg.iot.modular.product.bean.ProductFunctionBean;
import com.xywg.iot.modular.product.service.IProductFunctionService;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.jws.Oneway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description 产品功能
 * Date: Created in 14:55 2019/1/3
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("business/productFunction")
public class ProductFunctionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductFunctionController.class);
    @Autowired
    private IProductFunctionService  productFunctionService;
    @Autowired
    private MyProductFunctionService myProductFunctionService ;

    @PostMapping("selectPage")
    @OpenLog
    @RolesAllowed(value={RoleConstant.ADMIN,RoleConstant.AUTH})
    public ResultVO selectPage(@RequestBody QueryPageDTO q){

        Page page = new Page(q.getPageNum(),q.getPageSize());
        List<QueryParam> paramList = q.getParams();
        Map<String,Object> map = new HashMap<>(10);
        if(paramList!=null&&paramList.size()>0){
            for(int i = 0;i < paramList.size();i++) {
                map.put(paramList.get(i).getName(),paramList.get(i).getValue());
            }
        }
        productFunctionService.selectProductFunctionPage(page,map);
        ResultPage<ProductFunctionBean> resultPage  = new ResultPage<>(page.getTotal(),page.getRecords());
        return new ResultVO(ResultCode.SUCCESS,resultPage);
    }
    @PostMapping("deletes")
    @OpenLog
    @RolesAllowed(value=RoleConstant.ADMIN)
    public ResultVO deletes(@RequestBody Map<String,Object> map){
        List<String> dels = (List<String>) map.get("ids");
        try {
            productFunctionService.dels(dels);
            return new ResultVO(ResultCode.DELETE_SUCCESS,null);
        }catch (Exception e){
            return new ResultVO(ResultCode.ERROR,null);
        }
    }

    @PostMapping("insertEvent")
    @OpenLog
    @RolesAllowed(value=RoleConstant.ADMIN)
    public ResultVO insertEvent(@RequestBody MasterEventDTO event){
        try {
            myProductFunctionService.insertEvent(event);
            return new ResultVO(ResultCode.INSERT_SUCCESS,null);
        }catch (Exception e){
            e.printStackTrace();
            if(ResultCode.FUNCTION_KEY_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            return new ResultVO(ResultCode.INSERT_ERROR,null);
        }
    }

    @PostMapping("insertMethod")
    @OpenLog
    @RolesAllowed(value=RoleConstant.ADMIN)
    public ResultVO insertMethod(@RequestBody MasterMethodDTO method){
        try {
            myProductFunctionService.insertMethod(method);
            return new ResultVO(ResultCode.INSERT_SUCCESS,null);
        }catch (Exception e){
            e.printStackTrace();
            if(ResultCode.FUNCTION_KEY_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            return new ResultVO(ResultCode.INSERT_ERROR,null);
        }
    }

    @PostMapping("updateMethod")
    @OpenLog
    @RolesAllowed(value=RoleConstant.ADMIN)
    public ResultVO updateMethod(@RequestBody MasterMethodDTO method){
        try {
            myProductFunctionService.updateMethod(method);
            return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            e.printStackTrace();
            if(ResultCode.FUNCTION_KEY_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            return new ResultVO(ResultCode.UPDATE_ERROR,null);
        }
    }


    @PostMapping("updateEvent")
    @OpenLog
    @RolesAllowed(value=RoleConstant.ADMIN)
    public ResultVO updateEvent(@RequestBody MasterEventDTO event){
        try {
            myProductFunctionService.updateEvent(event);
            return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            e.printStackTrace();
            if(ResultCode.FUNCTION_KEY_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.FUNCTION_KEY_REPEAT,null);
            }
            return new ResultVO(ResultCode.UPDATE_ERROR,null);
        }
    }


    @PostMapping("selectEventById")
    @OpenLog
    @RolesAllowed(value={RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectEventById(@RequestBody SelectByIdDTO selectByIdDTO){

        return new ResultVO(ResultCode.SUCCESS,myProductFunctionService.selectEventById(selectByIdDTO.getId()));

    }

    @PostMapping("selectMethodById")
    @OpenLog
    @RolesAllowed(value={RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectMethodById(@RequestBody SelectByIdDTO selectByIdDTO){

        return new ResultVO(ResultCode.SUCCESS,myProductFunctionService.selectMethodtById(selectByIdDTO.getId()));

    }

    @ApiOperation("获取功能名称下拉列表")
    @PostMapping("/selectFunctionList")
    @OpenLog
    @RolesAllowed(value={RoleConstant.ADMIN,RoleConstant.AUTH})
    public ResultVO selectFunctionList(@RequestBody QueryPageDTO queryDTO){
        try {
            return new ResultVO(ResultCode.SUCCESS,productFunctionService.selectFunctionList(queryDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }

    @ApiOperation("获取标识符下拉列表")
    @PostMapping("/selectKeyList")
    @OpenLog
    @RolesAllowed(value={RoleConstant.ADMIN,RoleConstant.AUTH})
    public ResultVO selectKeyList(@RequestBody QueryDTO queryDTO){
        try {
            return new ResultVO(ResultCode.SUCCESS,productFunctionService.selectKeyList(queryDTO));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }
}

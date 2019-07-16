package com.xywg.iot.modular.colud.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.common.Constant;
import com.xywg.iot.util.UserUtil;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.dto.DeleteDTO;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.MyProductFunctionService;
import com.xywg.iot.modular.product.bean.ProductFunctionBean;
import com.xywg.iot.modular.product.vo.FunctionVo;
import com.xywg.iot.modular.product.vo.ProductInfoVo;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.service.IProductInfoService;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:39 2018/12/21
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/business/product")
public class ProductController extends BaseControllerPlus<ProductInfo,IProductInfoService> {
    @Autowired
    private IProductInfoService productService;
    @Autowired
    private MyProductFunctionService myProductFunctionService;

    public ProductController() {
        String  [] roles = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD};
        this.setQueryRoles(roles);
        this.setDeleteRoles(roles);
    }

    @ApiOperation("新增")
    @Override
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO insert(@RequestBody ProductInfo productInfo) {
        try {
            productInfo.setProductKey(RandomUtil.randomString(32));
            productInfo.setProductSecret(RandomUtil.randomString(32));
            Wrapper<ProductInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("name",productInfo.getName()).eq("create_user", UserUtil.USERID.get());
            List<ProductInfo> list = service.selectList(wrapper);
            //判断重名
            if(list.size() > 0) {
                return new ResultVO(ResultCode.PRODUCT_NAME_REPEAT,null);
            }
            if(service.insert(productInfo)) {
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
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO update(@RequestBody ProductInfo productInfo) {
        try {
            if(myProductFunctionService.updateProduct(productInfo)) {
                return new ResultVO(ResultCode.UPDATE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
            if(ResultCode.PRODUCT_NAME_REPEAT.getMsg().equals(e.getMessage())) {
                return new ResultVO(ResultCode.PRODUCT_NAME_REPEAT,null);
            }
        }
        return new ResultVO(ResultCode.UPDATE_ERROR,null);
    }

    @ApiOperation("启用")
    @PostMapping("/setUse")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO setUse(@RequestBody ProductInfoVo productInfoVo) {
        try {
            if(service.updateBatchById(productInfoVo.getProductInfos())) {
                List<ProductInfo> productInfoList = productInfoVo.getProductInfos();
                List<Integer> ids = new ArrayList<>(10);
                for(int i = 0;i < productInfoList.size();i++) {
                    ids.add(productInfoList.get(i).getId());
                }
                Wrapper<ProductInfo> wrapper = new EntityWrapper<>();
                wrapper.in("id",ids);
                List<ProductInfo> productInfos = service.selectList(wrapper);
                String key = "";
                for(int i = 0;i < productInfos.size();i++) {
                    if(i > 0) {
                        key += ",";
                    }
                    key += productInfos.get(i).getProductKey();
                }
                service.removeCaches(key);
                return new ResultVO(ResultCode.USE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.USE_ERROR,null);
    }

    @ApiOperation("禁用")
    @PostMapping("/setDis")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO setDis(@RequestBody ProductInfoVo productInfoVo) {
        try {
            if(service.updateBatchById(productInfoVo.getProductInfos())) {
                List<ProductInfo> productInfoList = productInfoVo.getProductInfos();
                List<Integer> ids = new ArrayList<>(10);
                for(int i = 0;i < productInfoList.size();i++) {
                    ids.add(productInfoList.get(i).getId());
                }
                Wrapper<ProductInfo> wrapper = new EntityWrapper<>();
                wrapper.in("id",ids);
                List<ProductInfo> productInfos = service.selectList(wrapper);
                String key = "";
                for(int i = 0;i < productInfos.size();i++) {
                    if(i > 0) {
                        key += ",";
                    }
                    key += productInfos.get(i).getProductKey();
                }
                service.removeCaches(key);
                return new ResultVO(ResultCode.DIS_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.DIS_ERROR,null);
    }

    @ApiOperation("查询产品下产品功能列表")
    @PostMapping("/getFunctionList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO getFunctionList(@RequestBody QueryPageDTO queryPageDTO) {
        try {
            Page<FunctionVo> page = new Page<>(queryPageDTO.getPageNum(),queryPageDTO.getPageSize());
            List<FunctionVo> list = myProductFunctionService.getFunctionList(page,queryPageDTO);
            Map<String,Object> map = new HashMap<>(10);
            map.put("list",list);
            map.put("total",page.getTotal());
            return new ResultVO(ResultCode.SUCCESS,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }

    @ApiOperation("获取列表")
    @PostMapping("/selectProduct")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectProduct(@RequestBody QueryPageDTO queryPageDTO) {
        try {
            Page<ProductInfoVo> page = new Page<>(queryPageDTO.getPageNum(),queryPageDTO.getPageSize());
            List<ProductInfoVo> list = myProductFunctionService.selectProduct(page,queryPageDTO);
            Map<String,Object> map = new HashMap<>(10);
            map.put("list",list);
            map.put("total",page.getTotal());
            return new ResultVO(ResultCode.SUCCESS,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }

    @ApiOperation("删除")
    @PostMapping("/deleteProduct")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO deleteProduct(@RequestBody DeleteDTO deleteDTO) {
        try {
            if(myProductFunctionService.deleteProduct(deleteDTO.getIds())) {
                return new ResultVO(ResultCode.DELETE_SUCCESS,null);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.DELETE_ERROR,null);
    }
}

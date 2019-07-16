package com.xywg.iot.modular.product.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modular.product.vo.FunctionVo;
import com.xywg.iot.modular.product.vo.ProductInfoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {
    /**
     * 功能列表
     * @param page
     * @param map
     * @return
     */
    List<FunctionVo> getFunctionList(Page<FunctionVo> page, Map<String, Object> map);

    /**
     * 获取列表
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<ProductInfoVo> selectProduct(Page<ProductInfoVo> page, Map<String, Object> map) throws Exception;
}

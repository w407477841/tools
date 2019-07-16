package com.xywg.iot.modular.product.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.dto.QueryDTO;
import com.xywg.iot.modular.product.bean.ProductFunctionBean;
import com.xywg.iot.modular.product.model.ProductInfo;

import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:14 2019/1/3
 * Modified By : wangyifei
 */
public interface IProductFunctionService extends IService<ProductInfo> {
    /**
     *  分页查询
     * @param page
     * @param map
     * @return
     */
    Page selectProductFunctionPage(Page page, Map<String,Object> map);

    void dels( List<String> dels);

    /**
     * 获取功能名称下拉列表
     * @param queryDTO
     * @return
     */
    List<ProductFunctionBean> selectFunctionList(QueryDTO queryDTO) throws Exception;

    /**
     * 获取标识符下拉列表
     * @param queryDTO
     * @return
     */
    List<ProductFunctionBean> selectKeyList(QueryDTO queryDTO) throws Exception;
}

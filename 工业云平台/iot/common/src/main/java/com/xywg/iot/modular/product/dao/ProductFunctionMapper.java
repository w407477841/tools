package com.xywg.iot.modular.product.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modular.product.bean.ProductFunctionBean;
import com.xywg.iot.modular.product.model.ProductInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:11 2019/1/3
 * Modified By : wangyifei
 */
public interface ProductFunctionMapper  extends BaseMapper<ProductInfo> {

    List<ProductFunctionBean> selectProductFunctionPage(RowBounds rowBounds,Map<String,Object> map);

    /**
     * 获取功能名称下拉列表
     * @param map
     * @return
     */
    List<ProductFunctionBean> selectFunctionList(Map<String,Object> map)throws Exception;

    /**
     * 获取标识符下拉列表
     * @param map
     * @return
     */
    List<ProductFunctionBean> selectKeyList(Map<String,Object> map)throws Exception;
}

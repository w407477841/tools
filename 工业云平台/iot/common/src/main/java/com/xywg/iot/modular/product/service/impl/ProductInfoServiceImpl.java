package com.xywg.iot.modular.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.dao.ProductInfoMapper;
import com.xywg.iot.modular.product.service.IProductInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

    @Override
    @OpenCache(exp = 500,model = "product",clazz= ProductInfo.class)
    public ProductInfo selectCacheOne(String key) {
        Wrapper<ProductInfo> wrapper  = new EntityWrapper<>();
        wrapper.eq("product_key",key);
        return this.selectOne(wrapper);
    }

    @Override
    @RemoveCache(model = "product")
    public void removeCaches(String key) {

    }
}

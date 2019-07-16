package com.xywg.iot.modular.product.service;

import com.xywg.iot.base.ICacheService;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
public interface IProductInfoService extends IService<ProductInfo> ,ICacheService<ProductInfo> {

}

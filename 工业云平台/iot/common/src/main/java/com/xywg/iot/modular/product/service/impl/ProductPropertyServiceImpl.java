package com.xywg.iot.modular.product.service.impl;

import com.xywg.iot.modular.product.model.ProductProperty;
import com.xywg.iot.modular.product.dao.ProductPropertyMapper;
import com.xywg.iot.modular.product.service.IProductPropertyService;
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
public class ProductPropertyServiceImpl extends ServiceImpl<ProductPropertyMapper, ProductProperty> implements IProductPropertyService {

}

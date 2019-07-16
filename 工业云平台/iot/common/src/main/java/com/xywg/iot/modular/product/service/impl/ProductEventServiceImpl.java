package com.xywg.iot.modular.product.service.impl;

import com.xywg.iot.modular.product.model.ProductEvent;
import com.xywg.iot.modular.product.dao.ProductEventMapper;
import com.xywg.iot.modular.product.service.IProductEventService;
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
public class ProductEventServiceImpl extends ServiceImpl<ProductEventMapper, ProductEvent> implements IProductEventService {

}

package com.xywg.iot.modular.colud.controller.enterprise;


import com.xywg.iot.base.BasalController;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyStarProduct;
import com.xywg.iot.modular.enterprise.service.AccountInfoCompanyStarProductService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 明星产品附件 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/business/accountInfoCompanyStarProduct")
public class AccountInfoCompanyStarProductController extends BasalController<AccountInfoCompanyStarProduct,AccountInfoCompanyStarProductService> {

}


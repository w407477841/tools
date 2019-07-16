package com.xywg.iot.modular.colud.controller.enterprise;


import com.xywg.iot.base.BasalController;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyProductDescription;
import com.xywg.iot.modular.enterprise.service.AccountInfoCompanyProductDescriptionService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 产品描述附件 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/business/accountInfoCompanyProductDescription")
public class AccountInfoCompanyProductDescriptionController extends BasalController<AccountInfoCompanyProductDescription,AccountInfoCompanyProductDescriptionService> {

}


package com.xywg.iot.modular.colud.controller.enterprise;


import com.xywg.iot.base.BasalController;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyDescription;
import com.xywg.iot.modular.enterprise.service.AccountInfoCompanyDescriptionService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 企业描述附件 前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/business/accountInfoCompanyDescription")
public class AccountInfoCompanyDescriptionController extends BasalController<AccountInfoCompanyDescription,AccountInfoCompanyDescriptionService> {

}


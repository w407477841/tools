package com.xywg.iot.modular.admin.product.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:31 2019/2/26
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
public class ControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerTest.class);
    @RequestMapping("gettest")
    public Object gettest(){

    return "haha";
    }
}

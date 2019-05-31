package com.example.springbootadminclient.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:52 2019/5/17
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
@Slf4j
public class AdminController {
    @GetMapping("admin1")
    public Object admin1(){

        System.out.println(log.isErrorEnabled());
        System.out.println(log.isWarnEnabled());
        System.out.println(log.isInfoEnabled());
        System.out.println(log.isDebugEnabled());

        return "ok";
    }

}

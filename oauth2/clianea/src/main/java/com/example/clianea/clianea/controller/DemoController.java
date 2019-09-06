package com.example.clianea.clianea.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping
public class DemoController {
    @GetMapping("/go")
    @RolesAllowed(value = {"admin"})
    public String go1(){


        return "ok";
    }
    @RolesAllowed(value = {"user"})
    @GetMapping("/haha")
    public String haha(){
        return "ada";
    }
    @RolesAllowed(value = {"base"})
    @GetMapping("/go1")
    public String go2(){
        return "go2";
    }

}

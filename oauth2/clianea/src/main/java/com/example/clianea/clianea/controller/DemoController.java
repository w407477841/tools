package com.example.clianea.clianea.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DemoController {
    @GetMapping("/go")
    public String go1(){


        return "ok";
    }
    @GetMapping("/haha")
    public String haha(){
        return "ada";
    }
    @GetMapping("/go1")
    public String go2(){
        return "go2";
    }

}

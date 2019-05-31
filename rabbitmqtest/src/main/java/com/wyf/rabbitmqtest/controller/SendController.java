package com.wyf.rabbitmqtest.controller;

import com.wyf.rabbitmqtest.sender.HelloSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:33 2019/4/28
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
public class SendController {
    @Autowired
    HelloSender helloSender;
    @RequestMapping("send")
    public Object send(){

        helloSender.send();
        return "on";
    }
    @RequestMapping("sendTopic")
    public Object sendTopic(String topic){

        helloSender.send(topic);
        return "on";
    }

}

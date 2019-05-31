package com.wyf.rabbitmqtest.rabbitadminconsumer.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 23:32 2019/5/2
 * Modified By : wangyifei
 */
@Component
@Slf4j
public class SpingUtil implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpingUtil.class);


    public static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("spring上下文");
        SpingUtil.applicationContext = applicationContext;
    }
}

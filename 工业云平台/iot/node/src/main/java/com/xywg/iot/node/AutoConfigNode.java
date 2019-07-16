package com.xywg.iot.node;

import com.xywg.iot.node.properties.XyNodeProperties;
import com.xywg.iot.node.service.XyNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:20 2019/3/8
 * Modified By : wangyifei
 */
@Configuration
@ConditionalOnClass(XyNodeProperties.class)
@ConditionalOnProperty(prefix = "xywg.node",value = "enabled",matchIfMissing = true)
@EnableScheduling
public class AutoConfigNode {
    public static final long cycleTime = 60*1000;
    private final XyNodeService  xyNodeService;
    @Autowired
    public AutoConfigNode(XyNodeService xyNodeService) {
        this.xyNodeService = xyNodeService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfigNode.class);
    @Scheduled(fixedDelay = cycleTime)
    public void putNode(){
        xyNodeService.putNode();
    }


}

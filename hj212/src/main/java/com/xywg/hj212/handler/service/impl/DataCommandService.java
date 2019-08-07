package com.xywg.hj212.handler.service.impl;

import com.xywg.hj212.common.enums.CommandEnum;
import com.xywg.hj212.handler.service.BaseCommandService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:04 2019/7/5
 * Modified By : wangyifei
 */
@Component
@Slf4j
public class DataCommandService extends BaseCommandService {


    @Override
    public void exec(String sn, ChannelHandlerContext ctx, Map<String, String> content) {
            for(Map.Entry<String,String> entry: content.entrySet()){
                    log.info("key= [{}] , value = [{}]",entry.getKey(),entry.getValue());
            }
    }

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_ENUM_DATA;
    }
}

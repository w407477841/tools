package com.xywg.hj212.handler.service.impl;

import com.xywg.hj212.common.enums.CommandEnum;
import com.xywg.hj212.handler.service.BaseCommandService;
import io.netty.channel.ChannelHandlerContext;
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
public class DataCommandService extends BaseCommandService {


    @Override
    public void exec(String sn, ChannelHandlerContext ctx, Map<String, String> content) {

    }

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_ENUM_DATA;
    }
}

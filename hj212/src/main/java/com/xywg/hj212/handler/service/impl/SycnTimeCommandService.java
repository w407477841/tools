package com.xywg.hj212.handler.service.impl;


import com.xywg.hj212.common.enums.CommandEnum;
import com.xywg.hj212.handler.service.BaseCommandService;
import com.xywg.hj212.pojo.Hj212Request;
import com.xywg.hj212.utils.CommonMethod;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:31 2019/7/30
 * Modified By : wangyifei
 */
@Component
@Slf4j
public class SycnTimeCommandService  extends BaseCommandService {


    @Override
    public void exec(String sn, ChannelHandlerContext ctx, Map<String, String> content) {
        Hj212Request data = new Hj212Request();
        /*组装数据体*/
        Map<String,String> contentMap =  CommonMethod.packageContent(data.getContents());
        contentMap.put("CN","9013");
        contentMap.put("CP","&& &&");
        ctx.writeAndFlush(data);




    }

    @Override
    public CommandEnum command() {
        return CommandEnum.COMMAND_ENUM_SYCNTIME;
    }
}

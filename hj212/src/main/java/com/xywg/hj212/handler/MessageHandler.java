package com.xywg.hj212.handler;

import cn.hutool.core.util.StrUtil;
import com.xywg.hj212.common.Const;
import com.xywg.hj212.common.enums.CommandEnum;
import com.xywg.hj212.common.enums.LogEnum;
import com.xywg.hj212.common.exceptions.CloseChannelException;
import com.xywg.hj212.handler.service.BaseCommandService;
import com.xywg.hj212.handler.service.TemplateExec;
import com.xywg.hj212.pojo.Hj212Request;
import com.xywg.hj212.utils.CommonMethod;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import static com.xywg.hj212.common.Const.*;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:28 2019/3/26
 * Modified By : wangyifei
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<Hj212Request> {
    private LongAdder linkAccount = new LongAdder();
    @Autowired
    private TemplateExec templateExec;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Hj212Request data) throws Exception {
        /*组装数据体*/
        Map<String,String> contentMap = CommonMethod.packageContent(data.getContents());
        log.info("组装数据体,");
        String sn =  contentMap.get("MN");
        String commCode =  contentMap.get("CN");
        BaseCommandService.actions.forEach(item->{
           if(item.command()==CommandEnum.getEnum(commCode)){
               templateExec.exec(sn,ctx,contentMap,item);
           }
        });
        /*判断拆分包及应答标志*/
        String flag =  contentMap.get("Flag");
        String flagBit = StrUtil.fillBefore(Integer.toBinaryString(Integer.parseInt(flag)),'0',8);
        String v5v0 = StrUtil.sub(flag,0,6);

        ctx.writeAndFlush(data);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有连接创建[{}]",ctx.channel().remoteAddress().toString());
        linkAccount.increment();
        log.info("当前总连接数[{}]",linkAccount.longValue());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("有连接断开[{}]",ctx.channel().remoteAddress().toString());
        linkAccount.decrement();
        log.info("当前总连接数[{}]",linkAccount.longValue());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        if(cause.getMessage().startsWith(CloseChannelException.class.getName())){
            // 需要关闭
            log.info("异常关闭连接");
            ctx.close();
        }




    }


}

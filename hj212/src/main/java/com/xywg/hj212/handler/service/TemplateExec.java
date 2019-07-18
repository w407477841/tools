package com.xywg.hj212.handler.service;


import com.xywg.hj212.common.Const;
import com.xywg.hj212.common.NettySession;
import com.xywg.hj212.common.enums.LogEnum;
import com.xywg.hj212.common.exceptions.CloseChannelException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:57 2019/7/5
 * Modified By : wangyifei
 */
@Slf4j
@Component
public class TemplateExec {


    /**
     * 模板方法 执行
     * @param sn 设备号
     * @param ctx 上下文
     * @param content 数据体
     * @param commandService 逻辑
     */
    public void exec(String sn, ChannelHandlerContext ctx, Map<String,String> content, BaseCommandService commandService ){
        log.info("设备[{}]开始执行[{}]",sn,commandService.command().getCommandCode());
            long startTime = System.currentTimeMillis();
            if(isNotLogin( sn, ctx)){
                log.info("设备[{}],未登录，需要执行登录");
                login( sn, ctx);
            }
            commandService.exec( sn,ctx,content);
            String execTime = (System.currentTimeMillis() - startTime)+"";
        log.info("设备[{}]开始完毕[{}],总耗时[{}]毫秒",sn,commandService.command().getCommandCode(),execTime);
        }
    /**
     * 登录验证
     * @param sn 设备号
     * @param ctx 上下文
     * @return 未登录返回true
     */
        private boolean isNotLogin(String sn, ChannelHandlerContext ctx){
            return !isLogin(sn,ctx);
        }

    /**
     * 登录验证
     * @param sn 设备号
     * @param ctx 上下文
     * @return 登录返回true
     */
        private boolean isLogin(String sn, ChannelHandlerContext ctx){
            NettySession nettySession =  ctx.channel().attr(Const.NETTY_SESSION).get();
            if(nettySession==null){
                // 新连接 还未登录
                if(Const.CHANNEL_MAP.containsKey(sn)){
                    //存在未断开的连接,必须先将老的关闭掉
                    Const.CHANNEL_MAP.get(sn).close();
                    log.error("设备[{}],存在未断开连接",sn);
                    throw new CloseChannelException("存在未断开连接");
                }
            }else{
                 return true;
            }
            return false;
        }

    /**
     * 登录
     * @param sn 设备号
     * @param ctx 上下文
     */
    private void login(String sn, ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        NettySession nettySession = new NettySession(sn);
        channel.attr(Const.NETTY_SESSION).set(nettySession);
        Const.CHANNEL_MAP.put(sn,channel);
        String logMsg = LogEnum.COMMON.format("登录","设备",sn,"登录成功");
        log.info(logMsg);
        log.info("设备[{}] 登录成功",sn);
    }

}

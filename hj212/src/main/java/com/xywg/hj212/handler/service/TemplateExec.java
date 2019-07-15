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
        String logmsg = LogEnum.COMMON.format("开始",sn,"-",commandService.command().getCommandCode(),"-",commandService.command().getCommandName());
        log.info(logmsg);
            long startTime = System.currentTimeMillis();
            if(isNotLogin( sn, ctx)){
                login( sn, ctx);
            }
            commandService.exec( sn,ctx,content);
            String execTime = (System.currentTimeMillis() - startTime)+"";
         logmsg = LogEnum.COMMON.format("结束",sn,"总耗时:",sn,execTime,"毫秒");
         log.info(logmsg);
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
                    String logmsg = LogEnum.COMMON.format("登录","设备",sn,"存在未断开连接");
                    log.error(logmsg);
                    throw new CloseChannelException(logmsg);
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
    }

}

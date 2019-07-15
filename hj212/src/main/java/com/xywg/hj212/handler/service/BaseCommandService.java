package com.xywg.hj212.handler.service;

import com.xywg.hj212.common.enums.CommandEnum;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:55 2019/7/5
 * Modified By : wangyifei
 */
public  abstract class BaseCommandService  implements InitializingBean {
  public static List<BaseCommandService> actions =new ArrayList<>();

 public abstract   void exec(String sn, ChannelHandlerContext ctx, Map<String,String> content);

  public abstract     CommandEnum command();

  @Override
  public void afterPropertiesSet() throws Exception {
      for(BaseCommandService baseCommandService:actions){
          if(baseCommandService.command()==this.command()){
            throw new Exception("指令重复了,检查代码{"+command().getCommandName()+"}");
          }
      }
      actions.add(this);
  }
}

package unpack.line.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:26 2019/5/24
 * Modified By : wangyifei
 */
public class LineHandler extends ChannelHandlerAdapter {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        System.out.println("异常"+cause.getMessage());
        ctx.close();

        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("连上了服务器");
        ctx.writeAndFlush("来了老弟"+System.getProperty("line.separator"));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("断开了服务器");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        System.out.println("msg:"+msg.toString());
        ctx.writeAndFlush("client:"+System.currentTimeMillis()+System.getProperty("line.separator"));
    }
}

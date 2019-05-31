package unpack.delimiter.handler;

import cn.hutool.core.util.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:29 2019/5/27
 * Modified By : wangyifei
 */
public class DelimiterHandler extends ChannelHandlerAdapter{

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        System.out.println("服务端异常");
        ctx.close();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("有东西来了");
        ctx.writeAndFlush("laile");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("断开服务器");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

          byte [] bytes = new byte[((ByteBuf) msg).readableBytes()];
        ((ByteBuf) msg).readBytes(bytes);
        System.out.println("收到服务器消息"+HexUtil.encodeHexStr(bytes));
    }
}

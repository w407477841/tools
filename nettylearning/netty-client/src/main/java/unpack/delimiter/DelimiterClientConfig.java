package unpack.delimiter;


import cn.hutool.core.util.HexUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import unpack.delimiter.handler.DelimiterHandler;
import unpack.line.handler.LineHandler;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:04 2019/5/24
 * Modified By : wangyifei
 */
public class DelimiterClientConfig {

    public static  void main(String [] args){
        NioEventLoopGroup group = new NioEventLoopGroup();


        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(HexUtil.decodeHex("fffffe"))))
                                    .addLast(new DelimiterHandler());
                        }
                    });
            ChannelFuture channelFuture =  bootstrap.connect("127.0.0.1",9090).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            group.shutdownGracefully();

        }

    }



}

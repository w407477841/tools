package unpack.delimiter;


import cn.hutool.core.util.HexUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import unpack.delimiter.handler.DelimiterHandler;
import unpack.line.handler.LineHandler;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:06 2019/5/24
 * Modified By : wangyifei
 */
public class DelimiterServerConfig {

    public  static  void main (String args[]){

        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup,childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(1024,Unpooled.copiedBuffer(HexUtil.decodeHex("fffffe"))))
                                    .addLast(new DelimiterHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024);
            ChannelFuture channelFuture =  serverBootstrap.bind(9090).sync();

            System.out.println("启动完毕");

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
         parentGroup.shutdownGracefully();
         childGroup.shutdownGracefully();
        }


    }


}

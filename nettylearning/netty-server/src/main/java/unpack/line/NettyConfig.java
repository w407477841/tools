package unpack.line;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import sun.misc.HexDumpEncoder;
import unpack.line.handler.LineHandler;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:06 2019/5/24
 * Modified By : wangyifei
 */
public class NettyConfig {

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
                                    // 1024 个字符后未检索到 结束符就报错
                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new LineHandler())
                                    .addFirst(new StringEncoder());


                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024);
            ChannelFuture channelFuture =  serverBootstrap.bind(9090).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
         parentGroup.shutdownGracefully();
         childGroup.shutdownGracefully();
        }


    }


}

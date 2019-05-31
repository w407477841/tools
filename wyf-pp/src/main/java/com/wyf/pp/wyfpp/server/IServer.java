package com.wyf.pp.wyfpp.server;

import io.netty.channel.socket.SocketChannel;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:38 2019/5/28
 * Modified By : wangyifei
 */
public interface IServer {
    void channel(SocketChannel socketChannel);
    void start() throws Exception;

}

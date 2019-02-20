package com.xywg.tools.tcpforward.netty.common;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:26 2019/2/20
 * Modified By : wangyifei
 */
@Data
public class XySession {

    private String serverId;
    private String clientId;
    /**
     * 可以发送
     */
    private boolean ok;

    public XySession(String serverId, String clientId) {
        this.serverId = serverId;
        this.clientId = clientId;
    }
}

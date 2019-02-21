package com.xywg.tools.simulator.netty.common;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:26 2019/2/20
 * Modified By : wangyifei
 */
@Data
public class XySession {

    private String clientId;
    /**
     * 可以发送
     */
    private boolean ok;

    public XySession( String clientId) {
        this.clientId = clientId;
    }
}

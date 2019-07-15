package com.xywg.hj212.common;


import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:49 2019/7/5
 * Modified By : wangyifei
 */
@Data
public class NettySession {

        private String sn;

    public NettySession(String sn) {
        this.sn = sn;
    }

    public NettySession() {
    }
}

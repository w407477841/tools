package com.xywg.hj212.common.exceptions;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:55 2019/7/3
 * Modified By : wangyifei
 */
public class CloseChannelException extends RuntimeException {

    public CloseChannelException() {
    }

    public CloseChannelException(String message) {
        super(message);
    }
}

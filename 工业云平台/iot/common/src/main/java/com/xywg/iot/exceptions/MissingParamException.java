package com.xywg.iot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:10 2019/3/2
 * Modified By : wangyifei
 */
public class MissingParamException extends Exception {

    public MissingParamException() {
    }

    public MissingParamException(String message) {
        super(message);
    }

    public MissingParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingParamException(Throwable cause) {
        super(cause);
    }

    public MissingParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

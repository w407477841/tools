package com.xywg.iot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:13 2019/3/2
 * Modified By : wangyifei
 */
public class NotBooleanException extends  Exception {

    public NotBooleanException() {
    }

    public NotBooleanException(String message) {
        super(message);
    }

    public NotBooleanException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotBooleanException(Throwable cause) {
        super(cause);
    }

    public NotBooleanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

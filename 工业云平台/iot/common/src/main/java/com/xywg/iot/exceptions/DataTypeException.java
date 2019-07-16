package com.xywg.iot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:24 2019/3/2
 * Modified By : wangyifei
 */
public class DataTypeException extends  Exception {

    public DataTypeException() {
    }

    public DataTypeException(String message) {
        super(message);
    }

    public DataTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataTypeException(Throwable cause) {
        super(cause);
    }

    public DataTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

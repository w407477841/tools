package com.xywg.iot.mqtt.modular.response;

import lombok.Data;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:56 2019/3/31
 * Modified By : wangyifei
 */
@Data
public class Response {
    private int code;
    private String message;
    private Object data;
}

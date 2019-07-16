package com.xywg.iot.enums;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:59 2019/3/2
 * Modified By : wangyifei
 */
public enum ExceptionType {
        /**属性*/
      PROPETY ("property","属性"),

        EVENT ("event","事件"),
        METHOD("method","方法");

      private String method;

      private String type;


    ExceptionType(String method, String type) {
        this.method = method;
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

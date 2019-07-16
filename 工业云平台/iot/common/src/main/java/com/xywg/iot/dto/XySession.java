package com.xywg.iot.dto;

import lombok.Data;

import java.util.Objects;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:31 2018/12/11
 * Modified By : wangyifei
 */
@Data
public class XySession {
    /**
     *  每个连接一个id
     */
    private String id;
    /**
     *  productKey
     */
    private String pk;
    /**
     * deviceName
     */
    private String dn;
    /**
     * deviceSecret
     */
    private String ds;

    private String token;
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()){ return false;}
        XySession xySession = (XySession) o;
        return Objects.equals(id, xySession.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}

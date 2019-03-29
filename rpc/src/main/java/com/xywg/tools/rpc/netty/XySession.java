package com.xywg.tools.rpc.netty;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:12 2019/2/22
 * Modified By : wangyifei
 */
@Data
public class XySession {

    private String id ;
    private String appName;

    public XySession(String id) {
        this.id = id;
    }

    public static XySession factory(String id){

        return new XySession(id);
    }

}

package com.xywg.iot.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:43 2018/12/20
 * Modified By : wangyifei
 */
@Data
public class LoginVO {


    private  String  random ;

    private  String user;

    public LoginVO() {
    }

    public LoginVO( String random, String user) {
        this.random = random;
        this.user = user;
    }
}

package com.xywg.iot.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:47 2018/12/20
 * Modified By : wangyifei
 */
@Data
public class TokenVO extends LoginVO{

    private String token;

    public TokenVO( String random, String user, String token) {
        super( random, user);
        this.token = token;
    }
}

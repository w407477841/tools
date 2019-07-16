package com.xywg.iot.modular.netty.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:28 2019/1/15
 * Modified By : wangyifei
 */
@Data
public class CallMethodDTO {

    private String pk;
    private String dn;
    private String method;
    private Map<String,String> params;


}

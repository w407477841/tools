package com.xywg.tools.rpc.netty.common;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:30 2019/2/22
 * Modified By : wangyifei
 */
@Data
public class Protocol {
    /**
     * 应用名
     */
    private String appName;
    /**
     * 调用应用名
     */
    private String toAppName;
    /**
     * 调用服务名
     */
    private String toServerName;

    private String method;

    private String params;


    /**
     * 1 发送
     * 2 返回
     */
    private String status;



}

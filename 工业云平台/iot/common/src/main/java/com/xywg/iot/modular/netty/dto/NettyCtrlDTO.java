package com.xywg.iot.modular.netty.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:43 2019/1/15
 * Modified By : wangyifei
 */
@Data
public class NettyCtrlDTO {
    /**
     *  控制码
     */
    private Integer ctrl ;
    /**
     * 数据
     */
    private String data ;


}

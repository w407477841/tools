package com.xywg.iot.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description 查询参数
 * Date: Created in 13:22 2018/12/29
 * Modified By : wangyifei
 */
@Data
public class QueryParam {

    private String name;
    private String condition;
    private Object value;
}

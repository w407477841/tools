package com.xywg.iot.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:28 2018/12/29
 * Modified By : wangyifei
 */
@Data
public class QueryOrder {
    private boolean open = false;
    private String name;
    private boolean isAsc = false;

}

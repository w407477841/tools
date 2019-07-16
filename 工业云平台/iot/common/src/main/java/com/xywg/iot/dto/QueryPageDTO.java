package com.xywg.iot.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description 分页查询
 * Date: Created in 13:13 2018/12/29
 * Modified By : wangyifei
 */
@Data
public class QueryPageDTO extends  QueryDTO {

        Integer pageSize;
        Integer pageNum;

}

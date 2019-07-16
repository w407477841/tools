package com.xywg.iot.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description 查询
 * Date: Created in 10:06 2018/12/29
 * Modified By : wangyifei
 */
@Data
public class QueryDTO {

    private List<QueryParam> params ;

    private QueryOrder  order;

    private List<String> selectColumns;

}


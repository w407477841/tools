package com.honghu.oauth2.dto;

import lombok.Data;

import java.util.List;

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


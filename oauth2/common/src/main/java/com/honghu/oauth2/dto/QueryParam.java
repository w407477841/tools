package com.honghu.oauth2.dto;

import lombok.Data;

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

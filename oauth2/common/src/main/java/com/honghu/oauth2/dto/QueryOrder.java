package com.honghu.oauth2.dto;

import lombok.Data;

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
    private boolean asc = false;

}

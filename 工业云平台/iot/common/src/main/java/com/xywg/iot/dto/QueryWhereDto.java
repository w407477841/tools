package com.xywg.iot.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hjy
 * @date 2019/1/4
 */
@Data
public class QueryWhereDto<T> {

    private Integer id;

    private List<Integer> ids;

    private Integer pageSize;

    private Integer pageNum;

    private T body;

    private String keyWord;

    private Integer deviceId;

    private Integer productId;
}

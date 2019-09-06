package com.honghu.oauth2.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;


@Data
@Accessors(chain = true)
public class RequestDTO<T> {

    private Map<String,Object> query;

    private T  body;


}

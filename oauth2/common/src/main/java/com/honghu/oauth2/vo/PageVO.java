package com.honghu.oauth2.vo;

import lombok.Data;

import java.util.List;
@Data
public class PageVO {

    private Long total;
    private List records;

    public PageVO(Long total, List records) {
        this.total = total;
        this.records = records;
    }


}

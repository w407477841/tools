package com.xywg.iot.modular.system.model;

import lombok.Data;

/**
 * @author hjy
 * @date 2019/1/8
 * 区域 基础 实体
 */
@Data
public class Areas {

    private Integer id;

    private String areaName;

    private Integer parentId;

    private String shortName;

    private Integer areaCode;

    private Integer zipCode;

    private String pinyin;

    private String lng;

    private String lat;

    private Integer level;

    private String position;

    private Integer sort;


}

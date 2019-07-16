package com.xywg.iot.modular.product.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xywg.iot.modular.product.model.ProductEventParam;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.model.ProductMethodParam;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/10
 *TIME:17:33
 */
@Data
public class ProductInfoVo extends ProductInfo {
    /**
     * 产品列表
     */
    @TableField(exist = false)
    private List<ProductInfo> productInfos;

    /**
     * 类型名称
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 设备数量
     */
    @TableField(exist = false)
    private Integer deviceNum;

    /**
     * 接入方式名称
     */
    @TableField(exist = false)
    private String linkTypeName;
}
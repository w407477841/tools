package com.xywg.iot.modular.product.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:58 2019/1/3
 * Modified By : wangyifei
 */
@Data
public class ProductFunctionBean {
    private String id;
    private String productType;
    private String functionType;
    private String functionName;
    private String key;
    private String comments;
    private Date createTime;

}

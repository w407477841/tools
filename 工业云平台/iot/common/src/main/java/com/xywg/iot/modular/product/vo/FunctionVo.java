package com.xywg.iot.modular.product.vo;

import com.xywg.iot.modular.product.model.ProductEventParam;
import com.xywg.iot.modular.product.model.ProductMethodParam;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/14
 *TIME:9:49
 */
@Data
public class FunctionVo {
    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 产品类型id
     */
    private Integer typeId;

    /**
     * 标识符
     */
    private String key;

    /**
     * 备注
     */
    private String comments;

    /**
     * 事件类型
     */
    private Integer eventType;

    /**
     * 参数列表
     */
    private List<ProductEventParam> list;

    /**
     * 输入参数列表
     */
    private List<ProductMethodParam> inList;

    /**
     * 输出参数列表
     */
    private List<ProductMethodParam> outList;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 最小值
     */
    private String min;

    /**
     * 最大值
     */
    private String max;

    /**
     * 步长
     */
    private String step;

    /**
     * 单位
     */
    private Integer unit;

    /**
     * 读写类型
     */
    private Integer wrType;

    /**
     * 调用方式
     */
    private Integer isSync;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 功能名称
     */
    private String functionType;

    /**
     * 产品id
     */
    private Integer productId;

    /**
     * 报警表达式
     */
    private String alarmExpression;
}

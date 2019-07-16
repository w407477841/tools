package com.xywg.iot.modular.device.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xywg.iot.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_device_error_log")
public class DeviceErrorLog extends BaseEntity<DeviceErrorLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("device_no")
    private String deviceNo;

    @TableField("product_key")
    private String productKey;

    @TableField("company")
    private String company;

    @TableField("product_type")
    private String productType;

    @TableField("details")
    private String details;

    @TableField("device_name")
    private String deviceName;

    @TableField("product_name")
    private String productName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

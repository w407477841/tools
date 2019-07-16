package com.xywg.iot.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xywg.iot.modular.device.model.DeviceInfo;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/4
 *TIME:10:25
 */
@Data
public class DeviceInfoVo extends DeviceInfo {
    /**
     * 设备集合
     */
    @TableField(exist = false)
    private List<DeviceInfo> deviceInfos;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 接入方式
     */
    @TableField(exist = false)
    private Integer linkType;

    /**
     * 类型名称
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 联网方式名称
     */
    @TableField(exist = false)
    private String linkTypeName;

    /**
     * 产品唯一编号
     */
    @TableField(exist = false)
    private String productKey;

    /**
     * 状态名称
     */
    @TableField(exist = false)
    private String statusName;
}

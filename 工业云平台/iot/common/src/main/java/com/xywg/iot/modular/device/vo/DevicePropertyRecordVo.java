package com.xywg.iot.modular.device.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xywg.iot.modular.device.model.DevicePropertyRecord;
import lombok.Data;

import java.util.Date;

/***
 *@author:jixiaojun
 *DATE:2019/1/8
 *TIME:17:50
 */
@Data
public class DevicePropertyRecordVo extends DevicePropertyRecord {
    /**
     * 设备编号
     */
    @TableField(exist = false)
    private String deviceNo;



    /**
     * 页数
     */
    @TableField(exist = false)
    private Integer pageNum;

    /**
     * 每页条数
     */
    @TableField(exist = false)
    private Integer pageSize;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date beginDate;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endDate;
}

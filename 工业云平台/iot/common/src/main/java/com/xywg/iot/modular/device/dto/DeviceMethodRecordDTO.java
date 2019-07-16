package com.xywg.iot.modular.device.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:48 2019/3/19
 * Modified By : wangyifei
 */
@Data
public class DeviceMethodRecordDTO {

    /**
     * 设备编号
     */
    private Integer id;



    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 每页条数
     */

    private Integer pageSize;


    private Date beginDate;


    private Date endDate;

}

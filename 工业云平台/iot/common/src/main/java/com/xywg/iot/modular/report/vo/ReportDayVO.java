package com.xywg.iot.modular.report.vo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:05 2019/1/14
 * Modified By : wangyifei
 */
@Data
public class ReportDayVO {


    private String reportUser;
    private Date reportDate;
    private Integer addAccount;
    private Integer total;
    private Integer dataAccount;


}

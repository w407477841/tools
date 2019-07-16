package com.xywg.iot.modular.timer.job;

import cn.hutool.core.date.DateUtil;
import com.xywg.iot.modular.report.model.ReportDay;
import com.xywg.iot.modular.report.service.IReportDayService;
import com.xywg.iot.modular.report.vo.ReportDayVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:29 2019/1/11
 * Modified By : wangyifei
 */
@Component
public class DayJob {
    @Autowired
    IReportDayService  reportDayService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void tongji(){

        String yesterday = DateUtil.offsetDay(new Date(),-1).toString("yyyy-MM-dd");

        List<ReportDayVO> reportDayVOList =  reportDayService.selectDailyAddDevice(yesterday);
        List<ReportDay> list = new ArrayList<>();
        reportDayVOList.forEach( item ->{
            ReportDay reportDay = new ReportDay();
            BeanUtils.copyProperties(item,reportDay);
            list.add(reportDay);
        });
        if(list.size()>0) {
            reportDayService.insertBatch(list);
        }
    }

}

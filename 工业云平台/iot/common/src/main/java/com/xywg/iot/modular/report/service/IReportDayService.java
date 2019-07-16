package com.xywg.iot.modular.report.service;

import com.xywg.iot.modular.report.model.ReportDay;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modular.report.vo.ReportDayVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyifei
 * @since 2019-01-14
 */
public interface IReportDayService extends IService<ReportDay> {
    /**
     *
     * @param daily
     * @return
     */
    List<ReportDayVO> selectDailyAddDevice( String daily);

}

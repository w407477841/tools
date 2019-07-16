package com.xywg.iot.modular.report.service.impl;

import com.xywg.iot.modular.report.model.ReportDay;
import com.xywg.iot.modular.report.dao.ReportDayMapper;
import com.xywg.iot.modular.report.service.IReportDayService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modular.report.vo.ReportDayVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyifei
 * @since 2019-01-14
 */
@Service
public class ReportDayServiceImpl extends ServiceImpl<ReportDayMapper, ReportDay> implements IReportDayService {

    @Override
    public List<ReportDayVO> selectDailyAddDevice(String daily) {
        return baseMapper.selectDailyAddDevice(daily);
    }
}

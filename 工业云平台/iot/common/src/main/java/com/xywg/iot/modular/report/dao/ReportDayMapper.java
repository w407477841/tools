package com.xywg.iot.modular.report.dao;

import com.xywg.iot.modular.report.model.ReportDay;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modular.report.vo.ReportDayVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyifei
 * @since 2019-01-14
 */
public interface ReportDayMapper extends BaseMapper<ReportDay> {
    /**
     *  查询每日统计
     * @param daily
     * @return
     */
     List<ReportDayVO> selectDailyAddDevice(@Param("daily") String daily);

}

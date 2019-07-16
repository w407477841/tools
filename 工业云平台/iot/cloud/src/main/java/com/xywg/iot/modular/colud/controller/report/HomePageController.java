package com.xywg.iot.modular.colud.controller.report;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.report.dto.AddDeviceLineDTO;
import com.xywg.iot.modular.report.dto.DataDeviceLineDTO;
import com.xywg.iot.modular.report.dto.TotalDeviceLineDTO;
import com.xywg.iot.modular.report.service.IReportDayService;
import com.xywg.iot.modular.report.vo.AddDeviceLineVO;
import com.xywg.iot.modular.report.vo.DataDeviceLineVO;
import com.xywg.iot.modular.report.vo.DeviceAccountVO;
import com.xywg.iot.modular.report.vo.TotalDeviceLineVO;
import com.xywg.iot.util.UserUtil;
import com.xywg.iot.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:33 2019/1/14
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("business/homepage")
public class HomePageController {


    private static final Logger LOGGER = LoggerFactory.getLogger(HomePageController.class);
    @Autowired
    IReportDayService  reportDayService;
    @Autowired
    IDeviceInfoService  deviceInfoService;

    /**
     *  设备新增折线图
     * @return
     */
    @PostMapping("addDeviceLine")
    @OpenLog
    @RolesAllowed(value = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE})
    public ResultVO addDeviceLine(@RequestBody AddDeviceLineDTO addDeviceLineDTO){
        //一周前

        String user = UserUtil.USERID.get() ;
        //String user = "0" ;
        Wrapper wrapper =new EntityWrapper();
        wrapper.eq("report_user",user);
        wrapper.ge("report_date",addDeviceLineDTO.getBegin());
        wrapper.le("report_date",addDeviceLineDTO.getEnd());
        wrapper.orderBy("report_date",true);
        wrapper.setSqlSelect("report_date as reportDate ","add_account as account");
        List<AddDeviceLineVO> list =  reportDayService.selectMaps(wrapper);
        return new ResultVO(ResultCode.SUCCESS,list);
    }


    /**
     * 设备累计折线图
     * @param totalDeviceLineDTO
     * @return
     */
    @PostMapping("totalDeviceLine")
    @RolesAllowed(value = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE})
    public ResultVO totalDeviceLine(@RequestBody TotalDeviceLineDTO totalDeviceLineDTO){
        //一周前

        String user = UserUtil.USERID.get() ;
        //String user = "0" ;
        Wrapper wrapper =new EntityWrapper();
        wrapper.eq("report_user",user);
        wrapper.ge("report_date",totalDeviceLineDTO.getBegin());
        wrapper.le("report_date",totalDeviceLineDTO.getEnd());
        wrapper.orderBy("report_date",true);
        wrapper.setSqlSelect("report_date as reportDate ","total as account");
        List<TotalDeviceLineVO> list =  reportDayService.selectMaps(wrapper);
        return new ResultVO(ResultCode.SUCCESS,list);
    }


    /**
     *数据量折线图
     * @return
     */
    @PostMapping("dataAccountLine")
    @RolesAllowed(value = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE})
    public ResultVO dataAccountLine(@RequestBody DataDeviceLineDTO dataDeviceLineDTO){

        String user = UserUtil.USERID.get() ;
       // String user = "0" ;
        Wrapper wrapper =new EntityWrapper();
        wrapper.eq("report_user",user);
        wrapper.ge("report_date",dataDeviceLineDTO.getBegin());
        wrapper.le("report_date",dataDeviceLineDTO.getEnd());
        wrapper.orderBy("report_date",true);
        wrapper.setSqlSelect("report_date as reportDate ","data_account as account");
        List<DataDeviceLineVO> list =  reportDayService.selectMaps(wrapper);
        return new ResultVO(ResultCode.SUCCESS,list);
    }

    /**
     *  合计
     * @return
     */

    @PostMapping("deviceTotal")
    @RolesAllowed(value = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE})
    public ResultVO deviceTotal(){
        String yesterday = DateUtil.offsetDay(new Date(),-1).toString("yyyy-MM-dd");
        String user = UserUtil.USERID.get() ;
      //  String user = "0" ;
        // 截止到昨天的所有新增设备
        Wrapper wrapper =new EntityWrapper();
        wrapper.eq("report_user",user);
        wrapper.le("report_date",yesterday) ;
        wrapper.setSqlSelect("sum(add_account) as account");

        BigDecimal yesterdayAccount = (BigDecimal) reportDayService.selectObj(wrapper);
        if(yesterdayAccount == null) {
            yesterdayAccount = new BigDecimal(0);
        }
        // 截止到昨天往前7天的所有新增设备
        String lastWeek = DateUtil.offsetDay(new Date(),-8).toString("yyyy-MM-dd");
        wrapper =new EntityWrapper();
        wrapper.eq("report_user",user);
        wrapper.le("report_date",lastWeek) ;
        wrapper.setSqlSelect("sum(add_account) as account");

        BigDecimal lastWeekAccount = (BigDecimal) reportDayService.selectObj(wrapper);
        if(lastWeekAccount == null){
            lastWeekAccount = new BigDecimal(0);
        }
        // 截止到昨天往前14天的所有新增设备
        String last2Week = DateUtil.offsetDay(new Date(),-15).toString("yyyy-MM-dd");
        wrapper =new EntityWrapper();
        wrapper.eq("report_user",user);
        wrapper.le("report_date",last2Week) ;
        wrapper.setSqlSelect("sum(add_account) as account");

        BigDecimal last2WeekAccount = (BigDecimal) reportDayService.selectObj(wrapper);
        DeviceAccountVO deviceAccountVO  =new DeviceAccountVO();
        deviceAccountVO.setTotal(yesterdayAccount.intValue());
        deviceAccountVO.setNewAdd(yesterdayAccount.subtract(lastWeekAccount).intValue());

        if(last2WeekAccount==null){
            last2WeekAccount = new BigDecimal(0);
        }


            BigDecimal bigDecimalLast2WeekAccount = lastWeekAccount.subtract(last2WeekAccount);
            if(bigDecimalLast2WeekAccount.compareTo(new BigDecimal(0))==0){
                //分母为0
                deviceAccountVO.setHuanbi("/");
            }else{
                BigDecimal bigDecimalLastWeekAccount  = yesterdayAccount.subtract(lastWeekAccount);
                deviceAccountVO.setHuanbi(bigDecimalLastWeekAccount.subtract(bigDecimalLast2WeekAccount).divide(bigDecimalLast2WeekAccount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(1,RoundingMode.HALF_UP).toString()+"%");
            }
        return new ResultVO(ResultCode.SUCCESS,deviceAccountVO);
    }

    /**
     * 设备位置饼图
     * @return
     */
    @PostMapping("deviceAddrPie")
    @RolesAllowed(value = {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.BASE})
    public ResultVO deviceAddrPie(){

        String user = UserUtil.USERID.get() ;


        return new ResultVO(ResultCode.SUCCESS,deviceInfoService.devceAddrTop5(user));
    }



}

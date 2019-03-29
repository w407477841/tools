package com.xywg.test.ceshi.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.xywg.test.ceshi.common.Const;
import com.xywg.test.ceshi.netty.Client;
import com.xywg.test.ceshi.thread.TimerBaseThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:54 2019/3/13
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
public class ClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);


    @RequestMapping(value="online")
    public Object online(){
            StringBuffer sb = new StringBuffer();
        for(String key:Const.clientChannels.keySet()){
            sb.append(key);
            sb.append("\r\n");
        }

        return sb.toString();

    }
    @PostMapping(value="start")
    public Object start(@RequestBody Map<String,String> map){
        Long sleep = Long.parseLong(map.get("sleep"));
        String sns  = map.get("sns");

        if(sleep==null||sleep.equals(0)){
            sleep = 30L;
        }
        if(StrUtil.isBlank(sns)){
            return "我需要参数`sns` 设备号以`，`分割；" +
                    "例如：sns=123456789,234567893";
        }
        String arraySn []=sns.split(",");

        for(String sn :arraySn){
            if(Const.clientChannels.containsKey(sn)){
                System.out.println("sn:"+sn+"已经开过啦");
                continue;
            }
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new Client(sn).start();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).start();

            // 开个线程一直发东西
            Const.threadPoolExecutor.execute(new TimerBaseThread(sn,sleep));
        }



        return "快去看看设备平台看看在线情况" +
                "再去192.168.1.64:15555 看看队列堆积情况";
    }


    @RequestMapping(value="sql")
    public Object online(@RequestBody Map<String,String> map){

     String  template =   "开始 INSERT INTO `device_platform`.`t_project_environment_monitor` (" +
                "`project_id`," +
                "`device_no`," +
                "`gprs`," +
                "`identifier`," +
                "`specification`," +
                "`owner`," +
                "`manufactor`," +
                "`pm10`," +
                "`pm25`," +
                "`noise`," +
                "`wind_speed`," +
                "`wind_direction`," +
                "`temperature`," +
                "`humidity`," +
                "`tsp`," +
                "`wind_force`," +
                "`atmospheric`," +
                "`is_online`," +
                "`status`," +
                "`place_point`," +
                "`comments`," +
                "`is_del`," +
                "`create_time`," +
                "`create_user`," +
                "`modify_time`," +
                "`modify_user`," +
                "`org_id`," +
                "`temperature_min`," +
                "`temperature_max`," +
                "`humidity_min`," +
                "`humidity_max`," +
                "`firmware_version`," +
                "`upgrade_time`," +
                "`name`" +
                ")" +
                "VALUES" +
                "(" +
                "'58'," +
                "'#deviceno#'," +
                "NULL," +
                "NULL," +
                "'test'," +
                "NULL," +
                "'南通'," +
                "'11.00'," +
                "'2.00'," +
                "'333.00'," +
                "'33.00'," +
                "NULL," +
                "NULL," +
                "NULL," +
                "'22.00'," +
                "NULL," +
                "NULL," +
                "'0'," +
                "'1'," +
                "'120.885961,31.993715'," +
                "NULL," +
                "'0'," +
                "now()," +
                "'1'," +
                "now()," +
                "'1'," +
                "'1'," +
                "'10.00'," +
                "'100.00'," +
                "'33.00','44.00'," +
                "NULL," +
                "NULL," +
                "NULL" +
                "); 结束";
        List<String > list = new LinkedList();
        String deviceNo = map.get("deviceNo");
        Long startNum =Long.parseLong(deviceNo);

        for(long i=0;i<100;i++){
            list.add(template.replace("#deviceno#",startNum+i+""));
        }

        return list;

    }

}

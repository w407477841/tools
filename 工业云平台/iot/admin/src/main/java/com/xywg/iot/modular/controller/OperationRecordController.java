package com.xywg.iot.modular.controller;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.modular.model.OperationRecord;
import com.xywg.iot.modular.model.ParamsRecord;
import com.xywg.iot.modular.service.IOperationRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:11 2019/3/22
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/")
public class OperationRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRecordController.class);

    private final  IOperationRecordService operationRecordService;

    public OperationRecordController(IOperationRecordService operationRecordService) {
        this.operationRecordService = operationRecordService;
    }
    @RequestMapping("insert")
    public Object insert(){
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setCollecTime(new Date());
        operationRecord.setContent("cuowu");
        List<ParamsRecord> list = new LinkedList<>();
        ParamsRecord paramsRecord= new ParamsRecord();
        paramsRecord.setValue("20");
        paramsRecord.setKey("temp");
        paramsRecord.setName("温度");
        list.add(paramsRecord);
        operationRecord.setParams(list);
        operationRecordService.saveObject(operationRecord);
        return "ok";
    }

    @RequestMapping("list")
    public Object list(String content){
        List<OperationRecord> list = operationRecordService.selectList(content);
        System.out.println(JSONUtil.parseArray(list).toString());
        return list;
    }


}

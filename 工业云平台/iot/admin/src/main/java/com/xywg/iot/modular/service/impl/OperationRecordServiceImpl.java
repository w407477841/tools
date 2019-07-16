package com.xywg.iot.modular.service.impl;

import com.xywg.iot.modular.dao.OperationRecordRepository;
import com.xywg.iot.modular.model.OperationRecord;
import com.xywg.iot.modular.service.IOperationRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:59 2019/3/22
 * Modified By : wangyifei
 */
@Service
public class OperationRecordServiceImpl implements IOperationRecordService {


    private final OperationRecordRepository operationRecordRepository;

    @Autowired
    public OperationRecordServiceImpl(OperationRecordRepository operationRecordRepository) {
        this.operationRecordRepository = operationRecordRepository;
    }

    @Override
    public void saveObject(OperationRecord operationRecord){

        operationRecordRepository.save(operationRecord);

    }

    @Override
    public List<OperationRecord> selectList(String content) {
        return operationRecordRepository.findByContent(content);
    }

}

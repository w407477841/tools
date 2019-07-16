package com.xywg.iot.modular.service;

import com.xywg.iot.modular.model.OperationRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:59 2019/3/22
 * Modified By : wangyifei
 */
public interface IOperationRecordService {

    void saveObject(OperationRecord operationRecord);

    List<OperationRecord> selectList(String content);

}

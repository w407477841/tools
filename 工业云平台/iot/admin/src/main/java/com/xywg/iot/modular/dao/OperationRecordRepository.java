package com.xywg.iot.modular.dao;

import com.xywg.iot.modular.model.OperationRecord;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:06 2019/3/23
 * Modified By : wangyifei
 */
@Repository
public interface OperationRecordRepository extends MongoRepository<OperationRecord,ObjectId> {
    /**
     *
     * @param content
     * @return
     */
    List<OperationRecord> findByContent(@Param("content") String content);

}

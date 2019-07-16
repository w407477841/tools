package com.xywg.iot.modular.model;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:57 2019/3/22
 * Modified By : wangyifei
 */
@Data
@Document("operation_record")
public class OperationRecord {
    @Id
    private String id;
    @Field("collect_time")
    private Date collecTime;
    @Field("hash_id")
    private String hashId;

    private String content;

    private List<ParamsRecord> params;

}

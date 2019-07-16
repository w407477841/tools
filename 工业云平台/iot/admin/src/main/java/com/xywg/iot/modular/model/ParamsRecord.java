package com.xywg.iot.modular.model;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:01 2019/3/23
 * Modified By : wangyifei
 */
@Data
public class ParamsRecord {

    private String value;
    private String key;
    private String name;
}

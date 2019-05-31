package com.xywg.hj212.pojo;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:13 2019/4/9
 * Modified By : wangyifei
 */
@Data
public class Hj212Request {

    private String header;
    private String length;
    private Map<String,String> contents;
    private String  crc;
    private String footer;



}

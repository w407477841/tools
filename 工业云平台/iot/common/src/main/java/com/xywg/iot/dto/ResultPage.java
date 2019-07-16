package com.xywg.iot.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description 分页返回实体
 * Date: Created in 13:09 2018/12/29
 * Modified By : wangyifei
 */
@Data
public class ResultPage<T> {
    long total ;
    List<T> list;
    public ResultPage(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}

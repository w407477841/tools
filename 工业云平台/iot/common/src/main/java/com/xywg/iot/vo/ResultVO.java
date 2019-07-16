package com.xywg.iot.vo;

import com.xywg.iot.enums.ResultCode;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:46 2018/12/20
 * Modified By : wangyifei
 */
@Data
public class ResultVO {

    private String msg;
    private Integer code;
    private Object data;

    public ResultVO(ResultCode resultCode, Object data) {
        this(resultCode.getMsg(),resultCode.getCode(),data);
    }

    public ResultVO(String msg, Integer code, Object data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }


}

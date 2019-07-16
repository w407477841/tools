package com.xywg.iot.util;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.dto.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:34 2018/12/29
 * Modified By : wangyifei
 */
public class BaseControllerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseControllerUtil.class);

    public static void packageParam( List<QueryParam> params , Wrapper wrapper ){
        if(params!=null){
            params.forEach(param->{
                switch(param.getCondition()){
                    case ">":
                        wrapper.gt(param.getName(),param.getValue());
                        break;
                    case ">=":
                        wrapper.ge(param.getName(),param.getValue());
                        break;
                    case "=":
                        wrapper.eq(param.getName(),param.getValue());
                        break;
                    case "<":
                        wrapper.lt(param.getName(),param.getValue());
                        break;
                    case "<=":
                        wrapper.le(param.getName(),param.getValue());
                        break;
                    case "like":
                        wrapper.like(param.getName(),param.getValue().toString());
                        break;
                    case "where":
                        wrapper.where(param.getName(),param.getValue().toString().split(","));
                        break;
                }
            });
        }

    }

}

package com.xywg.iot.dto;

import cn.hutool.core.util.RandomUtil;
import com.xywg.iot.enums.DeviceLoginStatus;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * method =
 * login 登录
 * property  属性上报
 * method.{methodName} 方法执行
 * event.{eventName}  事件上报
 * Date: Created in 10:27 2018/12/11
 * Modified By : wangyifei
 */
@Data
public class DataDTO {

    private String method;
    private String uuid;
    private String version;
    private String token ;
    private Map<String ,String> params;

    public static DataDTO loginResult(DeviceLoginStatus status,String token){
        DataDTO dataDTO =new DataDTO();
        dataDTO.setMethod("login");
        Map<String ,String> params =new HashMap<>(1);
        params.put("code",status.getStatus());
        params.put("token",token);
        dataDTO.setParams(params);
        return dataDTO;

    }
    public static DataDTO loginResult(DeviceLoginStatus status){
        DataDTO dataDTO =new DataDTO();
        dataDTO.setMethod("login");
        Map<String ,String> params =new HashMap<>(1);
        params.put("code",status.getStatus());
        dataDTO.setParams(params);
        return dataDTO;

    }



}

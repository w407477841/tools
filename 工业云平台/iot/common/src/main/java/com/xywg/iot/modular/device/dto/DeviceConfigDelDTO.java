package com.xywg.iot.modular.device.dto;

import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:19 2019/4/23
 * Modified By : wangyifei
 */
@Data
public class DeviceConfigDelDTO {

    private String fileName;


    /**
     * 设备名称
     */
    private List<DeviceInfoVo> deviceList;

}

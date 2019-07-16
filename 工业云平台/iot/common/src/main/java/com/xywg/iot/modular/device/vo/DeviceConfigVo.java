package com.xywg.iot.modular.device.vo;

import lombok.Data;

import java.util.List;


@Data
public class DeviceConfigVo{

    private static final long serialVersionUID = 1L;

    private Integer productId;

    private List<DeviceInfoVo> deviceList;

	private String  jsonString;

}

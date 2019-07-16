package com.xywg.iot.modular.remote.model.vo;

import com.xywg.iot.modular.remote.model.DeviceRemoteConfigRecord;
import lombok.Data;

import java.util.Map;

/**
 * @author hjy
 * @since 2019-01-15
 */
@Data

public class DeviceRemoteConfigRecordVO extends DeviceRemoteConfigRecord {

	private String productName;
	private String deviceName;

	private String productKey;

	private String deviceNo;

	private Map params;


}

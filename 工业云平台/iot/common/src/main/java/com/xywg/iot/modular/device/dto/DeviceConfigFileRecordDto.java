package com.xywg.iot.modular.device.dto;

import com.xywg.iot.base.BaseEntity;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author hjy
 * @since 2019-01-04
 */
@Data
public class DeviceConfigFileRecordDto extends BaseEntity<DeviceConfigFileRecordDto> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增
     */
	private Integer id;
    /**
     * 设备名称
     */
	private List<DeviceInfoVo> deviceList;
    /**
     * 产品名称
     */
	private Integer productId;
    /**
     * 升级包ID
     */
	private Integer packageId;

	/**
	 * 升级包路径
	 */
	private String path;
    /**
     * 下发标识
     */
	private Integer issuedFlag;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

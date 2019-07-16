package com.xywg.iot.modular.remote.model.vo;

import com.xywg.iot.base.BaseEntity;
import com.xywg.iot.modular.product.model.ProductInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hjy
 * @date 2019/1/25
 */
@Data
public class DeviceDetailInfoVo extends BaseEntity<DeviceDetailInfoVo> {
    /**
     * 自增
     */
    private Integer id;
    /**
     * 名字
     */
    private String name;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 产品id
     */
    private Integer productId;
    /**
     * 产品详细
     */
    private ProductInfo product;
    /**
     * 设备密钥
     */
    private String deviceSecret;
    /**
     * 设备状态 1：未激活 2：离线 3：在线
     */
    private Integer status;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 固件版本
     */
    private String version;
    /**
     * 添加日期
     */
    private Date addTime;
    /**
     * 激活日期
     */
    private Date activationTime;
    /**
     * 激活日期
     */
    private String position;
    /**
     *  地址  省|市
     */
    private String address ;
    /**
     * 备注
     */
    private String comments;
    /**
     * 最后上线时间
     */
    private Date lastTime;

    /**
     * 升级状态
     */
    private Integer upgradeStatus;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}

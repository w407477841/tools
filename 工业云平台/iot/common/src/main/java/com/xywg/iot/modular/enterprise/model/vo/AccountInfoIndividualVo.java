package com.xywg.iot.modular.enterprise.model.vo;

import com.xywg.iot.modular.enterprise.model.AccountInfoIndividual;
import lombok.Data;

/**
 * @author hjy
 * @date 2019/1/15
 */
@Data
public class AccountInfoIndividualVo extends AccountInfoIndividual {
    /**
     * 省份
     */
    private String provinceName;
    /**
     * 城市
     */
    private String cityName;
    /**
     * 区
     */
    private String districtName;
}

package com.xywg.iot.modular.device.bean;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:08 2019/1/15
 * Modified By : wangyifei
 */
@Data
public class AddrDetail {
    /**
     * 城市
     */
   private String city;
    /**
     * 百度城市代码
     */
    private String city_code;
    /**
     * 区县
     */
    private String district;
    /**
     * 省份
     */
    private String province;
    /**
     * 街道
     */
    private String street;
    /**
     * 门牌号
     */
    private String street_number;

}

package com.xywg.iot.modular.device.bean;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:06 2019/1/15
 * Modified By : wangyifei
 */
@Data
public class AddrContent {

   private String address;
   private AddrDetail address_detail;
   private AddrPoint point;


}

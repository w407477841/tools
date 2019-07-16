package com.xywg.iot.modular.device.bean;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:05 2019/1/15
 * Modified By : wangyifei
 */
@Data
public class Addr {

   private String address;

   private String status;
   private AddrContent content;

}

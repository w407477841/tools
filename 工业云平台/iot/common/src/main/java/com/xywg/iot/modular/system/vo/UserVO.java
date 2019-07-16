package com.xywg.iot.modular.system.vo;

import com.xywg.iot.modular.system.model.SysRole;
import com.xywg.iot.modular.system.model.SysUser;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:26 2019/1/17
 * Modified By : wangyifei
 */
@Data
public class UserVO extends SysUser {

    List<SysRole>  roles ;

}

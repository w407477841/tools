package com.xywg.iot.modular.system.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modular.system.model.Areas;

import java.util.List;

/**
 * @author hjy
 * @date 2019/1/8
 */
public interface AreasService extends IService<Areas> {

    List<Areas> getAreasList(Areas areas);
}

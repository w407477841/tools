package com.xywg.iot.modular.system.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modular.system.model.Areas;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
public interface AreasMapper extends BaseMapper<Areas> {

  List<Areas> getAreasList(Areas areas);

}

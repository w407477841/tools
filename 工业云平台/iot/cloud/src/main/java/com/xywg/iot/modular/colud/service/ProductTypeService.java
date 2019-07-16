package com.xywg.iot.modular.colud.service;

import com.xywg.iot.dto.QueryDTO;
import com.xywg.iot.modular.dictionary.vo.MasterTypeVo;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/12/29
 *TIME:17:16
 */
public interface ProductTypeService {
    /**
     * 获取产品类型列表
     * @param queryDTO
     * @return
     * @throws Exception
     */
    List<MasterTypeVo> selectProductTypeList(QueryDTO queryDTO) throws Exception;
}

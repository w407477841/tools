package com.xywg.iot.modular.colud.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.dto.QueryDTO;
import com.xywg.iot.modular.colud.service.ProductTypeService;
import com.xywg.iot.modular.dictionary.dao.MasterTypeMapper;
import com.xywg.iot.modular.dictionary.model.MasterType;
import com.xywg.iot.modular.dictionary.vo.MasterTypeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2018/12/29
 *TIME:17:18
 */
@Service
public class MyProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    private MasterTypeMapper masterTypeMapper;
    @Override
    public List<MasterTypeVo> selectProductTypeList(QueryDTO queryDTO) throws Exception {
        Wrapper<MasterType> wrapper = new EntityWrapper<>();
        List<MasterType> masterTypes = masterTypeMapper.selectList(wrapper);
        wrapper.eq("parent_id",0);
        List<MasterType> productParentTypes = masterTypeMapper.selectList(wrapper);
        List<MasterTypeVo> masterProductTypeVos = new ArrayList<>(10);
        for(int i = 0; i < masterTypes.size(); i++) {
            MasterTypeVo masterProductTypeVo = new MasterTypeVo();
            BeanUtils.copyProperties(masterTypes.get(i), masterProductTypeVo);
            masterProductTypeVos.add(masterProductTypeVo);
        }
        List<MasterTypeVo> productParentTypeVos = new ArrayList<>(10);
        for(int i = 0;i < productParentTypes.size();i++) {
            MasterTypeVo masterProductTypeVo = new MasterTypeVo();
            BeanUtils.copyProperties(productParentTypes.get(i), masterProductTypeVo);
            productParentTypeVos.add(masterProductTypeVo);
        }
        List<MasterTypeVo> list = new ArrayList<>(10);
        for(int i = 0;i < productParentTypeVos.size();i++) {
            MasterTypeVo masterProductTypeVo = productParentTypeVos.get(i);
            masterProductTypeVo = getChildren(masterProductTypeVo, masterProductTypeVos);
            list.add(masterProductTypeVo);
        }
        return productParentTypeVos;
    }

    private MasterTypeVo getChildren(MasterTypeVo masterProductTypeVo, List<MasterTypeVo> productTypes) {
        List<MasterTypeVo> masterProductTypeVoList = getChildren(masterProductTypeVo.getId(),productTypes);
        List<MasterTypeVo> list = new ArrayList<>(10);
        if(masterProductTypeVoList.size() > 0) {
            for(int i = 0; i < masterProductTypeVoList.size(); i++) {
                MasterTypeVo typeVo = masterProductTypeVoList.get(i);
                typeVo = getChildren(typeVo,productTypes);
                list.add(typeVo);
            }
            masterProductTypeVo.setChildren(list);
        }
        return masterProductTypeVo;
    }

    private List<MasterTypeVo> getChildren(Integer parentId, List<MasterTypeVo> productTypes) {
        List<MasterTypeVo> list = new ArrayList<>(10);
        for(int i = 0;i < productTypes.size();i++) {
            if(parentId.equals(productTypes.get(i).getParentId())) {
                list.add(productTypes.get(i));
            }
        }
        return list;
    }
}

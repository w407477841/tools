package com.xywg.iot.modular.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modular.system.dao.AreasMapper;
import com.xywg.iot.modular.system.model.Areas;
import com.xywg.iot.modular.system.service.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hjy
 * @date 2019/1/8
 */
@Service
public class AreasServiceImpl extends ServiceImpl<AreasMapper, Areas> implements AreasService {
    @Autowired
    private AreasMapper areasMapper;

    @Override
    public List<Areas> getAreasList(Areas areas) {
        return areasMapper.getAreasList(areas);
    }
}

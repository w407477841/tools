package com.xywg.iot.modular.colud.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.modular.colud.service.AuditService;
import com.xywg.iot.modular.enterprise.dao.AccountInfoCompanyDescriptionMapper;
import com.xywg.iot.modular.enterprise.dao.AccountInfoCompanyMapper;
import com.xywg.iot.modular.enterprise.dao.AccountInfoCompanyProductDescriptionMapper;
import com.xywg.iot.modular.enterprise.dao.AccountInfoCompanyStarProductMapper;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyDescription;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyProductDescription;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyStarProduct;
import com.xywg.iot.modular.enterprise.model.vo.AuditVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2019/1/24
 *TIME:13:49
 */
@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AccountInfoCompanyMapper accountInfoCompanyMapper;
    @Autowired
    private AccountInfoCompanyDescriptionMapper accountInfoCompanyDescriptionMapper;
    @Autowired
    private AccountInfoCompanyStarProductMapper accountInfoCompanyStarProductMapper;
    @Autowired
    private AccountInfoCompanyProductDescriptionMapper accountInfoCompanyProductDescriptionMapper;

    @Override
    public List<AuditVo> getAuditList(Page<AuditVo> page,QueryPageDTO queryPageDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        if(queryPageDTO.getParams().size() > 0) {
            map.put("name",queryPageDTO.getParams().get(0).getValue());
        }
        List<AuditVo> list = accountInfoCompanyMapper.getAuditList(page,map);
        for(int i = 0;i < list.size();i++) {
            Wrapper<AccountInfoCompanyDescription> companyDescriptionWrapper = new EntityWrapper<>();
            Wrapper<AccountInfoCompanyStarProduct> starProductWrapper = new EntityWrapper<>();
            Wrapper<AccountInfoCompanyProductDescription> productDescriptionWrapper = new EntityWrapper<>();
            companyDescriptionWrapper.eq("company_id",list.get(i).getId());
            starProductWrapper.eq("company_id",list.get(i).getId());
            productDescriptionWrapper.eq("company_id",list.get(i).getId());
            List<AccountInfoCompanyDescription> companyDescriptions = accountInfoCompanyDescriptionMapper.selectList(companyDescriptionWrapper);
            List<AccountInfoCompanyStarProduct> starProducts = accountInfoCompanyStarProductMapper.selectList(starProductWrapper);
            List<AccountInfoCompanyProductDescription> productDescriptions = accountInfoCompanyProductDescriptionMapper.selectList(productDescriptionWrapper);
            list.get(i).setCompanyFile(companyDescriptions);
            list.get(i).setStarFile(starProducts);
            list.get(i).setProductFile(productDescriptions);
        }
        return list;
    }
}

package com.xywg.iot.modular.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.QueryDTO;
import com.xywg.iot.modular.dictionary.model.MasterEventParam;
import com.xywg.iot.modular.dictionary.model.MasterMethodParam;
import com.xywg.iot.modular.dictionary.service.*;
import com.xywg.iot.modular.product.bean.ProductFunctionBean;
import com.xywg.iot.modular.product.dao.ProductFunctionMapper;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.service.IProductFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:15 2019/1/3
 * Modified By : wangyifei
 */
@Service
public class ProductFunctionServiceImpl extends ServiceImpl<ProductFunctionMapper,ProductInfo> implements IProductFunctionService  {
    @Autowired
    IMasterEventService masterEventService;
    @Autowired
    IMasterMethodService masterMethodService;
    @Autowired
    IMasterPropertyService masterPropertyService;
    @Autowired
    IMasterEventParamService masterEventParamService;
    @Autowired
    IMasterMethodParamService  masterMethodParamService;

    @Override
    public Page selectProductFunctionPage(Page page, Map<String,Object> map) {

        List<ProductFunctionBean> list = baseMapper.selectProductFunctionPage(page,map);

        page.setRecords(list);

        return page;
    }

    @Override
    public void dels(List<String> dels) {
        dels.forEach(item->{

            String funtionType = item.split("#")[1];
            String id = item.split("#")[0];
            switch (funtionType){
                case "2":
                    masterPropertyService.deleteById(Integer.parseInt(id));
                    break;
                case "1":
                    masterEventService.deleteById(Integer.parseInt(id));
                    Wrapper<MasterEventParam> wrapper = new EntityWrapper<>();
                    wrapper.eq("event_id",Integer.parseInt(id));
                    masterEventParamService.delete(wrapper);
                    break;
                case "3" :
                    masterMethodService.deleteById(Integer.parseInt(id));
                    Wrapper<MasterMethodParam> wrapper1 = new EntityWrapper<>();
                    wrapper1.eq("method_id",Integer.parseInt(id));
                    masterMethodParamService.delete(wrapper1);
                    break;
            }
        });

    }

    @Override
    public List<ProductFunctionBean> selectFunctionList(QueryDTO queryDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        return baseMapper.selectFunctionList(map);
    }

    @Override
    public List<ProductFunctionBean> selectKeyList(QueryDTO queryDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        return baseMapper.selectKeyList(map);
    }


}

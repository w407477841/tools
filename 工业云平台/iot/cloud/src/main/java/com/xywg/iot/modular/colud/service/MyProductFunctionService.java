package com.xywg.iot.modular.colud.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.dictionary.dto.MasterEventDTO;
import com.xywg.iot.modular.dictionary.dto.MasterMethodDTO;
import com.xywg.iot.modular.dictionary.model.MasterEvent;
import com.xywg.iot.modular.dictionary.model.MasterEventParam;
import com.xywg.iot.modular.dictionary.model.MasterMethod;
import com.xywg.iot.modular.dictionary.model.MasterMethodParam;
import com.xywg.iot.modular.dictionary.service.IMasterEventParamService;
import com.xywg.iot.modular.dictionary.service.IMasterEventService;
import com.xywg.iot.modular.dictionary.service.IMasterMethodParamService;
import com.xywg.iot.modular.dictionary.service.IMasterMethodService;
import com.xywg.iot.modular.dictionary.vo.MasterEventVO;
import com.xywg.iot.modular.dictionary.vo.MasterMethodVO;
import com.xywg.iot.modular.product.dao.ProductInfoMapper;
import com.xywg.iot.modular.product.model.*;
import com.xywg.iot.modular.product.service.*;
import com.xywg.iot.modular.product.service.impl.ProductEventParamServiceImpl;
import com.xywg.iot.modular.product.service.impl.ProductMethodParamServiceImpl;
import com.xywg.iot.modular.product.vo.FunctionVo;
import com.xywg.iot.modular.product.vo.ProductInfoVo;
import com.xywg.iot.util.UserUtil;
import com.xywg.iot.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:40 2019/1/10
 * Modified By : wangyifei
 */
@Component
public class MyProductFunctionService {
    @Autowired
    private IMasterMethodService  masterMethodService;
    @Autowired
    private IMasterEventService  masterEventService;
    @Autowired
    private IMasterEventParamService  masterEventParamService;
    @Autowired
    private IMasterMethodParamService masterMethodParamService;
    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private IProductEventService productEventService;
    @Autowired
    private IProductMethodService productMethodService;
    @Autowired
    private IProductEventParamService productEventParamService;
    @Autowired
    private IProductMethodParamService productMethodParamService;
    @Autowired
    private IProductPropertyService productPropertyService;

    @Transactional(rollbackFor = Exception.class)
    public void insertEvent(MasterEventDTO event){

        MasterEvent e = new MasterEvent();
        BeanUtils.copyProperties(event,e);
        Wrapper<MasterEvent> wrapper = new EntityWrapper<>();
        wrapper.eq("`key`",e.getKey());
        List<MasterEvent> list = masterEventService.selectList(wrapper);
        if(list.size() > 0) {
            throw new RuntimeException("功能标识符重复");
        }
        masterEventService.insert(e);
        if(event.getList()!=null&&event.getList().size()>0){
        List<MasterEventParam> outParams =  new ArrayList<>();
        event.getList().forEach(item->{
            item.setEventId(e.getId());
            outParams.add(item);
        });


            masterEventParamService.insertBatch(outParams);
        }


    }

    @Transactional(rollbackFor = Exception.class)
    public void insertMethod(MasterMethodDTO method){

        MasterMethod e = new MasterMethod();
        BeanUtils.copyProperties(method,e);
        Wrapper<MasterMethod> wrapper = new EntityWrapper<>();
        wrapper.eq("`key`",e.getKey());
        List<MasterMethod> list = masterMethodService.selectList(wrapper);
        if(list.size() > 0) {
            throw new RuntimeException("功能标识符重复");
        }
        masterMethodService.insert(e);
        if(method.getInList()!=null&&method.getInList().size()>0){
            List<MasterMethodParam> inParams =  new ArrayList<>();
            method.getInList().forEach(item->{
                item.setMethodId(e.getId());
                item.setParamType(1);
                inParams.add(item);
            });
            masterMethodParamService.insertBatch(inParams);
        }
        if(method.getOutList()!=null&&method.getOutList().size()>0){
            List<MasterMethodParam> outParams =  new ArrayList<>();
            method.getOutList().forEach(item->{
                item.setMethodId(e.getId());
                item.setParamType(2);
                outParams.add(item);
            });
            masterMethodParamService.insertBatch(outParams);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMethod(MasterMethodDTO method){

        MasterMethod e = new MasterMethod();
        BeanUtils.copyProperties(method,e);
        Wrapper<MasterMethod> methodWrapper = new EntityWrapper<>();
        methodWrapper.eq("`key`",e.getKey()).ne("id",e.getId());
        List<MasterMethod> list = masterMethodService.selectList(methodWrapper);
        if(list.size() > 0) {
            throw new RuntimeException("功能标识符重复");
        }
        masterMethodService.updateById(e);
        Wrapper<MasterMethodParam> wrapper =new EntityWrapper<>();
        wrapper.eq("method_id",e.getId());
       masterMethodParamService.delete(wrapper);

        if(method.getInList()!=null&&method.getInList().size()>0){
            List<MasterMethodParam> inParams =  new ArrayList<>();
            method.getInList().forEach(item->{
                item.setMethodId(e.getId());
                item.setParamType(1);
                inParams.add(item);
            });
            masterMethodParamService.insertBatch(inParams);
        }
        if(method.getOutList()!=null&&method.getOutList().size()>0){
            List<MasterMethodParam> outParams =  new ArrayList<>();
            method.getOutList().forEach(item->{
                item.setMethodId(e.getId());
                item.setParamType(2);
                outParams.add(item);
            });
            masterMethodParamService.insertBatch(outParams);
        }

    }



    @Transactional(rollbackFor = Exception.class)
    public void updateEvent(MasterEventDTO event){

        MasterEvent e = new MasterEvent();
        BeanUtils.copyProperties(event,e);
        Wrapper<MasterEvent> eventWrapper = new EntityWrapper<>();
        eventWrapper.eq("`key`",e.getKey()).ne("id",e.getId());
        List<MasterEvent> list = masterEventService.selectList(eventWrapper);
        if(list.size() > 0) {
            throw new RuntimeException("功能标识符重复");
        }
        masterEventService.updateById(e);
        Wrapper<MasterEventParam> wrapper =new EntityWrapper<>();
        wrapper.eq("event_id",e.getId());
        masterEventParamService.delete(wrapper);

        if(event.getList()!=null&&event.getList().size()>0){
            List<MasterEventParam> outParams =  new ArrayList<>();
            event.getList().forEach(item->{
                item.setEventId(e.getId());
                outParams.add(item);
            });
            masterEventParamService.insertBatch(outParams);
        }


    }

    public MasterEventVO selectEventById(Integer eventId){
        MasterEventVO masterEventVO  = new MasterEventVO();
        MasterEvent event = masterEventService.selectById(eventId);
        BeanUtils.copyProperties(event,masterEventVO);

        Wrapper<MasterEventParam> wrapper =new EntityWrapper<>();
        wrapper.eq("event_id",eventId);
        wrapper.eq("is_del",0);
        masterEventVO.setList(  masterEventParamService.selectList(wrapper));
        return masterEventVO;

    }

    public MasterMethodVO selectMethodtById(Integer methodId){
        MasterMethodVO masterMethodVO  = new MasterMethodVO();
        MasterMethod method = masterMethodService.selectById(methodId);
        BeanUtils.copyProperties(method,masterMethodVO);

        Wrapper<MasterMethodParam> wrapper =new EntityWrapper<>();
        wrapper.eq("method_id",methodId);
        wrapper.eq("is_del",0);
        wrapper.eq("param_type",1);
        masterMethodVO.setInList(  masterMethodParamService.selectList(wrapper));

        wrapper =new EntityWrapper<>();
        wrapper.eq("method_id",methodId);
        wrapper.eq("is_del",0);
        wrapper.eq("param_type",2);
        masterMethodVO.setOutList(  masterMethodParamService.selectList(wrapper));

        return masterMethodVO;

    }

    /**
     * 查询产品下产品功能列表
     * @param page
     * @param queryPageDTO
     * @return
     */
    public List<FunctionVo> getFunctionList(Page<FunctionVo> page, QueryPageDTO queryPageDTO) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("id",queryPageDTO.getParams().get(0).getValue());
        if(!UserUtil.ISADMIN.get()) {
            map.put("createUser",UserUtil.USERID.get());
        }
        return productInfoMapper.getFunctionList(page,map);
    }

    /**
     * 新增产品事件
     * @param functionVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean insertProductEvent(FunctionVo functionVo) {
        ProductEvent productEvent = new ProductEvent();
        BeanUtils.copyProperties(functionVo,productEvent);
        Wrapper<ProductEvent> wrapper = new EntityWrapper<>();
        wrapper.eq("`key`",productEvent.getKey()).eq("product_id",productEvent.getProductId());
        List<ProductEvent> list = productEventService.selectList(wrapper);
        if(list.size() > 0) {
            throw new RuntimeException(ResultCode.FUNCTION_KEY_REPEAT.getMsg());
        }
        boolean isSuccess = productEventService.insert(productEvent);
        List<ProductEventParam> productEventParams = functionVo.getList();
        if(productEventParams != null && productEventParams.size() > 0) {
            for(int i = 0;i < productEventParams.size();i++) {
                productEventParams.get(i).setEventId(productEvent.getId());
            }
            productEventParamService.insertBatch(productEventParams);
        }
        return isSuccess;
    }

    /**
     * 修改产品事件
     * @param functionVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductEvent(FunctionVo functionVo) {
        ProductEvent productEvent = new ProductEvent();
        BeanUtils.copyProperties(functionVo,productEvent);
        Wrapper<ProductEvent> wrapper = new EntityWrapper<>();
        wrapper.eq("`key`",productEvent.getKey()).eq("product_id",productEvent.getProductId()).ne("id",productEvent.getId());
        List<ProductEvent> list = productEventService.selectList(wrapper);
        if(list.size() > 0) {
            throw new RuntimeException(ResultCode.FUNCTION_KEY_REPEAT.getMsg());
        }
        boolean isSuccess = productEventService.updateById(productEvent);
        List<ProductEventParam> productEventParams = functionVo.getList();
        Wrapper<ProductEventParam> paramWrapper = new EntityWrapper<>();
        wrapper.eq("event_id",productEvent.getId());
        productEventParamService.delete(paramWrapper);
        if(productEventParams != null && productEventParams.size() > 0) {
            for(int i = 0;i < productEventParams.size();i++) {
                productEventParams.get(i).setEventId(productEvent.getId());
            }
            productEventParamService.insertBatch(productEventParams);
        }
        return isSuccess;
    }

    /**
     * 新增产品方法
     * @param functionVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean insertProductMethod(FunctionVo functionVo) {
        ProductMethod productMethod = new ProductMethod();
        BeanUtils.copyProperties(functionVo,productMethod);
        Wrapper<ProductMethod> wrapper = new EntityWrapper<>();
        wrapper.eq("`key`",functionVo.getKey()).eq("product_id",productMethod.getProductId());
        List<ProductMethod> productMethods = productMethodService.selectList(wrapper);
        if(productMethods.size() > 0) {
            throw new RuntimeException(ResultCode.FUNCTION_KEY_REPEAT.getMsg());
        }
        boolean isSuccess = productMethodService.insert(productMethod);
        List<ProductMethodParam> productInMethodParams = functionVo.getInList();
        List<ProductMethodParam> productOutMethodParams = functionVo.getOutList();
        if(productInMethodParams != null && productInMethodParams.size() > 0) {
            for(int i = 0;i < productInMethodParams.size();i++) {
                productInMethodParams.get(i).setMethodId(productMethod.getId());
            }
            productMethodParamService.insertBatch(productInMethodParams);
        }if(productOutMethodParams != null && productOutMethodParams.size() > 0) {
            for(int i = 0;i < productOutMethodParams.size();i++) {
                productOutMethodParams.get(i).setMethodId(productMethod.getId());
            }
            productMethodParamService.insertBatch(productOutMethodParams);
        }
        return isSuccess;
    }

    /**
     * 修改产品方法
     * @param functionVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductMethod(FunctionVo functionVo) {
        ProductMethod productMethod = new ProductMethod();
        BeanUtils.copyProperties(functionVo,productMethod);
        Wrapper<ProductMethod> wrapper = new EntityWrapper<>();
        wrapper.eq("`key`",functionVo.getKey()).eq("product_id",productMethod.getProductId()).ne("id",productMethod.getId());
        List<ProductMethod> productMethods = productMethodService.selectList(wrapper);
        if(productMethods.size() > 0) {
            throw new RuntimeException(ResultCode.FUNCTION_KEY_REPEAT.getMsg());
        }
        boolean isSuccess = productMethodService.updateById(productMethod);
        List<ProductMethodParam> productInMethodParams = functionVo.getInList();
        List<ProductMethodParam> productOutMethodParams = functionVo.getOutList();
        Wrapper<ProductMethodParam> paramWrapper = new EntityWrapper<>();
        wrapper.eq("method_id",productMethod.getId());
        productMethodParamService.delete(paramWrapper);
        if(productInMethodParams != null && productInMethodParams.size() > 0) {
            for(int i = 0;i < productInMethodParams.size();i++) {
                productInMethodParams.get(i).setMethodId(productMethod.getId());
            }
            productMethodParamService.insertBatch(productInMethodParams);
        }if(productOutMethodParams != null && productOutMethodParams.size() > 0) {
            for(int i = 0;i < productOutMethodParams.size();i++) {
                productOutMethodParams.get(i).setMethodId(productMethod.getId());
            }
            productMethodParamService.insertBatch(productOutMethodParams);
        }
        return isSuccess;
    }

    public FunctionVo selectProductEventById(Integer eventId){
        FunctionVo functionVo  = new FunctionVo();
        ProductEvent event = productEventService.selectById(eventId);
        BeanUtils.copyProperties(event,functionVo);

        Wrapper<ProductEventParam> wrapper =new EntityWrapper<>();
        wrapper.eq("event_id",eventId);
        wrapper.eq("is_del",0);
        functionVo.setList(  productEventParamService.selectList(wrapper));
        return functionVo;

    }

    public FunctionVo selectProductMethodtById(Integer methodId){
        FunctionVo functionVo  = new FunctionVo();
        ProductMethod method = productMethodService.selectById(methodId);
        BeanUtils.copyProperties(method,functionVo);

        Wrapper<ProductMethodParam> wrapper =new EntityWrapper<>();
        wrapper.eq("method_id",methodId);
        wrapper.eq("is_del",0);
        wrapper.eq("param_type",1);
        functionVo.setInList(  productMethodParamService.selectList(wrapper));

        wrapper =new EntityWrapper<>();
        wrapper.eq("method_id",methodId);
        wrapper.eq("is_del",0);
        wrapper.eq("param_type",2);
        functionVo.setOutList(  productMethodParamService.selectList(wrapper));

        return functionVo;

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(ProductInfo productInfo) {
        Wrapper<ProductInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("name",productInfo.getName()).ne("id",productInfo.getId()).eq("create_user",UserUtil.USERID.get());
        List<ProductInfo> list = productInfoMapper.selectList(wrapper);
        if(list.size() > 0) {
            throw new RuntimeException(ResultCode.PRODUCT_NAME_REPEAT.getMsg());
        }
        boolean isSuccess = productInfoService.updateById(productInfo);
        productInfoService.removeCaches(productInfo.getProductKey());
        return isSuccess;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(List<Integer> ids) {
        Wrapper<ProductInfo> wrapper = new EntityWrapper<>();
        wrapper.in("id",ids);
        List<ProductInfo> productInfos = productInfoService.selectList(wrapper);
        String key = "";
        for(int i = 0;i < productInfos.size();i++) {
            if(i > 0) {
                key += ",";
            }
            key += productInfos.get(i).getProductKey();
        }
        if(productInfoService.deleteBatchIds(ids)) {
            productInfoService.removeCaches(key);
        }
        Wrapper<ProductProperty> propertyWrapper = new EntityWrapper<>();
        Wrapper<ProductEvent> eventWrapper = new EntityWrapper<>();
        Wrapper<ProductEventParam> eventParamWrapper = new EntityWrapper<>();
        Wrapper<ProductMethod> methodWrapper = new EntityWrapper<>();
        Wrapper<ProductMethodParam> methodParamWrapper = new EntityWrapper<>();
        propertyWrapper.in("product_id",ids);
        productPropertyService.delete(propertyWrapper);
        eventWrapper.in("product_id",ids);
        List<ProductEvent> eventList = productEventService.selectList(eventWrapper);
        List<Integer> eventId = new ArrayList<>(10);
        for(int i = 0;i < eventList.size();i++) {
            eventId.add(eventList.get(i).getId());
        }
        eventParamWrapper.in("event_id",eventId);
        productEventParamService.delete(eventParamWrapper);
        productEventService.delete(eventWrapper);
        methodWrapper.in("product_id",ids);
        List<ProductMethod> methodList = productMethodService.selectList(methodWrapper);
        List<Integer> methodId = new ArrayList<>(10);
        for(int i = 0;i < methodList.size();i++) {
            methodId.add(methodList.get(i).getId());
        }
        methodParamWrapper.in("method_id",methodId);
        productMethodParamService.delete(methodParamWrapper);
        productMethodService.delete(methodWrapper);
        return true;
    }

    public List<ProductInfoVo> selectProduct(Page<ProductInfoVo> page,QueryPageDTO queryPageDTO) throws Exception {
        Map<String,Object> map = new HashMap<>(10);
        map.put("name",queryPageDTO.getParams().get(0).getValue());
        if(!UserUtil.ISADMIN.get()) {
            map.put("createUser",UserUtil.USERID.get());
        }
        return productInfoMapper.selectProduct(page,map);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEvent(List<Integer> ids) {
        Wrapper<ProductEvent> eventWrapper = new EntityWrapper<>();
        Wrapper<ProductEventParam> eventParamWrapper = new EntityWrapper<>();
        eventWrapper.in("id",ids);
        List<ProductEvent> eventList = productEventService.selectList(eventWrapper);
        List<Integer> eventId = new ArrayList<>(10);
        for(int i = 0;i < eventList.size();i++) {
            eventId.add(eventList.get(i).getId());
        }
        eventParamWrapper.in("event_id",eventId);
        productEventParamService.delete(eventParamWrapper);
        productEventService.delete(eventWrapper);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMethod(List<Integer> ids) {
        Wrapper<ProductMethod> methodWrapper = new EntityWrapper<>();
        Wrapper<ProductMethodParam> methodParamWrapper = new EntityWrapper<>();
        methodWrapper.in("id",ids);
        List<ProductMethod> methodList = productMethodService.selectList(methodWrapper);
        List<Integer> methodId = new ArrayList<>(10);
        for(int i = 0;i < methodList.size();i++) {
            methodId.add(methodList.get(i).getId());
        }
        methodParamWrapper.in("method_id",methodId);
        productMethodParamService.delete(methodParamWrapper);
        productMethodService.delete(methodWrapper);
        return true;
    }
}

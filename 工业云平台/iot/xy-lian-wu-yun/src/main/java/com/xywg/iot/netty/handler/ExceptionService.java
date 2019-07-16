package com.xywg.iot.netty.handler;

import com.xywg.iot.enums.ExceptionType;
import com.xywg.iot.modular.device.model.DeviceErrorLog;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceErrorLogService;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.dictionary.model.MasterType;
import com.xywg.iot.modular.dictionary.service.IMasterTypeService;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompany;
import com.xywg.iot.modular.enterprise.service.AccountInfoCompanyService;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.service.IProductInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:56 2019/3/2
 * Modified By : wangyifei
 */
@Component
public class ExceptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionService.class);

    private final IProductInfoService productService;

    private final IMasterTypeService  masterTypeService;

    private final AccountInfoCompanyService  accountInfoCompanyService;

    private final IDeviceErrorLogService deviceErrorLogService;

    private final IDeviceInfoService deviceInfoService;

    @Autowired
    public ExceptionService(IProductInfoService productService, IMasterTypeService masterTypeService, AccountInfoCompanyService accountInfoCompanyService, IDeviceErrorLogService deviceErrorLogService, IDeviceInfoService deviceInfoService) {
        this.productService = productService;
        this.masterTypeService = masterTypeService;
        this.accountInfoCompanyService = accountInfoCompanyService;
        this.deviceErrorLogService = deviceErrorLogService;
        this.deviceInfoService = deviceInfoService;
    }

    /**
     *
     * @param pk
     * @param sn
     * @param exceptionType
     * @param content
     * @param data
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public void insertError(String pk,String sn,ExceptionType exceptionType , String content,String data){
        // 查询 产品

        DeviceErrorLog  deviceErrorLog  =new DeviceErrorLog();
        deviceErrorLog.setProductKey(pk);
        deviceErrorLog.setDeviceNo(sn);
        ProductInfo productInfo =  productService.selectCacheOne(pk);

        deviceErrorLog.setProductName(productInfo.getName());

        /**设置错误信息的创建人为产品的创建人*/
        deviceErrorLog.setCreateTime(new Date());
        deviceErrorLog.setCreateUser(productInfo.getCreateUser());
        deviceErrorLog.setCreateUserName(productInfo.getCreateUserName());

        DeviceInfo deviceInfo  = deviceInfoService.selectCacheOne(pk+":"+sn);

        deviceErrorLog.setDeviceName(deviceInfo.getName());

        Integer type   = productInfo.getType();
        if(null!= type){
        /** 设置产品类型*/
         MasterType masterType =  masterTypeService.selectCacheOne(productInfo.getType().toString());
            deviceErrorLog.setProductType(masterType.getName());
        }

        AccountInfoCompany accountInfoCompany  = accountInfoCompanyService.selectCacheOne(productInfo.getCreateUser());
        if(accountInfoCompany != null ){
            deviceErrorLog.setCompany(accountInfoCompany.getName());
        }
        deviceErrorLog.setIsDel(0);
        deviceErrorLog.setDetails("错误类型<"+exceptionType.getType()+"> | 错误信息<"+content+"> | 原始数据<"+data+">");

        deviceErrorLogService.insert(deviceErrorLog);

    }


}

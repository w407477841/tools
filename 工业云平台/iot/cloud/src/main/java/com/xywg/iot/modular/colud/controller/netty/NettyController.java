package com.xywg.iot.modular.colud.controller.netty;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.device.dto.DeviceConfigFileRecordDto;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.product.model.ProductMethod;
import com.xywg.iot.modular.product.model.ProductMethodParam;
import com.xywg.iot.modular.product.service.IProductInfoService;
import com.xywg.iot.modular.product.service.IProductMethodParamService;
import com.xywg.iot.modular.product.service.IProductMethodService;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradePackageService;
import com.xywg.iot.node.service.XyNodeService;
import com.xywg.iot.util.NettyUtil;
import com.xywg.iot.util.UserUtil;
import com.xywg.iot.vo.ResultVO;
import com.xywg.log.annotations.OpenLog;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:32 2018/12/11
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/business/iot")
@Api
@SuppressWarnings("all")
public class NettyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyController.class);

    @Autowired
    private IDeviceUpgradePackageService deviceUpgradePackageService;
    @Autowired
    private XyNodeService xyNodeService;

    @Autowired
    private IDeviceInfoService deviceInfoService;
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private IProductMethodService productMethodService;

    /**
     * 方法参数
     */
    @Autowired
    private IProductMethodParamService productMethodParamService;
    @PostMapping("/{pk}/{dn}/method/{method}")
    @OpenLog
    @ResponseBody
   // @RolesAllowed(value = {RoleConstant.ADMIN, RoleConstant.AUTH, RoleConstant.THIRD})
    public Object callMethod( @PathVariable(value = "pk") String pk, @PathVariable(value = "dn") String dn, @PathVariable(value = "method") String method, @RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>(10);

        String cacheDeviceKey = pk + ":" + dn;
        DeviceInfo device = deviceInfoService.selectCacheOne(cacheDeviceKey);
        if (device == null) {
            throw new RuntimeException("设备不存在<" + dn + "> ");
        }
        //查询方法
        Wrapper<ProductMethod> methodWrapper = new EntityWrapper<>();
        methodWrapper.where("product_id = (select id from t_product_info where product_key = {0})", pk);
        methodWrapper.eq("`key`", method);
        ProductMethod productMethod = productMethodService.selectOne(methodWrapper);
        if (productMethod == null) {
            String content = "方法不存在<" + method + "> ";
           // exceptionService.insertError(pk,dn,ExceptionType.METHOD,content, JSONUtil.toJsonStr(params));
            throw new RuntimeException(content);
        }


        //查询方法 timestates 类型的 入参
        Wrapper<ProductMethodParam> methodParamWrapper = new EntityWrapper<>();
        methodParamWrapper.eq("method_id", productMethod.getId());
        methodParamWrapper.eq("param_type", 1);
        methodParamWrapper.eq("data_type",6);
        List<ProductMethodParam> methodParams = productMethodParamService.selectList(methodParamWrapper);
        methodParams.forEach(item->{
             params.put(item.getKey(),DateUtil.parse( params.get(item.getKey()),"yyyy-MM-dd HH:mm:ss.SSS").getTime()+"") ;
        });
        String localKey = NettyUtil.localKey(pk,dn);
        String url ="http://"+ xyNodeService.url(localKey)+"/internalCommunication/"+pk+"/"+dn+"/method/"+method;
        xyNodeService.call(url, JSONUtil.toJsonStr(params), UserUtil.accessToken.get());


        LOGGER.info("操作成功");


        // 判断设备方法是同步还是异步

        // 同步需要等待设备返回，异步直接返回调用者


        return new ResultVO(ResultCode.SUCCESS,null);
    }

    @PostMapping("/upgrade")
    //@OpenLog
    public Object upgrade( @RequestBody DeviceUpgradePackage deviceUpgradePackage) {
        //数据存库
        deviceUpgradePackageService.upgradeHandle(deviceUpgradePackage);

        List<DeviceInfoVo> deviceInfoVos = deviceUpgradePackage.getDeviceList();


        Set<String> set = new HashSet<>();
        for (DeviceInfoVo deviceInfoVo : deviceInfoVos) {
            String localKey = NettyUtil.localKey(deviceInfoVo.getProductKey(), deviceInfoVo.getDeviceNo());
            set.add(xyNodeService.node(localKey));
        }
        Map<String, List<DeviceInfoVo>> dnMap = new HashMap<>();
        for (String aSet : set) {
            dnMap.put(aSet, new ArrayList<>());
        }
        for (DeviceInfoVo deviceInfoVo : deviceInfoVos) {
            String localKey = NettyUtil.localKey(deviceInfoVo.getProductKey(), deviceInfoVo.getDeviceNo());
            String nodeName = xyNodeService.node(localKey);
            List<DeviceInfoVo> list = dnMap.get(nodeName);
            list.add(deviceInfoVo);
            dnMap.put(nodeName, list);
        }

        for (Map.Entry<String, List<DeviceInfoVo>> nodeName : dnMap.entrySet()) {
            String url = "http://" + xyNodeService.urlByName(nodeName.getKey()) + "/internalCommunication/upgrade";
            deviceUpgradePackage.setDeviceList(nodeName.getValue());
            xyNodeService.call(url, JSONUtil.toJsonStr(deviceUpgradePackage), UserUtil.accessToken.get());
        }

        return new ResultVO(ResultCode.SUCCESS,null);

    }


    @PostMapping("/rebootDevice")
    //@OpenLog
    // @RolesAllowed(value= {RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public Object rebootDevice( @RequestBody DeviceUpgradePackage deviceUpgradePackage ) {

        List<DeviceInfoVo> deviceList =deviceUpgradePackage.getDeviceList();
        Set<String> set = new HashSet<>();
        for (DeviceInfoVo deviceInfoVo : deviceList) {
            String localKey = NettyUtil.localKey(deviceInfoVo.getProductKey(), deviceInfoVo.getDeviceNo());
            set.add(xyNodeService.node(localKey));
        }
        Map<String, List<DeviceInfoVo>> dnMap = new HashMap<>();
        for (String aSet : set) {
            dnMap.put(aSet, new ArrayList<>());
        }
        for (DeviceInfoVo deviceInfoVo : deviceList) {
            String localKey = NettyUtil.localKey(deviceInfoVo.getProductKey(), deviceInfoVo.getDeviceNo());
            String nodeName = xyNodeService.node(localKey);
            List<DeviceInfoVo> list = dnMap.get(nodeName);
            list.add(deviceInfoVo);
            dnMap.put(nodeName, list);
        }

        for (Map.Entry<String, List<DeviceInfoVo>> nodeName : dnMap.entrySet()) {
            String url = "http://" + xyNodeService.urlByName(nodeName.getKey()) + "/internalCommunication/rebootDevice";
            deviceUpgradePackage.setDeviceList(nodeName.getValue());
            xyNodeService.call(url, JSONUtil.toJsonStr(deviceUpgradePackage), UserUtil.accessToken.get());
        }
        return new ResultVO(ResultCode.SUCCESS,null);

    }

    /**
     * 配置文件下发
     * @param request
     * @param deviceUpgradePackage
     * @return
     */
    @PostMapping("/sendConfig")
    //@OpenLog
    public Object sendConfig( @RequestBody DeviceConfigFileRecordDto configFileRecordDto) {

        Set<String> set = new HashSet<>();
        //查找相关的服务器
        for (DeviceInfoVo deviceInfoVo : configFileRecordDto.getDeviceList()) {
            String localKey = NettyUtil.localKey(deviceInfoVo.getProductKey(), deviceInfoVo.getDeviceNo());
            set.add(xyNodeService.node(localKey));
        }
        //将此次的设备分配到相关服务器
        Map<String, List<DeviceInfoVo>> dnMap = new HashMap<>();
        for (String aSet : set) {
            dnMap.put(aSet, new ArrayList<>());
        }
        for (DeviceInfoVo deviceInfoVo : configFileRecordDto.getDeviceList()) {
            String localKey = NettyUtil.localKey(deviceInfoVo.getProductKey(), deviceInfoVo.getDeviceNo());
            String nodeName = xyNodeService.node(localKey);
            List<DeviceInfoVo> list = dnMap.get(nodeName);
            list.add(deviceInfoVo);
            dnMap.put(nodeName, list);
        }

        for (Map.Entry<String, List<DeviceInfoVo>> nodeName : dnMap.entrySet()) {
            String url = "http://" + xyNodeService.urlByName(nodeName.getKey()) + "/internalCommunication/sendConfig";
            configFileRecordDto.setDeviceList(nodeName.getValue());
            xyNodeService.call(url, JSONUtil.toJsonStr(configFileRecordDto), UserUtil.accessToken.get());
        }

        return "完毕";

    }

}

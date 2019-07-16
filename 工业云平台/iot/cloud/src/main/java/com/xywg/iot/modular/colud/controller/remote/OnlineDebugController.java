package com.xywg.iot.modular.colud.controller.remote;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xywg.iot.dto.XySession;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.netty.config.NettyConfig;
import com.xywg.iot.modular.colud.netty.handler.IotHubHandler;
import com.xywg.iot.modular.colud.thread.impl.SimpleClientThread;
import com.xywg.iot.modular.product.model.ProductEvent;
import com.xywg.iot.modular.product.model.ProductMethod;
import com.xywg.iot.modular.product.service.IProductEventService;
import com.xywg.iot.modular.product.service.IProductMethodService;
import com.xywg.iot.modular.remote.model.OnlineDebug;
import com.xywg.iot.modular.remote.model.vo.DataDtoVo;
import com.xywg.iot.modular.remote.service.OnlineDebugService;
import com.xywg.iot.util.NettyUtil;
import com.xywg.iot.util.UserUtil;
import com.xywg.iot.vo.ResultVO;
import io.netty.channel.Channel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjy
 * @date 2019/1/21
 * 在线调试
 */
@RestController
@RequestMapping("/business/debug")
public class OnlineDebugController {
    @Autowired
    private OnlineDebugService onlineDebugService;
    @Autowired
    private NettyConfig nettyConfig;
    @Autowired
    private IProductMethodService productMethodService;
    @Autowired
    private IProductEventService productEventService;
    @Autowired
    private NettyUtil nettyUtil;

    @Value("${netty.iotNettyUrl:#{null}}")
    private String iotNettyUrl;
    @Value("${netty.iotNettyWebPort:#{null}}")
    private String iotNettyWebPort;


    /**
     * 发送调试指令
     */
    @PostMapping("sendOrder")
    @OpenLog
    public ResultVO sendOrder(@RequestBody OnlineDebug onlineDebug) {
        return onlineDebugService.sendOrder(onlineDebug);
    }


    /**
     * 获取不带分页的产品下的方法列表
     *
     * @param dto
     * @return
     */
    @PostMapping("getProductMethodDebug")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.AUTH, RoleConstant.THIRD})
    public ResultVO getProductMethod(@RequestBody QueryWhereDto dto) {
        EntityWrapper<ProductMethod> ew = new EntityWrapper<>();
        ew.eq("product_id", dto.getProductId());
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("create_user", UserUtil.USERID.get());
        }

        List<ProductMethod> list = productMethodService.selectList(ew);
        List<ProductMethod> lists = new ArrayList<>();
        for (ProductMethod productMethod : list) {
            ProductMethod method = new ProductMethod();
            BeanUtils.copyProperties(productMethod, method);
            method.setKey("method." + productMethod.getKey());
            method.setName("方法--" + productMethod.getName());
            lists.add(method);
        }
        return new ResultVO(ResultCode.SUCCESS, lists);
    }


    /**
     * 虚拟设备 上报属性 下拉列表
     * 获取不带分页的产品下的方法列表
     *
     * @param dto
     * @return
     */
    @PostMapping("getProductPropertyAndEventList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.AUTH, RoleConstant.THIRD})
    public ResultVO getProductEventAndProperty(@RequestBody QueryWhereDto dto) {
        EntityWrapper<ProductEvent> ew = new EntityWrapper<>();
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("create_user", UserUtil.USERID.get());
        }
        List<ProductEvent> eventList = productEventService.selectList(ew);

        List<Map<String, Object>> resList = new ArrayList<>();
        for (ProductEvent event : eventList) {
            Map<String, Object> map = new HashMap<>(3);
            map.put("product_id", event.getProductId());
            map.put("name", "事件--" + event.getName());
            map.put("key", "event." + event.getKey());
            resList.add(map);
        }
        Map<String, Object> mapProperty = new HashMap<>(3);
        mapProperty.put("product_id", dto.getProductId());
        mapProperty.put("name", "属性");
        mapProperty.put("key", "property");
        resList.add(mapProperty);
        return new ResultVO(ResultCode.SUCCESS, resList);
    }


    /**
     * 虚拟设备通信
     * @param dto
     * @return
     */
    @PostMapping("action")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.AUTH, RoleConstant.THIRD})
    public ResultVO action(@RequestBody DataDtoVo dto) {
        try{
            String type = "login";
            if (type.equals(dto.getMethod())) {
                Thread t = new Thread(new SimpleClientThread(nettyConfig, JSONUtil.toJsonStr(dto), dto.getClientId()));
                t.start();
            } else {
                Channel channel = IotHubHandler.nettyChannels.get(dto.getClientId());
                XySession xySession = channel.attr(IotHubHandler.NETTY_CHANNEL_KEY).get();
                dto.setToken(xySession.getToken());
                nettyUtil.send(JSONUtil.toJsonStr(dto), channel, false);
            }
            return new ResultVO(ResultCode.SUCCESS, null);
        }catch (Exception e){
            return new ResultVO(ResultCode.ERROR, null);
        }
    }

    /**
     * 关闭虚拟设备
     *
     * @param dto
     * @return
     */
    @PostMapping("close")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN, RoleConstant.AUTH, RoleConstant.THIRD})
    public ResultVO close(@RequestBody DataDtoVo dto) {
        Channel channel = IotHubHandler.nettyChannels.get(dto.getClientId());
        if (channel != null) {
            channel.close();
        }
        return new ResultVO(ResultCode.SUCCESS, null);
    }


}

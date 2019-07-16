package com.xywg.iot.modular.iothub.netty.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xywg.iot.common.service.WebsocketSendMessageService;
import com.xywg.iot.dto.DataDTO;
import com.xywg.iot.dto.XySession;
import com.xywg.iot.enums.DeviceLoginStatus;
import com.xywg.iot.enums.DeviceStatus;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.iothub.server.ActiveIssuedService;
import com.xywg.iot.modular.iothub.server.DeviceServer;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradePackageService;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradeRecordService;
import com.xywg.iot.node.service.XyNodeService;
import com.xywg.iot.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:43 2018/12/11
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
@SuppressWarnings("all")
public class IotHubHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 绑定通道
     */
    public static final AttributeKey<XySession> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");
    /**
     * 本地通道缓存
     */
    public static Map<String, Channel> nettyChannels = new ConcurrentHashMap<>(10);
    /**
     * 本地token 与 设备关系
     */
    public static Map<String, String> tokenChannels = new ConcurrentHashMap<>(10);

    private static final Logger LOGGER = LoggerFactory.getLogger(IotHubHandler.class);

    private final DeviceServer deviceServer;
    private final IDeviceInfoService deviceInfoService;
    protected final SimpMessageSendingOperations simpMessageSendingOperations;
    private final WebsocketSendMessageService websocketSendMessageService;
    private final ActiveIssuedService activeIssuedService;
    private final IDeviceUpgradePackageService deviceUpgradePackageService;
    private final IDeviceUpgradeRecordService deviceUpgradeRecordService;
    private final NettyUtil nettyUtil;
    private final XyNodeService xyNodeService;
    @Autowired
    public IotHubHandler(DeviceServer deviceServer, IDeviceInfoService deviceInfoService, SimpMessageSendingOperations simpMessageSendingOperations, WebsocketSendMessageService websocketSendMessageService, ActiveIssuedService activeIssuedService, IDeviceUpgradePackageService deviceUpgradePackageService, IDeviceUpgradeRecordService deviceUpgradeRecordService, NettyUtil nettyUtil, XyNodeService xyNodeService) {
        this.deviceServer = deviceServer;
        this.deviceInfoService = deviceInfoService;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.websocketSendMessageService = websocketSendMessageService;
        this.activeIssuedService = activeIssuedService;
        this.deviceUpgradePackageService = deviceUpgradePackageService;
        this.deviceUpgradeRecordService = deviceUpgradeRecordService;
        this.nettyUtil = nettyUtil;
        this.xyNodeService = xyNodeService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {

        int len = in.readableBytes();
        //  可读开始位置
        byte[] msgData = new byte[len];
        in.readBytes(msgData);
        String data = new String(msgData);
        DataDTO dataDTO = JSONUtil.toBean(data, DataDTO.class);
        String[] method = dataDTO.getMethod().split("\\.");

        if ("login".equals(method[0])) {
            String pk = dataDTO.getParams().get("productKey");
            String dn = dataDTO.getParams().get("deviceNo");
            String topic = "/debug/" + pk + "/" + dn;
            //将收到的数据往WebSocket 推送一份(用于在线调试)
            websocketSendMessageService.sendMessage(topic, "设备上报", data);
        } else {
            XySession xySession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
            if (xySession != null) {
                String topic = "/debug/" + xySession.getPk() + "/" + xySession.getDn();
                //将收到的数据往WebSocket 推送一份(用于在线调试)
                websocketSendMessageService.sendMessage(topic, "设备上报", data);
            }else{
                //说明是新的连接，需要重新创建恢复session
                //获取token
                String token  =  dataDTO.getToken() ;
                 if(!StrUtil.isBlank(token)){
                     String localKey =  tokenChannels.get(token);
                     if(!StrUtil.isBlank(localKey)){
                         Channel channel = nettyChannels.get(localKey);
                        XySession old_xySession =  channel.attr(NETTY_CHANNEL_KEY).get();
                        //设置新的连接为该设备的连接
                        ctx.channel().attr(NETTY_CHANNEL_KEY).set(old_xySession);
                        nettyChannels.put(localKey,ctx.channel());
                        //去除原来的连接
                         channel.attr(NETTY_CHANNEL_KEY).set(null);
                         channel.close();
                     }
                 }
            }
        }

        switch (method[0]) {
            case "login":
                login(dataDTO.getParams(), ctx);
                break;
            case "property":
                property(dataDTO.getParams(), ctx,data);
                break;
            case "event":
                event(method[1], dataDTO.getParams(), ctx,data);
                break;
            case "method":
                method(method[1], dataDTO.getUuid(), dataDTO.getParams(), ctx);
                break;
            case "heartbeat":
                heartbeat(dataDTO, ctx);
                break;
            case "upgrade":
                upgrade(dataDTO, ctx);
                break;
            default:
                other(dataDTO.getParams(), ctx);
                break;
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("###########有设备连接处理器############");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("###客户端闲置，服务器主动关闭连接###");
                ctx.close();
            }
        }
    }

    /**
     * 登录
     *
     * @param params 参数
     * @param ctx    上下文
     */
    private void login(Map<String, String> params, ChannelHandlerContext ctx) {
        //获取通道绑定的session
        XySession xySession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        if (xySession != null) {
            //已登录过
            DataDTO dataDTO = DataDTO.loginResult(DeviceLoginStatus.REPEAT);
            nettyUtil.send(dataDTO, ctx.channel(),true);
            LOGGER.info("重复登录<pk:{}><dn:{}><ds:{}><ip:{}>", xySession.getPk(), xySession.getDn(), xySession.getDs(), ctx.channel().remoteAddress());
        } else {
            // 登录验证
            xySession = new XySession();
            String productKey = params.get("productKey");
            String deviceNO = params.get("deviceNo");
            String deviceSecret = params.get("deviceSecret");
            xySession.setPk(productKey);
            xySession.setDn(deviceNO);
            xySession.setDs(deviceSecret);
            // 确保session唯一
            xySession.setId(IdUtil.simpleUUID());
            if (deviceServer.updateDeviceStatus(productKey, deviceSecret, deviceNO, DeviceStatus.ONLINE)) {
                // 登录成功
                // 通道与map双向绑定
                ctx.channel().attr(NETTY_CHANNEL_KEY).set(xySession);
                nettyChannels.put(NettyUtil.localKey(xySession.getPk(), xySession.getDn()), ctx.channel());

                //token 绑定 设备
                String token = xySession.getId();
                tokenChannels.put(token,NettyUtil.localKey(xySession.getPk(), xySession.getDn()));
                DataDTO dataDTO = DataDTO.loginResult(DeviceLoginStatus.SUCCESS,token);

                //返回登录结果
                nettyUtil.send(dataDTO, ctx.channel(),true);
                deviceServer.updateDeviceAddr(productKey, deviceSecret, deviceNO, ctx);

                // 将节点与通道绑定
                xyNodeService.bindChannel(NettyUtil.localKey(xySession.getPk(), xySession.getDn()));

                LOGGER.info("登录成功<pk:{}><dn:{}><ds:{}><ip:{}>", productKey, deviceNO, deviceSecret, ctx.channel().remoteAddress());

                //查询因为离线期间 是否有配置,如果有就需要主动下发
                activeIssuedService.handleOfflineFunction(ctx);

                /*websocketSendMessageService.sendWebsocket(JSONUtil.toJsonStr(dataDTO).replaceAll("\"", ""), ctx.channel());*/
            } else {
                //登录失败
                DataDTO dataDTO = DataDTO.loginResult(DeviceLoginStatus.ERROR);
                nettyUtil.send(dataDTO, ctx.channel(),true);
                LOGGER.info("设备不存在<pk:{}><dn:{}><ds:{}><ip:{}>", productKey, deviceNO, deviceSecret, ctx.channel().remoteAddress());
               /* websocketSendMessageService.sendWebsocket(JSONUtil.toJsonStr(dataDTO).replaceAll("\"", ""), ctx.channel());*/
            }
        }


    }


    /**
     * 数据上报
     *
     * @param params 参数
     * @param ctx    上下文
     */
    private void property(Map<String, String> params, ChannelHandlerContext ctx,String data) {
        if (isNoLogin(ctx)) {
            throw new RuntimeException("未登录");
        }
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        try {
            deviceServer.insertProperty(currSession.getPk(), currSession.getDn(), params,data);
        } catch (Exception e) {
            LOGGER.error("数据上报异常[{}]", e.getMessage());
        }


    }

    /**
     * 其他
     *
     * @param params 参数
     * @param ctx    上下文
     */
    private void other(Map<String, String> params, ChannelHandlerContext ctx) {
        if (isNoLogin(ctx)) {
            throw new RuntimeException("未登录");
        }
    }


    /**
     * 事件上报
     *
     * @param params 参数
     * @param ctx    上下文
     * @param event  事件
     */
    private void event(String event, Map<String, String> params, ChannelHandlerContext ctx,String data) {
        if (isNoLogin(ctx)) {
            throw new RuntimeException("未登录");
        }
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        try {
            deviceServer.insertEvent(currSession.getPk(), currSession.getDn(), event, params,data);
        } catch (Exception e) {
            LOGGER.error("事件上报异常[{}]", e.getMessage());
        }
    }

    /**
     * 方法返回
     *
     * @param params 参数
     * @param uuid   方法id
     * @param method 方法名
     * @param ctx    上下文
     */
    private void method(String method, String uuid, Map<String, String> params, ChannelHandlerContext ctx) {
        if (isNoLogin(ctx)) {
            throw new RuntimeException("未登录");
        }
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        try {
            deviceServer.insertOutMethod(currSession.getPk(), currSession.getDn(), method, uuid, params, JSONUtil.toJsonStr(params));
        } catch (Exception e) {
            LOGGER.error("方法返回异常[{}]", e.getMessage());
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        LOGGER.error("异常[{}]", cause.getMessage());
        if (isNoLogin(ctx)) {
            ctx.close();
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //获取当前通道session
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        if (currSession == null) {
            return;
        }
        String localkey = NettyUtil.localKey(currSession.getPk(), currSession.getDn());
        //获取本地缓存中的通道session
        XySession localSession = nettyChannels.get(localkey).attr(NETTY_CHANNEL_KEY).get();
        if (currSession.equals(localSession)) {
            //表示本地与当前是同一个副本
            nettyChannels.remove(localkey);
            //修改数据库 内设备状态为离线，不管是否操作成功
            try {
                deviceServer.updateDeviceStatus(currSession.getPk(), currSession.getDs(), currSession.getDn(), DeviceStatus.OFFLINE);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }
            LOGGER.info("删除本地通道及redis缓存");
        } else {
            //本地与当前不是同一个副本
            //可能由于设备异常掉线，并重新上线造成
            LOGGER.info("通道副本已被更新");
        }
        LOGGER.info("###########有设备断开处理器############");
    }

    private boolean isLogin(ChannelHandlerContext ctx) {
        XySession sn = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        return sn != null;
    }

    public static boolean isLogin(String pk, String dn) {
        String localkey = NettyUtil.localKey(pk, dn);
        Channel channel = nettyChannels.get(localkey);
        if (channel == null) {
            return false;
        }
        XySession sn = channel.attr(NETTY_CHANNEL_KEY).get();
        return sn != null;

    }


    private boolean isNoLogin(ChannelHandlerContext ctx) {
        return !isLogin(ctx);
    }

    /**
     * 心跳
     *
     * @param dto
     * @param ctx
     */
    private void heartbeat(DataDTO dto, ChannelHandlerContext ctx) {
        nettyUtil.send(dto, ctx.channel(),true);
        /*websocketSendMessageService.sendWebsocket(JSONUtil.toJsonStr(dto).replaceAll("\"", ""), ctx.channel());*/
    }

    /**
     * 升级结果
     *
     * @param dto
     */
    private void upgrade(DataDTO dto, ChannelHandlerContext ctx) {
        XySession currSession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
        DeviceInfo deviceInfoDb = deviceInfoService.selectCacheOne(currSession.getPk() + ":" + currSession.getDn());
        String code = "code";
        String id = dto.getParams().get("id");
        if (!dto.getParams().containsKey("id")) {
            LOGGER.info("设备(" + deviceInfoDb.getName() + "<" + deviceInfoDb.getDeviceNo() + ">)升级结果中缺少id字段");
            return;
        }

        //查询升级履历的数据
        DeviceUpgradeRecord upgradeRecord = deviceUpgradeRecordService.selectById(id);
        if (upgradeRecord == null) {
            LOGGER.info("设备(" + deviceInfoDb.getName() + "<" + deviceInfoDb.getDeviceNo() + ">)升级结果中设备返回的id字段无效");
            return;
        }

        //在这里要比对一下这个id 是否是这个设备的  防止乱写id,修改到其他设备的数据去了
        if (!upgradeRecord.getDeviceId().equals(deviceInfoDb.getId())) {
            LOGGER.info("设备(" + deviceInfoDb.getName() + "<" + deviceInfoDb.getDeviceNo() + ">)升级结果中设备返回的id字段无效");
            return;
        }
        if (!upgradeRecord.getProductId().equals(deviceInfoDb.getProductId())) {
            LOGGER.info("设备(" + deviceInfoDb.getName() + "<" + deviceInfoDb.getDeviceNo() + ">)升级结果中设备返回的id字段无效");
            return;
        }

        //根据升级包ID  查询升级包信息
        DeviceUpgradePackage deviceUpgradePackage = deviceUpgradePackageService.selectById(upgradeRecord.getPackageId());


        if (dto.getParams().containsKey(code)) {
            DeviceUpgradeRecord deviceUpgradeRecord = new DeviceUpgradeRecord();
            deviceUpgradeRecord.setId(upgradeRecord.getId());

            if ("ok".equalsIgnoreCase(dto.getParams().get(code))) {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setId(deviceInfoDb.getId());
                deviceInfo.setVersion(deviceUpgradePackage.getVersion());
                //升级成功时更新设备的版本号
                deviceInfoService.updateById(deviceInfo);
                deviceUpgradeRecord.setIssuedFlag(2);
            } else {
                deviceUpgradeRecord.setIssuedFlag(3);
            }

            //更新升级履历表中的升级结果
            deviceUpgradeRecordService.updateById(deviceUpgradeRecord);
        }

    }
}

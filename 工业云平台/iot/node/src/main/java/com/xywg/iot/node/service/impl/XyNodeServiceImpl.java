package com.xywg.iot.node.service.impl;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.xywg.iot.node.AutoConfigNode;
import com.xywg.iot.node.properties.XyNodeProperties;
import com.xywg.iot.node.service.XyNodeService;
import com.xywg.iot.node.utils.NodeRedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:29 2019/3/8
 * Modified By : wangyifei
 */
@Service
public class XyNodeServiceImpl implements XyNodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XyNodeServiceImpl.class);

    private final NodeRedisUtil redisUtil;

    private static final String node_key_prefix =  "iot:node";
    private static final String bind_prefix =  "iot:bind";

    private final XyNodeProperties xyNodeProperties;

    @Autowired
    public XyNodeServiceImpl(NodeRedisUtil redisUtil, XyNodeProperties xyNodeProperties) {
        this.redisUtil = redisUtil;
        this.xyNodeProperties = xyNodeProperties;
    }

    /**
     *  每个服务器会有一个节点
     *  缓存服务器名称 和web服务的IP+端口
     */
    @Override
    public void putNode() {
         LOGGER.info("更新节点 {}",xyNodeProperties.getName(),JSONUtil.toJsonStr(xyNodeProperties));
         redisUtil.set(node_key_prefix+":"+xyNodeProperties.getName(),JSONUtil.toJsonStr(xyNodeProperties),AutoConfigNode.cycleTime*3/1000);
    }

    /**
     *  绑定设备与服务器关系
     * @param localKey
     */
    @Override
    public void bindChannel(String localKey) {
        redisUtil.set(bind_prefix+":"+localKey,xyNodeProperties.getName());
    }

    /**
     * 节点对应的web服务
     * @param localKey
     * @return
     */
    @Override
    public String url(String localKey) {

        if(redisUtil.exists(bind_prefix+":"+localKey)){
            String nodeName  =     (String)redisUtil.get(bind_prefix+":"+localKey);
            if(redisUtil.exists(node_key_prefix+":"+nodeName)){
                XyNodeProperties properties =  JSONUtil.toBean((String)redisUtil.get(node_key_prefix+":"+nodeName),XyNodeProperties.class);
                return properties.getUrl();
            }
        }

        return null;
    }

    /**
     *  返回节点名称
     * @param localKey
     * @return
     */
    @Override
    public String node(String localKey) {
        if(redisUtil.exists(bind_prefix+":"+localKey)){
            String nodeName  =     (String)redisUtil.get(bind_prefix+":"+localKey);
            return nodeName;
        }

        return null;
    }

    /**
     *  根据节点名查 对应的web服务
     * @param nodeName
     * @return
     */
    @Override
    public String urlByName(String nodeName) {

            if(redisUtil.exists(node_key_prefix+":"+nodeName)){
                XyNodeProperties properties =  JSONUtil.toBean((String)redisUtil.get(node_key_prefix+":"+nodeName),XyNodeProperties.class);
                return properties.getUrl();
            }
       return null;
    }

    /**
     *  调用 服务
     * @param url
     * @param params
     * @param accessToken
     * @return
     */
    @Override
    public String call(String url,String params,String accessToken) {

        HttpRequest httpRequest = HttpUtil.createRequest(Method.POST, url);
        httpRequest.header("accessToken", accessToken);
        httpRequest.body(params);
        httpRequest.contentType("application/json");
        return httpRequest.execute().body();
    }

}

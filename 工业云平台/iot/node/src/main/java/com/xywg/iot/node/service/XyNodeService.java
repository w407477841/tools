package com.xywg.iot.node.service;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:27 2019/3/8
 * Modified By : wangyifei
 */
public interface XyNodeService {
    /**
     *  存储节点信息
     */
    void putNode();

    /**
     *  绑定 节点与channel通道
     * @param localKey
     */
    void bindChannel(String localKey);

    /**
     *  根据 pk.dn 找到该通道所在的服务器
     * @param localKey
     * @return
     */
    String url(String localKey);

    /**
     *  根据 pk.dn 找到该通道所在服务器
     * @param localKey
     * @return
     */
    String node(String localKey);

    /**
     *  根据 nodeName 取 url
     * @param nodeName
     * @return
     */
    String urlByName(String nodeName);


    /**
     * 调用
     * @param url
     * @param params
     * @param accessToken
     * @return
     */
    String call(String url,String params,String accessToken);


}

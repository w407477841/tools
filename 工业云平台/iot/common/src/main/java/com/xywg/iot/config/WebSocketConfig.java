package com.xywg.iot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author hjy
 * @date 2019/1/21
 * <p>
 * EnableWebSocketMessageBroker注解开启使用STOMP协议来传输基于代理(message broker)的消息,
 * 这时控制器支持使用@MessageMapping,就像使用@RequestMapping一样
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册STOMP协议的节点(endpoint),并映射指定的url，这样浏览器直接通过这个地址建立websocket连接
        registry.addEndpoint("/webSocketServer")
                //支持跨域
                .setAllowedOrigins("*")
                //当客户机不支持websocket时，采用轮训方式
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //表示在topic和user这几个域上可以向客户端发消息, stompClient.subscribe
        config.enableSimpleBroker("/topic", "/config","/debug");
        //这句话表示给指定用户发送一对一的主题前缀是"/user"
        config.setUserDestinationPrefix("/user");
        //这句话表示客户端向服务器端发送时的主题上面需要加"/app"作为前缀。 stompClient.send
        //这个配置的是服务器订阅消息的前缀，比如@MessageMapping("/hello-socket")，浏览器端发送消息地址就是app/hello-socket;
        config.setApplicationDestinationPrefixes("/app");
    }

}

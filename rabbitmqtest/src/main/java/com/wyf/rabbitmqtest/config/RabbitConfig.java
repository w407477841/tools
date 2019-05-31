package com.wyf.rabbitmqtest.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author hujingyun
 * RabbitMq  初始化队列主题  不同的topic 必须在此类初始化一个实例对象
 */
@Configuration
public class RabbitConfig {

    public static final String quequeA = "queue.a";

    public static final String quequeB = "queue.b";

    public static final String quequeC = "queue.c";

    public static final String fanoutExchange = "com.wyf.fanout";

    @Bean
    public Queue queueA() {
        return new Queue(quequeA);
    }

    @Bean
    public Queue queueB() {
        return new Queue(quequeB);
    }

    @Bean
    public Queue queueC() {
        return new Queue(quequeC);
    }


    @Bean
    public Queue queueD() {
        return new Queue("queue.*");
    }

    @Bean
    public Queue queueXXX() {
        return new Queue("queue.#");
    }


    /**
     *  广播/ 订阅模式
     *  一对多
     * @return
     */
    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange(fanoutExchange);
    }
    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange("com.wyf.topic");
    }
    @Bean
    Binding bindXXX(Queue queueXXX,TopicExchange topicExchange){
        return  BindingBuilder.bind(queueXXX).to(topicExchange).with("queue.#");
    }
    @Bean
    Binding bindC(Queue queueC,TopicExchange topicExchange){
        return  BindingBuilder.bind(queueC).to(topicExchange).with("queue.c");
    }
    @Bean
    Binding bindD(Queue queueD,TopicExchange topicExchange){
        return  BindingBuilder.bind(queueD).to(topicExchange).with("queue.*");
    }


    @Bean
    Binding bindA(Queue queueA,FanoutExchange fanoutExchange){
    return       BindingBuilder.bind(queueA).to(fanoutExchange);
    }

    @Bean
    Binding bindB(Queue queueB,FanoutExchange fanoutExchange){
        return       BindingBuilder.bind(queueB).to(fanoutExchange);
    }

}
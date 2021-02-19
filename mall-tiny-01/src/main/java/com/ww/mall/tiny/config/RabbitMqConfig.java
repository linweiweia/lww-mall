package com.ww.mall.tiny.config;

import com.ww.mall.tiny.dto.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 14:00
 * @describe:   RabbitMq配置类（实现订单超时未支付自动取消）
 */

@Configuration
public class RabbitMqConfig {

    /**
     * 取消订单队列绑定的交换机
     */
    @Bean
    public DirectExchange orderDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 延时队列（死信队列）绑定的交换机
     */
    @Bean
    public DirectExchange orderTtlDirect() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 取消订单队列
     */
    @Bean
    public Queue orderQueue() {
        return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getQueue());
    }

    /**
     * 延时队列  durable队列名
     */
    @Bean
    public Queue orderTtlQueue() {
        return QueueBuilder.durable(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getQueue())
                .withArgument("x-dead-letter-exchange", QueueEnum.QUEUE_ORDER_CANCEL.getExchange())  //延时队列中消息过期转发的交换机
                .withArgument("x-dead-letter-routing-key", QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey()) //延时队列中消息过期转发的路由key
                .build();
    }


    /**
     * 给取消订单队列绑定交换机
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder
                .bind(orderQueue())
                .to(orderDirect())
                .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    }
    //@Bean
    //public Binding orderBinding(DirectExchange orderDirect, Queue orderQueue) {
    //    return BindingBuilder
    //            .bind(orderQueue)
    //            .to(orderDirect)
    //            .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    //}

    /**
     * 给延时队列绑定交换机
     */
    @Bean
    public Binding orderTtlBinding() {
        return BindingBuilder
                .bind(orderTtlQueue())
                .to(orderTtlDirect())
                .with(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getRouteKey());
    }
    //@Bean
    //public Binding orderTtlBinding(DirectExchange orderTtlDirect, Queue orderTtlQueue) {
    //    return BindingBuilder
    //            .bind(orderTtlQueue)
    //            .to(orderTtlDirect)
    //            .with(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getRouteKey());
    //}


}

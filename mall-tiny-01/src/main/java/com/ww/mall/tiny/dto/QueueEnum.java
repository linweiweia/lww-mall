package com.ww.mall.tiny.dto;

import lombok.Getter;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 14:02
 * @describe:   消息队列定义
 */

@Getter
public enum  QueueEnum {

    //取消订单队列
    QUEUE_ORDER_CANCEL("mall.order.direct", "mall.order.cancel", "mall.order.cancel"),
    //延时队列（死信队列）
    QUEUE_ORDER_CANCEL_TTL("mall.order.direct.ttl","mall.order.cancel.ttl","mall.order.cancel.ttl");


    //交换机名称
    private String exchange;
    //队列名称
    private String queue;
    //路由key
    private String routeKey;


    QueueEnum(String exchange, String queue, String routeKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routeKey = routeKey;
    }

}

package com.ww.mall.tiny.component;

import com.ww.mall.tiny.service.OmsPortalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 15:19
 * @describe:   监听取消订单队列，处理取消订单操作
 */

@Component
@RabbitListener(queues = "mall.order.cancel")
public class CancelOrderReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderReceiver.class);

    @Autowired
    private OmsPortalOrderService omsPortalOrderService;

    /**
     * 处理取消订单队列消息：handle是消费消息的回调函数（参数应该是Spring自动匹配的），有点像js的写法。
     */
    @RabbitHandler
    public void handle(Long orderId) {
        LOGGER.info("receiver delay message orderId:{}", orderId);
        omsPortalOrderService.cancelOrder(orderId);
    }

}

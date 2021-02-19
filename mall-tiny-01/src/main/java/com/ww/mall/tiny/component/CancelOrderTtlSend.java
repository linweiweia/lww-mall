package com.ww.mall.tiny.component;

import com.ww.mall.tiny.dto.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 15:04
 * @describe:   向延时队列发送消息
 */

@Component
public class CancelOrderTtlSend {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderTtlSend.class);

    @Autowired
    private AmqpTemplate amqpTemplate;  //spring操作mq对象


    public void sendMessage(Long orderId, final long delayTimes) {
        //向延时队列发送消息
        //String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getExchange(), QueueEnum.QUEUE_ORDER_CANCEL_TTL.getRouteKey(), orderId, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置延时时间
                message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                return message;
            }
        });
        LOGGER.info("send delay message orderId:{}", orderId);
    }
}

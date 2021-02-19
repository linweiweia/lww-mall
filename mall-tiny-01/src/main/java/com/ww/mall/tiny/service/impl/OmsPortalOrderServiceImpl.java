package com.ww.mall.tiny.service.impl;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.component.CancelOrderTtlSend;
import com.ww.mall.tiny.dto.OrderParam;
import com.ww.mall.tiny.service.OmsPortalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 14:48
 * @describe:   订单管理实现类
 */

@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService{

    private static final Logger LOGGER = LoggerFactory.getLogger(OmsPortalOrderServiceImpl.class);

    @Autowired
    private CancelOrderTtlSend cancelOrderTtlSend;

    @Override
    public CommonResult generateOrder(OrderParam orderParam) {
        //下单进行锁定库存，减积分等操作
        LOGGER.info("process generateOrder");
        sendDelayMessageCancelOrder(11L);
        return CommonResult.success("下单成功",null);
    }

    @Override
    public void cancelOrder(Long orderId) {
        //模拟取消订单，加积分，增库存等
        LOGGER.info("process cancelOrder orderId:{}", orderId);
    }

    /**
     * 发送延时消息
     * 下单成功就调用此方法
     */
    private void sendDelayMessageCancelOrder(Long orderId) {
        //设置超时时间 测试30s
        long delayTime = 1000 * 30;
        //发送消息到延时队列
        cancelOrderTtlSend.sendMessage(orderId, delayTime);
    }
}

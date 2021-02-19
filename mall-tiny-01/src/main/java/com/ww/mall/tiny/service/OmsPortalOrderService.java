package com.ww.mall.tiny.service;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.dto.OrderParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 14:43
 * @describe:   订单管理service
 */

public interface OmsPortalOrderService {

    /**
     * 生成订单操作
     * 注意：使用事物控制
     */
    @Transactional
    CommonResult generateOrder(OrderParam orderParam);

    /**
     * 超时取消订单
     */
    @Transactional
    void cancelOrder(Long orderId);


}

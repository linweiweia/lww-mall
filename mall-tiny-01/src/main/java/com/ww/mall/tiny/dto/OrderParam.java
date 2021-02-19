package com.ww.mall.tiny.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 14:45
 * @describe:   生成订单入参
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam {
    //收货地址id
    private Long memberReceiveAddressId;
    //优惠券id
    private Long couponId;
    //使用的积分数
    private Integer useIntegration;
    //支付方式
    private Integer payType;
}

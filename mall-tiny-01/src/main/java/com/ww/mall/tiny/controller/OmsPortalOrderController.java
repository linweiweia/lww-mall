package com.ww.mall.tiny.controller;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.dto.OrderParam;
import com.ww.mall.tiny.service.OmsPortalOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-02-07 15:28
 * @describe:   订单管理controller
 */

@RestController
@Api(tags = "OmsPortalOrderController", description = "订单管理")
@RequestMapping("/order")
public class OmsPortalOrderController {

    @Autowired
    private OmsPortalOrderService omsPortalOrderService;

    @ApiOperation("生成订单")
    @PostMapping("/generate")
    public CommonResult generateOrder(@RequestBody OrderParam orderParam) {
        return omsPortalOrderService.generateOrder(orderParam);
    }
}

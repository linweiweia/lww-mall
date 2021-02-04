package com.ww.mall.tiny.controller;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.service.impl.EsProductServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-25 15:50
 * @describe:   在ES中操作产品controller
 */

@RestController
@Api(tags = "EsProductController", description = "搜索商品管理")
@RequestMapping("/esProduct")
public class EsProductController {

    @Autowired
    private EsProductServiceImpl esProductService;


    @ApiOperation(value = "将所有商品导入到ES中")
    @PostMapping("/importAll")
    public CommonResult getAllProductList() {
        boolean b = esProductService.importAll();
        if (b) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }





}

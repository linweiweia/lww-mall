package com.ww.mall.tiny.controller;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.comom.api.CommonPage;
import com.ww.mall.tiny.mbg.mode.PmsBrand;
import com.ww.mall.tiny.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-26 17:17
 * @describe: 品牌控制器
 */


@Api(tags = "BrandController", description = "商品品牌管理")
@RestController
@RequestMapping("/brand")
public class PmsBrandController {

    @Autowired
    private PmsBrandService brandService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PmsBrandController.class);


    //增加品牌
    @ApiOperation("添加新品牌")
    @PostMapping("/createBrand")
    @PreAuthorize("hasAuthority('pms:brand:create')")
    public CommonResult createBrand(@ApiParam("产品对象json格式") @RequestBody PmsBrand pmsBrand) {
        CommonResult commonResult;
        int count = brandService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = CommonResult.success(pmsBrand);
            LOGGER.debug("create pmsBrand success:{}", pmsBrand);
        } else {
            commonResult = CommonResult.failed();
            LOGGER.debug("create pmsBrand fail:{}", pmsBrand);
        }
        return commonResult;
    }

    //删除品牌
    @ApiOperation("删除品牌")
    @DeleteMapping("/deleteBrand/{id}")
    @PreAuthorize("hasAuthority('pms:brand:delete')")
    public CommonResult deleteBrand(@PathVariable("id") Long id) {
        CommonResult commonResult;
        int count = brandService.deleteBrand(id);
        if (count == 1) {
            commonResult = CommonResult.success();
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    //按ID查找品牌
    @ApiOperation("获取指定id的品牌详情")
    @GetMapping("/getBrand/{id}")
    @PreAuthorize("hasAuthority('pms:brand:read')")
    public CommonResult<PmsBrand> getBrand(@PathVariable("id") Long id) {
        //查找就不做判断了，有就有没有就没有
        return CommonResult.success(brandService.getBrand(id));
    }


    //查找全部
    @ApiOperation("查找全部")
    @GetMapping("/listAllBrand")
    @PreAuthorize("hasAuthority('pms:brand:read')")
    public CommonResult<List<PmsBrand>> listAllBrand() {
        //查找就不做判断了，有就有没有就没有
        return CommonResult.success(brandService.listAllBrand());
    }

    //分页查找
    @ApiOperation("分页查找")
    @GetMapping("/listBrand/{pageNum}/{pageSize}")
    @PreAuthorize("hasAuthority('pms:brand:read')")
    public CommonResult<CommonPage<PmsBrand>> listBrand(@ApiParam("页数") @PathVariable(value = "pageNum") Integer pageNum,
                                                        @ApiParam("每页大小") @PathVariable(value = "pageSize") Integer pageSize) {
        //做参数检查默认值
        List<PmsBrand> brands = brandService.listBrand(pageNum, pageSize);
        //查找就不做判断了，有就有没有就没有
        return CommonResult.success(CommonPage.fullPage(brands));
    }

    //修改
    @ApiOperation("修改品牌")
    @PutMapping("/updateBrand/{id}")
    @PreAuthorize("hasAuthority('pms:brand:update')")
    public CommonResult updateBrand(@PathVariable("id") Long id, @RequestBody PmsBrand brand) {
        CommonResult commonResult;
        int count = brandService.updateBrand(id, brand);
        if (count == 1) {
            commonResult = CommonResult.success();
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

}

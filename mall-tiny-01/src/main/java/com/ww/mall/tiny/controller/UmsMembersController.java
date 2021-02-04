package com.ww.mall.tiny.controller;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.service.UmsMembersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-12-09 09:41
 * @describe:   会员管理
 */

@RestController
@Api(tags = "UmsMembersController", description = "会员注册管理")
@RequestMapping("/sso")
public class UmsMembersController {

    @Autowired
    private UmsMembersService umsMembersService;

    @ApiOperation("获取验证码")
    @GetMapping("/getAuthCode/{telephone}")
    public CommonResult getAuthCode(@PathVariable("telephone") String telephone) {
        return umsMembersService.generateAuthCode(telephone);
    }

    @ApiOperation("校验验证码")
    @PostMapping("/verifyAuthCode/{telephone}/{authCode}")
    public CommonResult verifyAuthCode(@PathVariable("telephone") String telephone, @PathVariable("authCode") String authCode) {
        return umsMembersService.verifyAuthCode(telephone, authCode);
    }
}

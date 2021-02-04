package com.ww.mall.tiny.controller;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.dto.UmsAdminLoginParam;
import com.ww.mall.tiny.mbg.mode.UmsAdmin;
import com.ww.mall.tiny.mbg.mode.UmsPermission;
import com.ww.mall.tiny.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-15 17:53
 * @describe:   管理员（用户）控制器
 */
@RestController
@Api(tags = "UmsAdminController" ,value = "管理员用户管理")
@RequestMapping("/admin")
public class UmsAdminController {

    @Autowired
    private UmsAdminService adminService;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdmin umsAdminPara, BindingResult result) {
        UmsAdmin umsAdmin = adminService.register(umsAdminPara);
        if (umsAdmin == null) {
            return CommonResult.failed("用户名已存在");
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation("用户登入接口")
    @PostMapping("/login")
    public CommonResult login(@RequestBody UmsAdminLoginParam loginParam) {
        String token = adminService.login(loginParam.getUsername(), loginParam.getPassword());
        if (token == null) {
            return CommonResult.validDateFailed("用户名或密码错误");
        }
        HashMap<Object, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("获取用户所有权限")
    @GetMapping("/permission/{adminId}")
    public CommonResult<List<UmsPermission>> getPermissionList(@PathVariable Long adminId) {
        List<UmsPermission> permissionsList = adminService.getPermissionsList(adminId);
        return CommonResult.success(permissionsList);
    }




}

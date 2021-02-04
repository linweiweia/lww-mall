package com.ww.mall.tiny.controller;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.nosql.mongodb.document.MemberReadHistory;
import com.ww.mall.tiny.nosql.mongodb.repository.MemberReadHistoryRepository;
import com.ww.mall.tiny.service.MemberReadHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-27 10:09
 * @describe:   游览记录controller
 */

@Api(tags = "MemberReadHistoryController", description = "会员商品浏览记录管理")
@RestController
@RequestMapping("/member/history")
public class MemberReadHistoryController {

    @Autowired
    private MemberReadHistoryService memberReadHistoryService;

    /**
     * 创建游览记录
     */
    @ApiOperation("创建会员记录")
    @PutMapping("/create")
    public CommonResult create(@RequestBody MemberReadHistory memberReadHistory) {
        int count = memberReadHistoryService.create(memberReadHistory);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 批量删除会员游览记录
     * 注意：接收list参数使用@RequestParam  前端请求直接放参数后面ids=1,2,3,4
     */
    @ApiOperation("批量删除会员游览记录")
    @DeleteMapping("/delete")
    public CommonResult delete(@RequestParam("ids") List<String> ids) {
        int count = memberReadHistoryService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 获取会员游览信息
     */
    @ApiOperation("获取会员游览信息")
    @GetMapping("/list/{memberId}")
    public CommonResult list(@PathVariable("memberId") Long memberId) {
        List<MemberReadHistory> memberReadHistoryList = memberReadHistoryService.list(memberId);
        return CommonResult.success(memberReadHistoryList);
    }
}

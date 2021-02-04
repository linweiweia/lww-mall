package com.ww.mall.tiny.service;

import com.ww.mall.tiny.comom.api.CommonResult;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-12-07 22:25
 * @describe:   会员管理 模拟发送短信
 */
public interface UmsMembersService {

    /**
     * 模拟生成验证码
     * @param telephone
     * @return
     */
    CommonResult generateAuthCode(String telephone);


    /**
     * 验证验证码
     * @param telephone
     * @param authCode
     * @return
     */
    CommonResult verifyAuthCode(String telephone, String authCode);

}

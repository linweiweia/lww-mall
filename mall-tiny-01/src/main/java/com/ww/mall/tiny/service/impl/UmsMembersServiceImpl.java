package com.ww.mall.tiny.service.impl;

import com.ww.mall.tiny.comom.api.CommonResult;
import com.ww.mall.tiny.comom.exception.CatchException;
import com.ww.mall.tiny.comom.utlis.RedisUtils;
import com.ww.mall.tiny.service.UmsMembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-12-08 22:45
 * @describe:   会员管理
 */

@Service
public class UmsMembersServiceImpl implements UmsMembersService {

    @Autowired
    private RedisUtils redisUtils;
    //存入redis验证码前缀
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    //验证码过期时间
    @Value("${redis.key.expire.authCode}")
    private Long REDIS_KEY_EXPIRE_AUTH_CODE;


    //模拟生成验证码存入redis
    //@CatchException //标识该方法，会在切面被拦截抛出异常
    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        redisUtils.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb, REDIS_KEY_EXPIRE_AUTH_CODE);
        return CommonResult.success("获取验证成功",sb.toString());
    }

    //判断手机和验证码是否匹配
    @CatchException //标识该方法，会在切面被拦截抛出异常
    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (StringUtils.isEmpty(telephone)) {
            return CommonResult.failed("验证码为空");
        }
        String realAuthCode = (String) redisUtils.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        boolean equals = authCode.equals(realAuthCode);
        if (equals) {
            return CommonResult.success("校验成功",null);
        } else {
            return CommonResult.failed("验证码校验失败");
        }
    }
}

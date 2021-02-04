package com.ww.mall.tiny.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-25 10:09
 * @describe:
 */
@Configuration
@MapperScan({"com.ww.mall.tiny.mbg.mapper", "com.ww.mall.tiny.dao"})
public class MybatisConfig {
}

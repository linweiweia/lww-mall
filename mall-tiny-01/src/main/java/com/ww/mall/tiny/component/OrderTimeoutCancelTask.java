package com.ww.mall.tiny.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-19 16:01
 * @describe:   定时任务  取消超时订单
 */

@Component
public class OrderTimeoutCancelTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderTimeoutCancelTask.class);

    /**
     * cron表达式：Seconds Minutes Hours DayofMonth Month DayofWeek [Year]
     *         * 表示每个单位   ？只在日期和星期中使用  无具体意义相当于占位符
     * 每10分钟扫描一次，扫描设定超时时间之前下的订单，如果没支付则取消该订单
     */
    @Scheduled(cron = "0 1/10 * ? * ?")
    private void cancelTimeOutOrder() {
        LOGGER.info("spring Scheduled");
    }
}

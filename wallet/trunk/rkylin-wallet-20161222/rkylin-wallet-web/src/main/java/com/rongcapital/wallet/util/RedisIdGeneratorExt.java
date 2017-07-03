package com.rongcapital.wallet.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.rkylin.date.RkylinDateUtil;
import com.rkylin.utils.RkylinUtil;
import com.rongcapital.wallet.constants.AccountConstants;

/**
 * Created by shuimiao on 2016-3-7.
 */

public class RedisIdGeneratorExt {

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public String getAccountBusCode(String key, long timeOut, TimeUnit timeUnit, String format) {
        Date date = new Date();
        String secondStr = RkylinDateUtil.getSecondTime(date);
        Long number = this.redisTemplate.opsForValue().increment(key + secondStr, 1l);
        this.redisTemplate.expire(key + secondStr, timeOut, timeUnit);
        return secondStr + RkylinUtil.formatDecimal(number, format);
    }

    public String getRadomUserId() {
        return getAccountBusCode(AccountConstants.ACCOUNT_OPEN_USERID_KEY, 1, TimeUnit.SECONDS,
                AccountConstants.NO_FORMAT);
    }

    public String genOrderNo() {
        return getAccountBusCode(AccountConstants.GEN_ORDER_NO_KEY, 1, TimeUnit.SECONDS,
                AccountConstants.ORDER_NO_FORMAT);
    }
}

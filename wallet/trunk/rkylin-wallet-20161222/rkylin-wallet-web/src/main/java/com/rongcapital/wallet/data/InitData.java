package com.rongcapital.wallet.data;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.constants.VersionConstants;
import com.rongcapital.wallet.util.DateInitUtil;


@Component
public class InitData extends DateInitUtil{

    private static Logger logger = LoggerFactory.getLogger(InitData.class);

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> hashOperations;
    
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOperations;

    private final static String bankListKey = "bankList";

    private final static String versionKey = "clientVersion";

    @PostConstruct
    private void initBank() throws Exception {
        /*------初始银行列表 -----*/
        Map<String, String> map = CardConstants.BANK_MAP;
        hashOperations.putAll(SystemConstants.getKey("bankList"), map);     
        /*------初始版本号---------*/
        valueOperations.set(SystemConstants.getKey(versionKey), VersionConstants.VESION_NEW);
    }

}

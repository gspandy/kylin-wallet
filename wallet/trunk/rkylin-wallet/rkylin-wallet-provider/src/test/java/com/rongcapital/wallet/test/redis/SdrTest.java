package com.rongcapital.wallet.test.redis;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.junit.Test;
import com.rongcapital.wallet.test.base.TestBase;

/**
 * spring对各种redis数据类型进行了封装
 * Description:
 * @author: Administrator
 * @CreateDate: 2016年6月16日
 * @version: V1.0
 */
public class SdrTest extends TestBase {

    @Resource(name = "redisTemplate")
    protected HashOperations<String, String, Object> hashOperations;

    @Test
    public void saveTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("aa", 11);
        map.put("bb", 22);
        for (int i = 0; i < 5; i++) {
            hashOperations.putAll("hashkey", map);
        }
    }

}

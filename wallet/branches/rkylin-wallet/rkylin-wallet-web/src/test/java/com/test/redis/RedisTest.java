package com.test.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.rongcapital.redis.sdr.base.BaseRedisService;
import com.test.base.TestBase;

public class RedisTest extends TestBase {

    @Autowired
    private BaseRedisService<String, String> baseRedisService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void saveTest() {
        try {
            baseRedisService.set("tttttt", "wese",-1);

            Map<String, String> map = new HashMap<String, String>();
            map.put("aa", "张三");
            map.put("bb", "李四");
            map.put("cc", "王五");
            baseRedisService.bathSet(map, 10000);

            String[] keys = { "aa", "bb", "cc", "dd" };
            List<String> list = baseRedisService.bathGet(keys);
            for (String v : list) {
                System.out.println("取到值:" + v);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

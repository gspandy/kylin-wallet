package com.rongcapital.wallet.test.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongcapital.redis.sdr.base.BaseRedisService;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.test.base.TestBase;

/**
 * BaseRedisService是redis工具包里的类，在service impl类中可以继承，对当前实体进行操作，也可以直接通过注入方式来操作
 * 测试baseRedisService(注入方式)
 * 
 * @author Administrator
 */
public class BaseRedisServiceTest extends TestBase {

    @Autowired
    private BaseRedisService<String, String> baseRedisService;
    @Autowired
    private BaseRedisService<String, List<UserInfo>> baseRedisServiceList;
 
    
    @Test
    public void saveTest() {
        try {
           
            baseRedisService.set("tttttt", "wese", 10000);

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
          
            e.printStackTrace();
        }
    }

    
    @Test
    public void listTest() {

        try {
            UserInfo u = new UserInfo();
           

            List<UserInfo> list = new ArrayList<UserInfo>();
            list.add(u);
         

            boolean result = baseRedisServiceList.set("user", list, 50);
            System.out.println(result);

            List<UserInfo> resultList = baseRedisServiceList.get("user");
            for (UserInfo uu : resultList) {
              

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

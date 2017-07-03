package com.test.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import com.rongcapital.redis.sdr.base.BaseRedisService;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.util.SmsUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.BankTypeVo;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.SmsInfoVo;
import com.rongcapital.wallet.vo.UserInfoVo;
import com.test.base.TestBase;

public class RedisTest extends TestBase {

    protected static ListOperations<String, BankTypeVo> listOperations;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Test
    public void saveTest() {
        try {
            System.out.println("执行了");
            if (!hashOperations.getOperations().hasKey(SystemConstants.getKey("bankList"))) {
//                Map<String, String> map = CardConstants.BANK_MAP;
//                hashOperations.putAll(SystemConstants.getKey("bankList"), map);

            } else {
                Set<String> keys = hashOperations.keys(SystemConstants.getKey("bankList"));
                List<String> list = hashOperations.multiGet(SystemConstants.getKey("bankList"), keys);
                List<BankTypeVo> listVo = new ArrayList<BankTypeVo>();
                for (String key : keys) {

                    listVo.add(new BankTypeVo(key, hashOperations.get(SystemConstants.getKey("bankList"), key)));
                }

                for (BankTypeVo b : listVo) {
                    System.out.println(b.getBankCode() + ":  " + b.getBankName());
                }
            }

            // String listaa=valueOperations.get(SystemConstants.getKey("bankList"));
            // System.out.println(SystemConstants.getKey("bankList"));
            // if (ValidateUtils.isEmpty(listaa)){
            // List<BankTypeVo> list=CardConstants.BANK_LIST;
            // String a=JsonUtil.bean2JsonStr(list);
            // valueOperations.set(SystemConstants.getKey("bankList"), a);
            // }else{
            // String a=valueOperations.get(SystemConstants.getKey("bankList"));
            // System.out.println(a);
            // List<BankTypeVo> listA= JsonUtil.jsonStr2List(a, BankTypeVo.class);
            // System.out.println(JsonUtil.bean2JsonStr(listA));
            // for(BankTypeVo b:listA){
            // System.out.println(b.getBankCode()+": "+b.getBankName());
            // }
            // }

            // SmsUtil.setData("136888", "123");
            //
            // SmsInfoVo vo = SmsUtil.getData("13688899");
            // if (null != vo) {
            // System.out.println(vo.getDate());
            // System.out.println(vo.getVcode());
            // }
            // baseRedisService.set("tttttt", "wese",-1);
            //
            // Map<String, String> map = new HashMap<String, String>();
            // map.put("aa", "张三");
            // map.put("bb", "李四");
            // map.put("cc", "王五");
            // baseRedisService.bathSet(map, 10000);
            //
            // String[] keys = { "aa", "bb", "cc", "dd" };
            // List<String> list = baseRedisService.bathGet(keys);
            // for (String v : list) {
            // System.out.println("取到值:" + v);
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

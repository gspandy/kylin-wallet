package com.rongcapital.wallet.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.SmsInfoVo;

@Component
public class SmsUtil {

    private static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

    protected static HashOperations<String, String, String> hashOperations;

    @Resource(name = "stringRedisTemplate")
    public void setHashOperations(HashOperations<String, String, String> hashOperations) {
        SmsUtil.hashOperations = hashOperations;
    }

    public static void setData(String telNum, String vcode) throws Exception {
        logger.info("SmsUtil.setData入参:{" + telNum + ":" + vcode + "}");

        Map<String, String> properties = new HashMap<>();
        properties.put("date", String.valueOf(System.currentTimeMillis()));
        properties.put("vcode", vcode);

        hashOperations.putAll(SystemConstants.getKeySMS(telNum), properties);

        hashOperations.getOperations().expire(SystemConstants.getKeySMS(telNum), 5, TimeUnit.MINUTES); // 保留7天

    }

    public static void setData(String telNum, String vcode, Long time) throws Exception {
        logger.info("SmsUtil.setData入参:{" + telNum + ":" + vcode + "}");

        Map<String, String> properties = new HashMap<>();
        properties.put("date", String.valueOf(System.currentTimeMillis()));
        properties.put("vcode", vcode);

        hashOperations.putAll(SystemConstants.getKeySMS(telNum), properties);

        hashOperations.getOperations().expire(SystemConstants.getKeySMS(telNum), time, TimeUnit.MINUTES); // 保留7天

    }

    public static SmsInfoVo getData(String telNum) throws Exception {

        SmsInfoVo vo = null;
        Collection<String> keys = new ArrayList<String>();
        keys.add("date");
        keys.add("vcode");

        List<String> list = hashOperations.multiGet(SystemConstants.getKeySMS(telNum), keys);
        if (list != null && list.size() >= 2) {
            if (!ValidateUtils.isEmpty(list.get(0)) && !ValidateUtils.isEmpty(list.get(1))) {
                vo = new SmsInfoVo();
                vo.setDate((String) list.get(0));
                vo.setVcode((String) list.get(1));
            }

        }
        return vo;
    }

    /**
     * 删除 Discription:
     * 
     * @param tonken
     * @return boolean
     * @author libi
     * @since 2016年9月1日
     */
    public static void delToken(String telNum) throws Exception {

        hashOperations.getOperations().delete(SystemConstants.getKeySMS(telNum));

    }

}

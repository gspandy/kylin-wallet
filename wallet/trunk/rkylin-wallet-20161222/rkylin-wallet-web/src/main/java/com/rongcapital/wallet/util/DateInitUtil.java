package com.rongcapital.wallet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.constants.VersionConstants;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.BankTypeVo;

@Component
public class DateInitUtil {

    private static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

    private static HashOperations<String, String, String> hashOperations;
    
    private static ValueOperations<String,String> valueOperations;

    private final static String bankListKey = "bankList";

    private final static String versionKey = "clientVersion";
    
    @Resource(name = "stringRedisTemplate")
    public void setHashOperations(HashOperations<String, String, String> hashOperations) {
        DateInitUtil.hashOperations = hashOperations;
    }
    
    
    @Resource(name = "stringRedisTemplate")
    public void setValueOperations(ValueOperations<String,String> valueOperations) {
        DateInitUtil.valueOperations = valueOperations;
    }

    /**
     * 银行卡列表
     * Discription:
     * @return List<BankTypeVo>
     * @author libi
     * @since 2016年10月28日
     */
    public static List<BankTypeVo> getBankList() {

        if (hashOperations.getOperations().hasKey(SystemConstants.getKey(bankListKey))) {
            Set<String> keys = hashOperations.keys(SystemConstants.getKey("bankList"));
         
            List<BankTypeVo> listVo = new ArrayList<BankTypeVo>();
            for (String key : keys) {

                listVo.add(new BankTypeVo(key, hashOperations.get(SystemConstants.getKey("bankList"), key)));
            }
            return listVo;
        }
        return CardConstants.BANK_LIST;
    }
    
    /**
     * 版本号
     * Discription:
     * @return String
     * @author libi
     * @since 2016年10月28日
     */
    public static String getClientVersion() {
        String versionString = valueOperations.get(SystemConstants.getKey(versionKey));
        if (!ValidateUtils.isEmpty(versionString)) {
            return versionString;
        }
        return VersionConstants.VESION_NEW;
    }
}

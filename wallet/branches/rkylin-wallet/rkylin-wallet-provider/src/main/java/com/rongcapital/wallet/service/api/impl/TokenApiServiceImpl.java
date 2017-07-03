package com.rongcapital.wallet.service.api.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.service.TokenApiService;
import com.rongcapital.wallet.constant.SystemConstants;
import com.rongcapital.wallet.util.validate.ValidateUtils;

@Service("tokenApiService")
public class TokenApiServiceImpl implements TokenApiService {

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Override
    public boolean validateToken(String tonken, String userId) {
        if (ValidateUtils.isEmpty(tonken) || ValidateUtils.isEmpty(userId)) {
            return false;
        }
        if (hashOperations.hasKey(SystemConstants.getKey(tonken), "userId")) {
            Object o = hashOperations.get(SystemConstants.getKey(tonken), "userId");
            if (!ValidateUtils.isEmpty(o)) {
                if (userId.equals((String) o)) {
                    return true;
                }
            }
        }
        return false;
    }

}

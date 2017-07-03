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
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.UserInfoVo;

@Component
public class TokenUtil {

    private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    protected static HashOperations<String, String, String> hashOperations;

    @Resource(name = "stringRedisTemplate")
    public void setHashOperations(HashOperations<String, String, String> hashOperations) {
        TokenUtil.hashOperations = hashOperations;
    }

    public static void iniTUserInfo(UserInfoPo userInfo) throws Exception {
        userInfo.setOrgId(UserPresetConstants.ORG_ID);
        userInfo.setProId(UserPresetConstants.PRO_ID);
        userInfo.setRoleId(UserPresetConstants.ROLE_ID);
    }

    public static void setData(ClientInfo clientInfo, UserInfoPo userInfo, String tonken) throws Exception {
        logger.info("TokenUtil.setData入参:{" + JsonUtil.bean2JsonStr(userInfo) + "}");
        iniTUserInfo(userInfo);
        // 用户存在token
        if (hashOperations.hasKey(SystemConstants.getKey(userInfo.getUserId()), "token")) {
            String token = hashOperations.get(SystemConstants.getKey(userInfo.getUserId()), "token");
            String client = hashOperations.get(SystemConstants.getKey(userInfo.getUserId()), "client");
            // 来源相同，删除老token
            if (null != client && null != clientInfo.getClient() && clientInfo.getClient().equals(client)) {
                hashOperations.getOperations().delete(SystemConstants.getKey(token));
            }

        }

        Map<String, String> properties = new HashMap<>();
        properties.put("date", String.valueOf(System.currentTimeMillis()));
        properties.put("orgId", userInfo.getOrgId());
        properties.put("proId", userInfo.getProId());
        properties.put("roleId", userInfo.getRoleId());
        properties.put("userInfoId", String.valueOf(userInfo.getUserInfoId()));
        properties.put("userId", userInfo.getUserId());
        properties.put("userCode", null != userInfo.getUserCode() ? userInfo.getUserCode() : "");

        properties.put("usernName", userInfo.getUserName() != null ? userInfo.getUserName() : "");
        properties.put("userRealName", userInfo.getUserRealName() != null ? userInfo.getUserRealName() : "");
        properties.put("idCard", userInfo.getIdCard() != null ? userInfo.getIdCard() : "");
        properties.put("userType", userInfo.getUserType() != null ? String.valueOf(userInfo.getUserType()) : "");
        properties.put("idCardType", null != userInfo.getIdCardType() ? userInfo.getIdCardType()
                : String.valueOf(CardConstants.CARD_TYPE_CERTIFICATE_VALIDATE));

        hashOperations.putAll(SystemConstants.getKey(tonken), properties);

        hashOperations.getOperations().expire(SystemConstants.getKey(tonken), 7, TimeUnit.DAYS); // 保留7天

        hashOperations.put(SystemConstants.getKey(userInfo.getUserId()), "token", tonken);
        if (null != clientInfo && null != clientInfo.getClient()) {
            hashOperations.put(SystemConstants.getKey(userInfo.getUserId()), "client", clientInfo.getClient());
        }
        hashOperations.getOperations().expire(SystemConstants.getKey(userInfo.getUserId()), 7, TimeUnit.DAYS); // 保留7天
    }

    public static UserInfoVo getData(String tonken) throws Exception {

        UserInfoVo vo = null;
        Collection<String> keys = new ArrayList<String>();
        keys.add("orgId");
        keys.add("proId");
        keys.add("roleId");
        keys.add("userInfoId");
        keys.add("userId");
        keys.add("userCode");

        keys.add("usernName");
        keys.add("userRealName");
        keys.add("idCard");
        keys.add("userType");
        keys.add("idCardType");

        List<String> list = hashOperations.multiGet(SystemConstants.getKey(tonken), keys);
        if (list != null && list.size() >= 7) {
            vo = new UserInfoVo();
            if (!ValidateUtils.isEmpty(list.get(0))) {
                vo.setOrgId((String) list.get(0));
            }

            if (!ValidateUtils.isEmpty(list.get(1))) {
                vo.setProId((String) list.get(1));
            }

            if (!ValidateUtils.isEmpty(list.get(2))) {
                vo.setRoleId((String) list.get(2));
            }

            if (!ValidateUtils.isEmpty(list.get(3))) {
                vo.setUserInfoId(Long.parseLong(list.get(3)));
            }

            if (!ValidateUtils.isEmpty(list.get(4))) {
                vo.setUserId(list.get(4));
            }

            if (!ValidateUtils.isEmpty(list.get(5))) {
                vo.setUserCode(list.get(5));
            }

            if (!ValidateUtils.isEmpty(list.get(6))) {
                vo.setUserName((String) list.get(6));
            }
            if (!ValidateUtils.isEmpty(list.get(7))) {
                vo.setUserRealName((String) list.get(7));
            }
            if (!ValidateUtils.isEmpty(list.get(8))) {
                vo.setIdCard((String) list.get(8));
            }
            if (!ValidateUtils.isEmpty(list.get(9))) {
                vo.setUserType(list.get(9));
            }
            if (!ValidateUtils.isEmpty(list.get(10))) {
                vo.setIdCardType(list.get(10));
            }

        }
        return vo;
    }

    /**
     * 验证tonken是否有效果 Discription: 过期时间为7天
     * 
     * @param tonken void
     * @author libi
     * @since 2016年8月22日
     */
    public static boolean validateToken(String tonken, String userId) throws Exception {
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

    /**
     * 刷新token Discription:
     * 
     * @param tonken
     * @param userId
     * @throws Exception void
     * @author libi
     * @since 2016年10月17日
     */
    public static void refreshToken(String token, String userId) throws Exception {

        hashOperations.getOperations().expire(SystemConstants.getKey(token), 7, TimeUnit.DAYS); // 保留7天
        hashOperations.getOperations().expire(SystemConstants.getKey(userId), 7, TimeUnit.DAYS); // 保留7天
    }

    /**
     * 删除 Discription:
     * 
     * @param tonken
     * @return boolean
     * @author libi
     * @since 2016年9月1日
     */
    public static void delToken(String tonken) throws Exception {

        hashOperations.getOperations().delete(SystemConstants.getKey(tonken));

    }

}

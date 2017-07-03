package com.rongcapital.wallet.api.service;

import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;

public interface UserInfoApiService {

    public boolean register(UserInfoPo userInfo) throws Exception;

    public UserLoginPo getLoginInfo(String userName) throws Exception;

    public UserInfoPo getUserInfoById(Long userId) throws Exception;

    public UserInfoPo getUserInfoByUserId(Long userId) throws Exception;

}

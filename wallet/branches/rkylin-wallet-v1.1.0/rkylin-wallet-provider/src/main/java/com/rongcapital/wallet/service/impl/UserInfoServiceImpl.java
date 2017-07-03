package com.rongcapital.wallet.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.service.UserInfoService;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    private static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    
    @Override
    public boolean save(UserInfo userInfo) {

        return false;
    }

}

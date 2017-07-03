package com.rongcapital.wallet.api.service;

import java.util.List;

import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.po.UserWhitePo;
import com.rongcapital.wallet.api.vo.UserWhiteVo;

public interface UserInfoApiService {

    public boolean register(UserInfoPo userInfo) throws Exception;

    public UserLoginPo getLoginInfo(String userName) throws Exception;

    public UserInfoPo getUserInfoById(Long userId) throws Exception;

    public UserInfoPo getUserInfoByUserId(String userId) throws Exception;

    public List<UserWhiteVo> getUserWhiteList(UserWhitePo userWhitePo) throws Exception;
  
    public boolean registerCompany(CompanyPo companyPo) throws Exception;
    
    public int bathWhiteList(List<UserWhitePo> list) throws Exception;
  
    public boolean whiteExists(UserWhitePo userWhitePo) throws Exception;
    
    public void saveWhite(UserWhitePo userWhitePo) throws Exception;
    
    public void delWhite(UserWhitePo userWhitePo) throws Exception;
}

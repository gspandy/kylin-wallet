package com.rongcapital.wallet.api.service;

import com.rongcapital.wallet.api.po.UserLoginHistoryPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.vo.PersonInfoVo;

public interface LoginAipService {

    public boolean validatePwd(String pwd);

    public boolean loginNameIsExist(String loginName);
    
    public boolean updatePwd(String pwd);
    
    public UserLoginPo getLoginInfoByName(String userName);

    public int updateLoginInfo(UserLoginPo userLoginPo);
        
    public void insertLoginHistory(UserLoginHistoryPo po);
    
    public PersonInfoVo getPersonInfoByUserId(Long userId);
    
    
}

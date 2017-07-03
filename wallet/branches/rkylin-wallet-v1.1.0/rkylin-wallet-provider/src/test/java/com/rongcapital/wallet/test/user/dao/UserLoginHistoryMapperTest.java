package com.rongcapital.wallet.test.user.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.UserLoginHistoryMapper;
import com.rongcapital.wallet.po.UserLoginHistory;
import com.rongcapital.wallet.test.base.TestBase;

public class UserLoginHistoryMapperTest extends TestBase {

    @Autowired
    private UserLoginHistoryMapper userLoginHistoryMapper;
    
    
    
    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果
     * Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    @Rollback(false)
    public void saveTest() {
        UserLoginHistory user = new UserLoginHistory();
        user.setLoginIp("192.1.1.1");
        user.setLoginType(2);
        user.setLongSource(1);
        user.setUserInfoId(111111l);
       
        try {
            userLoginHistoryMapper.insertSelective(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
}

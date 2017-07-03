package com.rongcapital.wallet.test.user.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.UserLoginMapper;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.po.UserLogin;
import com.rongcapital.wallet.test.base.TestBase;

public class UserLoginMapperTest extends TestBase {

    @Autowired
    private UserLoginMapper userLoginMapper;

    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果 Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    @Rollback(false)
    public void saveTest() {

        UserLogin user = new UserLogin();
        user.setLoginName("11111111");
        user.setPwdSalt("aaaaa");
           
        try {
            userLoginMapper.insertSelective(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.rongcapital.wallet.test.user.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.UserInfoMapper;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.test.base.TestBase;

public class UserInfoDaoTest extends TestBase {

    @Autowired
    private UserInfoMapper userInfoMapper;
    
    
    
    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果
     * Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    @Rollback(false)
    public void saveTest() {
        UserInfo user = new UserInfo();
        user.setUserType(2);
        user.setStatus(1);
        user.setUserId("9");
      /*  user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());*/
        try {
            int a= userInfoMapper.insertSelective(user);
           
            System.out.println(user.getUserInfoId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
}

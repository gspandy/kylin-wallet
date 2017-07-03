package com.rongcapital.wallet.test.user.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.UserInfoMapper;
import com.rongcapital.wallet.po.CompanyInfo;
import com.rongcapital.wallet.po.PersonInfo;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.test.base.TestBase;
import com.rongcapital.wallet.util.json.JsonUtil;

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
            UserInfo a= userInfoMapper.selectByPrimaryKey(28l);
            PersonInfo p=a.getPersonInfo();
            CompanyInfo c=a.getCompanyInfo();
            
            System.out.println(JsonUtil.bean2JsonStr(p));
            System.out.println(JsonUtil.bean2JsonStr(c));
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
}

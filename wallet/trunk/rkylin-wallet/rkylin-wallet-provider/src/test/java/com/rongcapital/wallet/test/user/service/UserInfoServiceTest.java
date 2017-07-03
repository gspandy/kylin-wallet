package com.rongcapital.wallet.test.user.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.test.base.TestBase;

public class UserInfoServiceTest extends TestBase {

    @Autowired
    private UserInfoApiService userInfoApiService;

    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果 Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    @Rollback(false)
    public void saveTest() {
        CompanyPo p = new CompanyPo();
        p.setCompanyName("阿坦克巴");
        p.setBusLince("cccc");
        p.setUserType("1");    
        p.setUserId("33333");
        
        UserInfoPo userInfo=new UserInfoPo();
        userInfo.setUserId("");
        userInfo.setUserName("13688888888");
        userInfo.setUserType(2);
        userInfo.setSalt("afdfda");
        userInfo.setPwd("fdafdaf");
        try {
           boolean a= userInfoApiService.registerCompany(p);
          
           System.out.println(a);
         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

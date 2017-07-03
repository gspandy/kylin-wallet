package com.rongcapital.wallet.test.user.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserWhitePo;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.po.UserWhite;
import com.rongcapital.wallet.test.base.TestBase;

public class UserWhiteServiceTest extends TestBase{

    @Autowired
    private UserInfoApiService userInfoApiService;
    
    
    
    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果
     * Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    @Rollback(false)
    public void saveTest() {
        UserWhite p = new UserWhite();
        
        p.setUserId("333");
        p.setRefId("8888");
        p.setRefName("aa");
        
        p.setApplyId("www");
       
      /*  user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());*/
        try {
           // userWhiteMapper.insertSelective(p);
            UserWhitePo user=new UserWhitePo();
            user.setUserId("333");
            user.setRefId("8888");
//            List<UserWhiteVo> list=userInfoApiService.getUserWhiteList(user);
//            if(null!=list){
//                System.out.println(list.size());
//            }
            UserInfoPo list = userInfoApiService.getUserInfoByUserId("14623543519137914");

            //List<UserWhiteVo> list=userInfoApiService.getUserWhiteList(user);
            if(null!=list){
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

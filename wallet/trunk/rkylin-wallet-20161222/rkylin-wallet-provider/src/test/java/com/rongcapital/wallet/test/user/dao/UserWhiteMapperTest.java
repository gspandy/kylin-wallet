package com.rongcapital.wallet.test.user.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.UserWhiteMapper;
import com.rongcapital.wallet.po.UserWhite;
import com.rongcapital.wallet.test.base.TestBase;

public class UserWhiteMapperTest extends TestBase{

    @Autowired
    private UserWhiteMapper userWhiteMapper;
    
    
    
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
            UserWhite user=new UserWhite();
            user.setUserId("333");
            user.setRefId("8888");
            List<UserWhite> list=userWhiteMapper.getList(user);
            if(null!=list){
                System.out.println(list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

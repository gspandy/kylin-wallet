package com.rongcapital.wallet.test.user.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.PersonInfoMapper;
import com.rongcapital.wallet.po.PersonInfo;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.test.base.TestBase;

public class PersonInfoDaoTest extends TestBase {

    @Autowired
    private PersonInfoMapper personInfoMapper;
    
    
    
    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果
     * Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    public void saveTest() {
     
        try {
            PersonInfo p=personInfoMapper.selectByUserId(20l);
            if(null!=p){
                System.out.println(p.getUserCname());
                System.out.println(p.getIdNumber());
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
}

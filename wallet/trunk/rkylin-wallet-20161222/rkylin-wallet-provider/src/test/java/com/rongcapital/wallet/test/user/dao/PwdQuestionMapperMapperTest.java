package com.rongcapital.wallet.test.user.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.PwdQuestionMapper;
import com.rongcapital.wallet.po.PwdQuestion;
import com.rongcapital.wallet.test.base.TestBase;

public class PwdQuestionMapperMapperTest extends TestBase {

    @Autowired
    private PwdQuestionMapper pwdQuestionMapper;
    
    
    
    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果
     * Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    @Rollback(false)
    public void saveTest() {
        PwdQuestion p = new PwdQuestion();
        
       
      /*  user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());*/
        try {
            pwdQuestionMapper.insertSelective(p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   
}

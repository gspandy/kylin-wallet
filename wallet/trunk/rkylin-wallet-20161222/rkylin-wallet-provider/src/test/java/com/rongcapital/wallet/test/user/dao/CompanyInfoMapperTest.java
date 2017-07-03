package com.rongcapital.wallet.test.user.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.rongcapital.wallet.dao.CompanyInfoMapper;
import com.rongcapital.wallet.po.CompanyInfo;
import com.rongcapital.wallet.test.base.TestBase;

public class CompanyInfoMapperTest extends TestBase {

    @Autowired
    private CompanyInfoMapper companyMainInfoMapper;

    /**
     * @Rollback(false) Junit对数据库操作默认会执行回滚操作，设为false,可以看数据库执行的结果 Discription: void
     * @author Administrator
     * @since 2016年6月16日
     */
    @Test
    @Rollback(false)
    public void saveTest() {
        CompanyInfo com=new CompanyInfo();
        com.setBuslince("aaaaa");
        com.setCompanyType(1);
        com.setCompanyName("阿里巴巴");
        com.setUserInfoId(111l);
        companyMainInfoMapper.insertSelective(com);
    }

}

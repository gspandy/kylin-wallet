package com.test.dubbo;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.log.SysoCounter;
import com.rkylin.wheatfield.pojo.User;
import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.dubbo.AccountDubbo;
import com.rongcapital.wallet.dubbo.vo.BalanceVo;
import com.rongcapital.wallet.dubbo.vo.FinanaceAccountVo;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.CompanyVo;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;
import com.test.base.TestBase;

public class AccountDubboTest extends TestBase {

    @Autowired
    private AccountDubbo accountDubbo;

   
    @Test
    public void searchTest() {
        String userId = "201609021546470001"; // 接入机构中设置的用户ID
        String constId = UserPresetConstants.ORG_ID; // 机构号
        String productId = UserPresetConstants.PRO_CREDIT_ID; // 产品号
        
        UserInfoVo vo = new UserInfoVo();    
        vo.setUserId(userId);
        vo.setOrgId(constId);
        vo.setProId(productId);
   
        vo.setAccountType(new String[]{AccountConstants.ACCOUNT_TYPE_SON});
       
        List<FinanaceAccountVo> v = accountDubbo.searchAccount(vo);
        System.out.println(JsonUtil.bean2JsonStr(v));
        System.out.println();
    }
    
    
    @Test
    public void openCompany() {

//        String userId = "201619020546470005"; // 接入机构中设置的用户ID
//        String constId = UserPresetConstants.ORG_ID; // 机构号
//        String productId = UserPresetConstants.PRO_ID; // 产品号
//
//        CompanyPo vo=new CompanyPo();
//
//        //北京成林科技有限公司
//        //91110108099195687X
//
//
//        //91110108099195227X  随便传的
//        vo.setCompanyName("测试账户三");   // 企业名称 N
//        vo.setBusLince("91110108599195337X");  // 营业执照 N
//        vo.setUserId(userId); // 接入机构中设置的用户ID N
//        vo.setUserType("1"); //用户类型(1：商户 ) N
//        vo.setConstId(constId);  // 机构号 N
//        vo.setProductId(productId); // 产品号 N
//        vo.setUserName("测试机构"); // 用户名称，及接入机构的用户名 N

        String userId = "201612091557060001"; // 接入机构中设置的用户ID
        String constId ="M666666"; // 机构号
        String productId = "P000221";

        CompanyPo vo=new CompanyPo();

        //北京成林科技有限公司
        //91110108099195687X


        //91110108099195227X  随便传的
        vo.setCompanyName("北京融数沃雷特科技服务有限公司");   // 企业名称 N
        vo.setBusLince("91110105MA0059308G");  // 营业执照 N
        vo.setUserId(userId); // 接入机构中设置的用户ID N
        vo.setUserType("1"); //用户类型(1：商户 ) N
        vo.setConstId(constId);  // 机构号 N
        vo.setProductId(productId); // 产品号 N
        vo.setUserName("万众企业账户"); // 用户名称，及接入机构的用户名 N

        vo.setCorporateName("许波");
        vo.setCorporateIdentity("431124198503089013");

        try{
            ResultVo v = accountDubbo.openCompanyAccount(vo);
            System.out.println(v);
        }catch (Exception e){
            e.printStackTrace();
        }

       
    }
    
    
    @Test
    public void searchBalance() {

        User u = new User();
        
        u.userId = "201609021546470001";
        u.constId = UserPresetConstants.ORG_ID;
        u.productId = UserPresetConstants.PRO_ID;
        
        BalanceVo balance;
        try {
            balance = accountDubbo.getBalance(u, null, AccountConstants.BALANCE_GET_TYPE_1);
            System.out.println(JsonUtil.bean2JsonStr(balance));
        } catch (Exception e) {
           
        }
       
       
       
    }
    
   
}

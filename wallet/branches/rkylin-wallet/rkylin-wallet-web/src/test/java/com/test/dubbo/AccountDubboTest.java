package com.test.dubbo;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.pojo.User;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.dubbo.AccountDubbo;
import com.rongcapital.wallet.dubbo.vo.BalanceVo;
import com.rongcapital.wallet.dubbo.vo.FinanaceAccountVo;
import com.rongcapital.wallet.util.json.JsonUtil;
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

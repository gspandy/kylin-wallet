package com.rongcapital.wallet.dubbo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.api.AccountManagementService;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rongcapital.wallet.controller.bank.BankController;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.json.JsonUtil;

public class BankCardDubbo {

    private static Logger logger = LoggerFactory.getLogger(BankCardDubbo.class);
    @Autowired
    private AccountManagementService accountManagementService; // 查账户信息
  
    /**
     * 查询银行卡列表
     * Discription:
     * @param query
     * @return List<AccountInfo>
     * @author libi
     * @since 2016年8月24日
     */
    public List<AccountInfo> getBankList(AccountInfoQuery query) {
        logger.info("BankCardDubbo.getBankList入参:{"+JsonUtil.bean2JsonStr(query)+"}");
        return accountManagementService.selectAccountListForJsp(query);
    }

}

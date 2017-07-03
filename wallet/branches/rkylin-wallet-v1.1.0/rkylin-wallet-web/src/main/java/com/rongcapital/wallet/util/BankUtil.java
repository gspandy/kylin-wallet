package com.rongcapital.wallet.util;

import java.util.ArrayList;
import java.util.List;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rongcapital.wallet.api.vo.BankInfoVo;
import com.rongcapital.wallet.util.encryption.DesEncryptUtil;

public class BankUtil {

    public static List<BankInfoVo> converBank(List<AccountInfo> list) throws Exception{

        if (null == list || list.size() == 0) {
            return new ArrayList<>();
        }
        List<BankInfoVo> listBankInfo = new ArrayList<>();
        for (AccountInfo a : list) {
            if(a.getStatus()!=1){
                continue;
            }
            BankInfoVo b = new BankInfoVo();
            // b.setFinAccountId(a.getFinAccountId());
            b.setBankCardNum(DesEncryptUtil.des3Encode(a.getAccountNumber())); //加密
            b.setBankCode(a.getBankHead());
            b.setBankName(a.getBankHeadName());
            b.setBankBranchName(a.getBankBranchName());
            b.setBankBranch(a.getBankBranch());
            // b.setUserRealName(b.getUserRealName());;
            // b.setTel(b.getTel());
            // b.setIdCard(a.getCertificateNumber());
            b.setStatus(a.getStatus());
            b.setBankType(a.getAccountTypeId());
            listBankInfo.add(b);
        }
        return listBankInfo;
    }

}

package com.rongcapital.wallet.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.api.AccountDubboService;
import com.rkylin.wheatfield.api.AccountInfoDubboService;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.domain.M000003OpenPersonAccountBean;
import com.rkylin.wheatfield.model.BalanceResponse;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.model.FinAccountResponse;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.FinanaceAccount;
import com.rkylin.wheatfield.pojo.User;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.ResponseConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.controller.bank.BankController;
import com.rongcapital.wallet.dubbo.vo.BalanceVo;
import com.rongcapital.wallet.dubbo.vo.FinanaceAccountVo;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class AccountDubbo {

    private static Logger logger = LoggerFactory.getLogger(AccountDubbo.class);
    @Autowired
    private PaymentAccountServiceApi paymentAccountServiceApi; // 账户余额

    @Autowired
    private AccountDubboService acccountDubboService; // 个人开户

    @Autowired
    AccountInfoDubboService accountInfoDubboService;

    /**
     * 查询用户余额 Discription:
     * 
     * @param user
     * @param finAccountId
     * @param type
     * @return List<BalanceDetail>
     * @author libi
     * @since 2016年8月24日
     */
    public BalanceVo getBalance(User user, String finAccountId, String type) throws Exception{

        logger.info("AccountDubbo.getBalance-方法入参:{" + JsonUtil.bean2JsonStr(user) + ":finAccountId" + finAccountId
                + "type:" + type + "}");
        BalanceResponse balance = paymentAccountServiceApi.getUserBalance(user, finAccountId, type);
        logger.info("AccountDubbo.getBalance-dubbo返回:{" + JsonUtil.bean2JsonStr(balance) + "}");
        if (ResponseConstants.SUCCESS.equals(balance.getCode())) {

            if (type.equals(AccountConstants.BALANCE_GET_TYPE_1)) {
                Balance b = balance.getBalance();
                if (null != b) {

                    BalanceVo vo = new BalanceVo(b.getAmount(), b.getBalanceCredit(), b.getBalanceFrozon(),
                            b.getBalanceOverLimit(), b.getBalanceSettle(), b.getBalanceUsable(), b.getFinAccountId());
                    return vo;
                }
            }
        }
        return null;
    }

    /**
     * 个人开户 Discription:
     * 
     * @param user
     * @param finAccountId
     * @param type
     * @return Balance
     * @author libi
     * @since 2016年8月25日
     */
    public ResultVo openPersonAccount(UserInfoPo po) {

        logger.info("AccountDubbo.getBalance入参:{" + JsonUtil.bean2JsonStr(po) + "}");

        M000003OpenPersonAccountBean accountBean = new M000003OpenPersonAccountBean();

        accountBean.setConstId(po.getOrgId());
        accountBean.setProductId(po.getProId());
        accountBean.setUserId(po.getUserId());
        accountBean.setUsername(po.getUserRealName());
        accountBean.setPersonChnName(po.getUserRealName());
        accountBean.setCertificateType(po.getIdCardType());
        accountBean.setCertificateNumber(po.getIdCard());
        logger.info("AccountDubbo.getBalance dubbo入参:{" + JsonUtil.bean2JsonStr(accountBean) + "}");
        CommonResponse response = acccountDubboService.openPrivateAccount(accountBean);
        logger.info("AccountDubbo.getBalance dubbo反回:{" + JsonUtil.bean2JsonStr(response) + "}");
        ResultVo vo = new ResultVo();
        if (ResponseConstants.SUCCESS.equals(response.getCode())) {

            vo.setSuccess(true);

        } else {
            vo.setMsg(response.getMsg());
        }
        return vo;
    }

    /**
     * 查询账户信息 Discription:
     * 
     * @param vo
     * @return ResultVo
     * @author libi
     * @since 2016年8月30日
     */
    public List<FinanaceAccountVo> searchAccount(UserInfoVo vo) {
        List<FinanaceAccountVo> listAccount = null;
        logger.info("AccountDubbo.searchAccount入参:{" + JsonUtil.bean2JsonStr(vo) + "}");
        com.rkylin.wheatfield.bean.User user = new com.rkylin.wheatfield.bean.User();
        user.setInstCode(vo.getOrgId());
        user.setProductId(vo.getProId());
        user.setUserId(vo.getUserId());
        /*----------默认只查主账户----------------*/
        if (null == vo.getAccountType()) {
            vo.setAccountType(new String[] { AccountConstants.ACCOUNT_TYPE_MAIN });
        }
        user.setType(vo.getAccountType());
        logger.info("AccountDubbo.searchAccount-dubbo入参:{" + JsonUtil.bean2JsonStr(user) + "}");
        FinAccountResponse response = accountInfoDubboService.getFinAccount(user);
        logger.info("AccountDubbo.searchAccount-dubbo返回:{" + JsonUtil.bean2JsonStr(response) + "}");
        if (response.getCode().equals("1")) {
            listAccount = new ArrayList<>();
            List<FinanaceAccount> list = response.getFinAccList();
            if (null != list && list.size() > 0) {
                for (FinanaceAccount f : list) {
                    listAccount.add(new FinanaceAccountVo(vo.getOrgId(), vo.getProId(), vo.getUserId(),
                            f.getFinAccountId(), f.getFinAccountTypeId()));
                }
            }
        }
        return listAccount;
    }

    public static void main(String[] args) {

        // String personChnName = "李碧"; // 中文姓名 //当操作类型是2：修改时，此项目为可选
        // String certificateType = "0"; // 证件类型,0身份证;1护照;2军官证;3士兵证;4回乡证;5户口本;6外国护照;7其它 当操作类型是2：修改时，此项目为可选）
        // String certificateNumber = "430626198409216759"; // 证件号（当操作类型是2：修改时，此项目为可选）
        //
        // String userId = "454545"; // 接入机构中设置的用户ID
        // String constId = UserPresetConstants.ORG_ID; // 机构号
        // String productId = UserPresetConstants.PRO_ID; // 产品号
        // String username = "李碧"; // 用户姓名
        // String operType = "1"; // 操作类型（1：新增，2：修改）
        //
        // UserInfoPo po = new UserInfoPo();
        // po.setIdCardType(certificateType);
        // po.setIdCard(certificateNumber);
        // po.setUserId(userId);
        // po.setOrgId(constId);
        // po.setProId(productId);
        // po.setUserRealName(username);

        // String fixTel = ""; // 固定电话号码
        // String personEngName = ""; // 英文姓名 可为null
        // String personType = ""; // 个人类别 默认1 可为null
        // String personSex = ""; // 性别（1：女，2：男） 可为null
        // String birthday = ""; // 出生日期,YYYYMMDD 可为null
        // String mobileTel = ""; // 证件号（当操作类型是2：修改时，此项目为可选）可为null
        // String email = ""; // 电子邮箱 可为null
        // String post = ""; // 邮编 可为null
        // String address = ""; // 地址 可为null
        // String remark = ""; // 备注 可为null
        // String role = ""; // 角色号 可为null

        /*------------查询余额----------------*/

    }
}

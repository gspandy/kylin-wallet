package com.rongcapital.wallet.controller.bank;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rkylin.wheatfield.api.AccountManagementService;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rongcapital.wallet.api.vo.BankInfoVo;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.dubbo.BankCardDubbo;
import com.rongcapital.wallet.rop.BankCardRop;
import com.rongcapital.wallet.rop.UserRop;
import com.rongcapital.wallet.util.BankUtil;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.BankInfo;
import com.rongcapital.wallet.vo.CardInfoVo;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

/**
 * 银行卡 Description:
 * 
 * @author: libi
 * @CreateDate: 2016年8月22日
 * @version: V1.0
 */

@Controller
@RequestMapping("/bank")
public class BankController {

    private static Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private BankCardDubbo bankCardDubbo;

    /**
     * 查询银行卡列表 Discription:
     * 
     * @param num
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月22日
     */
    @ResponseBody
    @RequestMapping("/bankList")
    public ActionResult bankList(ClientInfo clientInfo) throws Exception{

        ActionResult result = new ActionResult();
        
            UserInfoVo userInfo = TokenUtil.getData(clientInfo.getToken());
            AccountInfoQuery query = new AccountInfoQuery();
            query.setRootInstCd(userInfo.getOrgId());
            query.setAccountName(userInfo.getUserId());
            query.setProductId(userInfo.getProId());
            // 取银行卡列表
            List<AccountInfo> bankInfoList = bankCardDubbo.getBankList(query);
            if (null != bankInfoList) {
                logger.info("UserController.login取银行卡列表dubbo结果:{" + JsonUtil.bean2JsonStr(bankInfoList) + "}");
            } 

            List<BankInfoVo> bankList = BankUtil.converBank(bankInfoList);

            if (null != bankList) {
                logger.info("UserController.login取银行卡列表转换后结果:{" + JsonUtil.bean2JsonStr(bankList) + "}");
                result.setSuccess(true);
                result.addResult("bankList", bankList);

            } else {
                result.setCode(ErrorCodeEnum.BANKLIST_NOT_SEARCH.getCode());
                result.setMsg(ErrorCodeEnum.BANKLIST_NOT_SEARCH.getValue());

            }

        logger.info("BankController.bankList返回结果" + JsonUtil.bean2JsonStr(result));
        return result;
    }

    /**
     * 查询银行卡明细 Discription:
     * 
     * @param num
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月22日
     */
    @ResponseBody
    @RequestMapping("/bankDetail")
    public ActionResult bankDetail(ClientInfo clientInfo, String bankCardNum) throws Exception{
        ActionResult result = new ActionResult();
        if (ValidateUtils.isEmpty(bankCardNum)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("BankController.bankDetail返回结果" + JsonUtil.bean2JsonStr(result));
            return result;
        }

        logger.info("UserController.bankDetail入参:{bankCardNum" + bankCardNum + "}");
      
            BankInfo bankInfo = UserRop.cardDetail(TokenUtil.getData(clientInfo.getToken()), bankCardNum);
            if (null != bankInfo) {
                result.setSuccess(true);
                result.addResult("bankInfo", bankInfo);
            } else {
                result.setCode(ErrorCodeEnum.BANK_CARD_ERROR.getCode());
                result.setMsg(ErrorCodeEnum.BANK_CARD_ERROR.getValue());
            }
       
        logger.info("BankController.bankDetail返回结果" + JsonUtil.bean2JsonStr(result));
        return result;

    }

    /**
     * 验证只能绑储蓄卡 Discription:
     * 
     * @param clientInfo
     * @param bankCardNum
     * @return ActionResult
     * @author libi
     * @since 2016年9月8日
     */
    @ResponseBody
    @RequestMapping("/bankTypeValidate")
    public ActionResult bankTypeValidate(ClientInfo clientInfo, String bankCardNum) throws Exception{
        ActionResult result = new ActionResult();
        if (ValidateUtils.isEmpty(bankCardNum)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("BankController.bankTypeValidate返回结果" + JsonUtil.bean2JsonStr(result));
            return result;
        }

        logger.info("UserController.bankTypeValidate入参:{bankCardNum" + bankCardNum + "}");
       
            BankInfo bankInfo = UserRop.cardDetail(TokenUtil.getData(clientInfo.getToken()), bankCardNum);
            if (null != bankInfo && bankInfo.getCardType().equals(CardConstants.CARD_TYPE_ROP_SAVE_CODE)) {
                result.setSuccess(true);
                result.addResult("success", true);
            } else {
                result.setCode(ErrorCodeEnum.BANK_VALIDATE.getCode());
                result.setMsg(ErrorCodeEnum.BANK_VALIDATE.getValue());
            }
        
        logger.info("BankController.bankTypeValidate返回结果" + JsonUtil.bean2JsonStr(result));
        return result;

    }

    /**
     * 验证银行卡信息 Discription:
     * 
     * @param clientInfo
     * @param bankCardNum
     * @param tel
     * @param userName
     * @param idCard
     * @return
     * @throws Exception ActionResult
     * @author libi
     * @since 2016年8月26日
     */
    @ResponseBody
    @RequestMapping("/validateBank")
    public ActionResult validateBank(ClientInfo clientInfo, CardInfoVo cardInfo) throws Exception {
        ActionResult result = new ActionResult();
        if (null == cardInfo || ValidateUtils.isEmpty(cardInfo.getBankCardNum())
                || ValidateUtils.isEmpty(cardInfo.getUserName()) || ValidateUtils.isEmpty(cardInfo.getIdCard())
                || ValidateUtils.isEmpty(cardInfo.getTel())) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.debug(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            return result;
        }

      
            UserInfoVo user = TokenUtil.getData(clientInfo.getToken());
            ResultVo resultVo = UserRop.cardValidate(user, cardInfo);

            if (resultVo.isSuccess()) {
                result.setSuccess(true);
                result.addResult("success", true);
            } else {
                result.setCode(ErrorCodeEnum.BANK_CHECK.getCode());
                result.setMsg(null != resultVo.getMsg() ? resultVo.getMsg() : ErrorCodeEnum.BANK_CHECK.getValue());             
            }
       
        logger.debug("/bank/validateBank.do返回" + JsonUtil.bean2JsonStr(result));
        return result;
    }

    /**
     * 绑卡 Discription:
     * 
     * @param clientInfo
     * @param bankCardNum
     * @param tel
     * @param userName
     * @param idCard
     * @return ActionResult
     * @author libi
     * @since 2016年8月23日
     */
    @ResponseBody
    @RequestMapping("/bindCard")
    public ActionResult bindCard(ClientInfo clientInfo, CardInfoVo cardInfo) throws Exception{
        ActionResult result = new ActionResult();

        if (cardInfo == null || ValidateUtils.isEmpty(cardInfo.getBankCardNum())
                || ValidateUtils.isEmpty(cardInfo.getUserName()) || ValidateUtils.isEmpty(cardInfo.getBankName())
                || ValidateUtils.isEmpty(cardInfo.getBankCode()) || ValidateUtils.isEmpty(cardInfo.getTel())) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("BankController.bindCard返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        logger.info("BankController.bindCard参数:{" + JsonUtil.bean2JsonStr(cardInfo) + "}");
       

            UserInfoVo user = TokenUtil.getData(clientInfo.getToken());

            if (null == cardInfo.getIdCard()) {
                cardInfo.setIdCard(user.getIdCard());
            }

            // 只能绑定自己的卡
            if (!user.getIdCard().equals(cardInfo.getIdCard())
                    || !user.getUserRealName().equals(cardInfo.getUserName())) {
                result.setCode(ErrorCodeEnum.BANK_VALIDATE_ON.getCode());
                result.setMsg(ErrorCodeEnum.BANK_VALIDATE_ON.getValue());
                logger.info("BankController.bindCard返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
                return result;
            }

            // 先验证四要素
            ResultVo validateVo = UserRop.cardValidate(user, cardInfo);

            if (!validateVo.isSuccess()) {
                result.setCode(ErrorCodeEnum.BANK_CHECK.getCode());
                result.setMsg(null != validateVo.getMsg() ? validateVo.getMsg() : ErrorCodeEnum.BANK_CHECK.getValue());
                logger.info("BankController.bindCard返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
                return result;
            }

            cardInfo.setCurrency(CardConstants.CARD_CURRENCY_CNY);// 币种（CNY）
            cardInfo.setPurpose(CardConstants.CARD_PURPOSE_OTHER); //// 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
            cardInfo.setAccountProperty(CardConstants.CARD_PROPERTY_PRIVATE);// 账户属性（1：对公，2：对私）

            cardInfo.setCardType(CardConstants.CARD_TYPE_BANK); // 卡类型
            cardInfo.setCurrency(CardConstants.CARD_CURRENCY_CNY);// 币种（CNY）
            cardInfo.setPurpose(CardConstants.CARD_PURPOSE_OTHER); //// 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
            cardInfo.setAccountProperty(CardConstants.CARD_PROPERTY_PRIVATE);// 账户属性（1：对公，2：对私）

            ResultVo resultVo = BankCardRop.cardBind(user, cardInfo);
            if (resultVo.isSuccess()) {
                result.setSuccess(true);
                result.addResult("success", true);

            } else {
                result.setCode(ErrorCodeEnum.BANK_BIND_ERROR.getCode());
                result.setMsg(null != resultVo.getMsg() ? resultVo.getMsg() : ErrorCodeEnum.BANK_BIND_ERROR.getValue());

            }
       
        logger.info("BankController.bindCard返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;
    }

    @ResponseBody
    @RequestMapping("/delCard")
    public ActionResult delCard(ClientInfo clientInfo,String bankCardNum) throws Exception{
        ActionResult result = new ActionResult();

        if (ValidateUtils.isEmpty(bankCardNum)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("BankController.delCard返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        logger.info("BankController.delCard参数:{bankCardNum:" + bankCardNum+ "}");
       
            UserInfoVo user = TokenUtil.getData(clientInfo.getToken());

            ResultVo resultVo = BankCardRop.cardDel(user, bankCardNum);
            
            if (resultVo.isSuccess()) {
                result.setSuccess(true);
                result.addResult("success", true);

            } else {
                result.setCode(ErrorCodeEnum.BANK_DEL_ERROR.getCode());
                result.setMsg(null != resultVo.getMsg() ? resultVo.getMsg() : ErrorCodeEnum.BANK_DEL_ERROR.getValue());

            }
        
        logger.info("BankController.delCard返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;
    }
}

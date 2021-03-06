package com.rongcapital.wallet.controller.transaction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Rop.api.ApiException;
import com.Rop.api.domain.Accountinfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Maps;
import com.rkylin.order.mixservice.baseservice.AntideductService;
import com.rkylin.order.mixservice.baseservice.TransferService;
import com.rkylin.order.mixservice.baseservice.WithdrawService;
import com.rkylin.order.response.BaseResponse;
import com.rkylin.order.service.OrderInfoBaseService;
import com.rkylin.utils.DateUtil;
import com.rkylin.wheatfield.pojo.User;
import com.rongcapital.passguard.response.BaseResultVo;
import com.rongcapital.passguard.response.CheckPwdResult;
import com.rongcapital.passguard.response.UserRequestData;
import com.rongcapital.passguard.service.PasswordGuardManageService;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.OrderCodeEnum;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.dubbo.AccountDubbo;
import com.rongcapital.wallet.dubbo.vo.BalanceVo;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.rop.BankCardRop;
import com.rongcapital.wallet.util.BeanMapper;
import com.rongcapital.wallet.util.RedisIdGeneratorExt;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.CardInfoVo;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.OrderInfoMap;
import com.rongcapital.wallet.vo.OrderInfoVo;
import com.rongcapital.wallet.vo.UserInfoVo;

/**
 * 
 * Description:交易管理
 * 
 * @author: Administrator
 * @CreateDate: 2016年8月23日
 * @version: V1.0
 */
@Controller
@RequestMapping("/transfer")
public class TransferController {
    private static Logger logger = LoggerFactory.getLogger(TransferController.class);
    @Autowired
    private TransferService transferService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private OrderInfoBaseService orderInfoBaseService;
    @Autowired
    private AccountDubbo accountDubbo;
    @Autowired
    private PasswordGuardManageService passwordGuardManageService;
    @Autowired
    private RedisIdGeneratorExt redisIdGeneratorExt;
    @Autowired
    private AntideductService antideductService;
    @Autowired
    private UserInfoApiService userInfoApiService;
    // @Autowired
    // private OrderAccountInfoService orderAccountInfoService;

    private static String TYPE = "1";// 查询类型，传1按照精确查找，传2表示该用户下所有账户余额

    /**
     * Discription:转账
     * 
     * @param token
     * @param amount 金额（单位：分）
     * @param userrelateid 接收方用户id
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月24日
     */
    @ResponseBody
    @RequestMapping("/transferBussiness")
    @SystemControllerLog(description = "转账")
    public ActionResult transferBussiness(String token, String amount, String userrelateid, String payPwd) {
        ActionResult ar = new ActionResult();

        if (ValidateUtils.isEmpty(userrelateid)) {

            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("接收方用户不能为空");
            return ar;
        }
        if (ValidateUtils.isEmpty(payPwd)) {

            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("支付密码不能为空");
            return ar;
        }

        // TODO 验证支付密码是否正确

        UserInfoVo uipo = null;
        try {
            uipo = TokenUtil.getData(token);
        } catch (Exception e) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg("系统异常");
            return ar;
        }
        // if (null == uipo || null == uipo.getUserId()) {
        // ar.setSuccess(false);
        // ar.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode());
        // ar.setMsg("token已失效，请重新登录！");
        // return ar;sssss
        // }
        // try {
        // ResultVo checkPwd = PwdRop.checkPwd(uipo, "P", payPwd);
        // if (!checkPwd.isSuccess()) {
        //
        // ar.setSuccess(false);
        // ar.setCode(ErrorCodeEnum.PAYPWD_ERROR.getCode());
        // ar.setMsg(checkPwd.getMsg());
        // return ar;
        // }
        // } catch (ApiException e1) {
        // logger.error("调用校验密码rop异常" + e1.getErrMsg());
        //
        // ar.setSuccess(false);
        // ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        // ar.setMsg("调用校验密码rop异常！请重试");
        // return ar;
        // } catch (ParseException e1) {
        // logger.error("调用校验密码rop异常" + e1.getMessage());
        //
        // ar.setSuccess(false);
        // ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        // ar.setMsg("调用校验密码rop异常！请重试");
        // return ar;
        // }
        UserRequestData userRequestData = new UserRequestData();
        userRequestData.setUserId(uipo.getUserId());
        userRequestData.setOrgId(uipo.getOrgId());
        userRequestData.setProductId(uipo.getProId());
        userRequestData.setTerminalType(UserRequestData.TerminalType.APP);
        userRequestData.setEncryptPassword(payPwd);
        userRequestData.setType(UserRequestData.Type.P);
        BaseResultVo<CheckPwdResult> resultVo = passwordGuardManageService.checkPassword(userRequestData);
        if (!resultVo.isSuccess()) {

            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PAYPWD_ERROR.getCode());
            ar.setMsg(resultVo.getErrorMsg());
            return ar;
        }
        Map<String, String> arg0 = new HashMap<String, String>();
        arg0.put("transfertype", "2");// 转账指示，请联系供应商获取
        arg0.put("conditioncode", TYPE);// 商户指示，请联系供应商获取
        arg0.put("merchantcode", UserPresetConstants.ORG_ID);// 发生方商户码
        arg0.put("productid", UserPresetConstants.PRO_ID);// 发生方产品号
        arg0.put("userid", uipo.getUserId());// 发生方用户id
        arg0.put("requestno", redisIdGeneratorExt.genOrderNo());// 请求号
        arg0.put("requesttime", DateUtil.getDateTime());// 请求时间 请按格式填写（yyyy-MM-dd HH:mm:ss)
        arg0.put("amount", amount);// 金额（单位：分）
        arg0.put("userfee", "0");// 手续费（单位：分）
        arg0.put("intermerchantcode", UserPresetConstants.ORG_ID);// 接收方机构码
        arg0.put("interproductid", UserPresetConstants.PRO_ID);// 接收方产品号
        arg0.put("userrelateid", userrelateid);// 接收方用户id
        arg0.put("orderTypeId", "3001");

        logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.TransferService.transferBussiness，参数:"
                + JsonUtil.bean2JsonStr(arg0));
        BaseResponse transferBussiness = transferService.transferBussiness(arg0);
        logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.TransferService.transferBussiness，返回:"
                + JsonUtil.bean2JsonStr(transferBussiness));
        if (transferBussiness.isIs_success()) {
            ar.setSuccess(true);
            ar.addResult("success", true);
        } else {
            ar.setMsg(transferBussiness.getMsg());
            ar.setCode(ErrorCodeEnum.CREDIT_WITHDR_ERROR.getCode());
        }
        return ar;
    }

    /**
     * 
     * 
     * Discription:提现
     * 
     * @param token
     * @param amount 单位为分，100为1元
     * @param cardno 提现卡的卡号（需要已经在账户绑定）
     * @param payPwd支付密码
     * @param bankBranchCode开户行支行号
     * @param bankBranch开户行支行名称
     * @param bankprovince开户行所在省
     * @param bankcity开户行所在市
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月26日
     */
    @ResponseBody
    @RequestMapping("/withdraw")
    @SystemControllerLog(description = "提现")
    public ActionResult withdraw(String token, String amount, String cardno, String payPwd, String bankBranchCode,
            String bankBranch, String bankprovince, String bankcity) {
        ActionResult ar = new ActionResult();

        if (ValidateUtils.isEmpty(cardno)) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("卡号不能为空！");
            logger.debug("卡号不能为空！");
            return ar;
        }
        if (ValidateUtils.isEmpty(payPwd)) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("支付密码不能为空！");
            logger.debug("支付密码不能为空！");
            return ar;
        }

        UserInfoVo uipo = null;
        try {
            uipo = TokenUtil.getData(token);
        } catch (Exception e) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg("系统异常");
            logger.debug("系统异常");
            return ar;
        }
        if (null == uipo || null == uipo.getUserId()) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode());
            ar.setMsg("token已失效，请重新登录！");
            logger.debug("token已失效，请重新登录！");
            return ar;
        }

        // ResultVo checkPwd = PwdRop.checkPwd(uipo, "P", payPwd);
        UserRequestData userRequestData = new UserRequestData();
        userRequestData.setUserId(uipo.getUserId());
        userRequestData.setOrgId(uipo.getOrgId());
        userRequestData.setProductId(uipo.getProId());
        userRequestData.setTerminalType(UserRequestData.TerminalType.APP);
        userRequestData.setEncryptPassword(payPwd);
        userRequestData.setType(UserRequestData.Type.P);
        BaseResultVo<CheckPwdResult> resultVo = passwordGuardManageService.checkPassword(userRequestData);
        if (!resultVo.isSuccess()) {

            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PAYPWD_ERROR.getCode());
            ar.setMsg(resultVo.getErrorMsg());
            return ar;
        }

        List<Accountinfo> cardDetail = null;
        try {
            cardDetail = BankCardRop.accountinfoQuery(uipo);
        } catch (ApiException e) {
            logger.error("重新绑卡异常！");
        }
        Accountinfo ainfo = null;
        for (Accountinfo accountinfo : cardDetail) {
            if (cardno.equals(accountinfo.getAccount_number())) {
                ainfo = accountinfo;
                break;
            }
        }
        if (null == ainfo) {
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg("非绑定卡不能提现！");
            logger.debug("非绑定卡不能提现！");
            return ar;
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("amount", amount);// 单位为分，100为1元
        paramMap.put("userid", uipo.getUserId());// 商户的用户id
        paramMap.put("merchantcode", UserPresetConstants.ORG_ID);// 账户系统给商户分配的机构码
        paramMap.put("orderdate", DateUtil.getDateTime());// 日期+时间2015-06-09 23:59:59
        paramMap.put("productid", UserPresetConstants.PRO_ID);// 账户给商户分配的产品号
        // 以下是可选的参数
        paramMap.put("userfee", "0");// 手续费（分）
        paramMap.put("cardno", cardno);// 提现卡的卡号（需要已经在账户绑定）
        logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.WithdrawService.withdraw，参数:"
                + JsonUtil.bean2JsonStr(paramMap));
        Map<String, String> withdraw = null;
        if (ValidateUtils.isEmpty(ainfo.getBankbranchname())) {
            CardInfoVo card = new CardInfoVo();
            card.setBankCode(bankBranchCode); // 开户行支行号
            card.setBankbranchname(bankBranch); // 开户行支行名称 可选
            card.setBankProvince(bankprovince); // 开户行所在省
            card.setBankCity(bankcity); // 开户行所在市
            card.setBankCardNum(cardno);
            try {
                boolean infoUpdate = BankCardRop.infoUpdate(uipo, card);
            } catch (ApiException e) {
                logger.error("更新银行卡异常" + e.getErrMsg());
            } catch (ParseException e) {
                logger.error("更新银行卡异常" + e.getMessage());
            }

            paramMap.put("userorderid", redisIdGeneratorExt.genOrderNo());// 商户系统的订单id
            withdraw = withdrawService.withdraw(paramMap);

        } else {
            paramMap.put("userorderid", redisIdGeneratorExt.genOrderNo());// 商户系统的订单id
            withdraw = withdrawService.withdraw(paramMap);

            if ("false".equals(withdraw.get("issuccess"))) {
                CardInfoVo card = new CardInfoVo();
                card.setBankCode(bankBranchCode); // 开户行支行号
                card.setBankbranchname(bankBranch); // 开户行支行名称 可选
                card.setBankProvince(bankprovince); // 开户行所在省
                card.setBankCity(bankcity); // 开户行所在市
                card.setBankCardNum(cardno);

                try {
                    boolean infoUpdate = BankCardRop.infoUpdate(uipo, card);
                } catch (ApiException e) {
                    logger.error("更新银行卡异常", e);
                } catch (ParseException e) {
                    logger.error("更新银行卡异常", e);
                }
                paramMap.put("userorderid", redisIdGeneratorExt.genOrderNo());// 商户系统的订单id
                withdraw = withdrawService.withdraw(paramMap);

            }
        }

        logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.WithdrawService.withdraw，返回:"
                + JsonUtil.bean2JsonStr(withdraw));
        ar.setSuccess("true".equals(withdraw.get("issuccess")));
        ar.setCode("true".equals(withdraw.get("issuccess")) ? 0 : 500);
        ar.setMsg(withdraw.get("retmsg"));
        ar.addResult("success", "true".equals(withdraw.get("issuccess")));
        return ar;
    }

    /**
     * 
     * Discription:信用额度转账提现
     * 
     * @param clientInfo
     * @param userInfo
     * @return ActionResult
     * @author Administrator
     * @since 2016年9月8日
     */
    @ResponseBody
    @RequestMapping("/cashCredit")
    @SystemControllerLog(description = "信用额度提现")
    public ActionResult cashCredit(ClientInfo clientInfo, String amount, String cardno, String payPwd,
            String bankBranchCode, String bankBranch, String bankprovince, String bankcity) {
        ActionResult ar = new ActionResult();

        if (ValidateUtils.isEmpty(cardno)) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("卡号不能为空！");
            logger.debug("卡号不能为空！");
            return ar;
        }
        if (ValidateUtils.isEmpty(payPwd)) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("支付密码不能为空！");
            logger.debug("支付密码不能为空！");
            return ar;
        }
        UserInfoVo uipo = null;
        try {
            uipo = TokenUtil.getData(clientInfo.getToken());
        } catch (Exception e) {
            logger.error("获取token数据异常", e);
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg("系统异常");
            return ar;
        }
        /*
         * if (null == uipo || null == uipo.getUserId()) { ar.setSuccess(false);
         * ar.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode()); ar.setMsg("token已失效，请重新登录！"); return ar; }
         */
        UserRequestData userRequestData = new UserRequestData();
        userRequestData.setUserId(uipo.getUserId());
        userRequestData.setOrgId(uipo.getOrgId());
        userRequestData.setProductId(uipo.getProId());
        userRequestData.setTerminalType(UserRequestData.TerminalType.APP);
        userRequestData.setEncryptPassword(payPwd);
        userRequestData.setType(UserRequestData.Type.P);
        BaseResultVo<CheckPwdResult> resultVo = passwordGuardManageService.checkPassword(userRequestData);
        if (!resultVo.isSuccess()) {

            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PAYPWD_ERROR.getCode());
            ar.setMsg(resultVo.getErrorMsg());
            return ar;
        }

        String genOrderNo = redisIdGeneratorExt.genOrderNo();
        Map<String, String> arg0 = new HashMap<String, String>();
        arg0.put("transfertype", "2");// 转账指示，请联系供应商获取
        arg0.put("conditioncode", TYPE);// 商户指示，请联系供应商获取
        arg0.put("merchantcode", UserPresetConstants.ORG_ID);// 发生方商户码
        arg0.put("productid", UserPresetConstants.PRO_CREDIT_ID);// 发生方产品号
        arg0.put("userid", uipo.getUserId());// 发生方用户id
        arg0.put("requestno", genOrderNo);// 请求号
        arg0.put("requesttime", DateUtil.getDateTime());// 请求时间 请按格式填写（yyyy-MM-dd HH:mm:ss)
        arg0.put("amount", amount);// 金额（单位：分）
        arg0.put("userfee", "0");// 手续费（单位：分）
        arg0.put("intermerchantcode", UserPresetConstants.ORG_ID);// 接收方机构码
        arg0.put("interproductid", UserPresetConstants.PRO_ID);// 接收方产品号
        arg0.put("userrelateid", uipo.getUserId());// 接收方用户id
        arg0.put("orderTypeId", "3001");

        logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.TransferService.transferBussiness，参数:"
                + JsonUtil.bean2JsonStr(arg0));
        BaseResponse transferBussiness = transferService.transferBussiness(arg0);
        logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.TransferService.transferBussiness，返回:"
                + JsonUtil.bean2JsonStr(transferBussiness));
        if (!transferBussiness.isIs_success()) {
            ar.setSuccess(transferBussiness.isIs_success());
            ar.setMsg(transferBussiness.getMsg());
            ar.setCode(Integer.parseInt(transferBussiness.getRetcode()));
            return ar;
        } else {
            /**** 转账成功，开始提现 ****/
            logger.debug("转账成功，开始提现");
            List<Accountinfo> cardDetail = null;
            try {
                cardDetail = BankCardRop.accountinfoQuery(uipo);
            } catch (ApiException e) {
                logger.error("重新绑卡异常！", e);
            }
            Accountinfo ainfo = null;
            for (Accountinfo accountinfo : cardDetail) {
                if (cardno.equals(accountinfo.getAccount_number())) {
                    ainfo = accountinfo;
                    break;
                }
            }
            if (null == ainfo) {
                ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                ar.setMsg("非绑定卡不能提现！");
                logger.debug("非绑定卡不能提现！");
                return ar;
            }
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("amount", amount);// 单位为分，100为1元
            paramMap.put("userid", uipo.getUserId());// 商户的用户id
            paramMap.put("merchantcode", UserPresetConstants.ORG_ID);// 账户系统给商户分配的机构码
            paramMap.put("orderdate", DateUtil.getDateTime());// 日期+时间2015-06-09 23:59:59
            paramMap.put("productid", UserPresetConstants.PRO_ID);// 账户给商户分配的产品号
            // 以下是可选的参数
            paramMap.put("userfee", "0");// 手续费（分）
            paramMap.put("cardno", cardno);// 提现卡的卡号（需要已经在账户绑定）
            logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.WithdrawService.withdraw，参数:"
                    + JsonUtil.bean2JsonStr(paramMap));
            Map<String, String> withdraw = null;
            if (ValidateUtils.isEmpty(ainfo.getBankbranchname())) {
                CardInfoVo card = new CardInfoVo();
                card.setBankCode(bankBranchCode); // 开户行支行号
                card.setBankbranchname(bankBranch); // 开户行支行名称 可选
                card.setBankProvince(bankprovince); // 开户行所在省
                card.setBankCity(bankcity); // 开户行所在市
                card.setBankCardNum(cardno);
                try {
                    boolean infoUpdate = BankCardRop.infoUpdate(uipo, card);
                } catch (ApiException e) {
                    logger.error("更新银行卡异常", e);
                } catch (ParseException e) {
                    logger.error("更新银行卡异常", e);
                }

                paramMap.put("userorderid", redisIdGeneratorExt.genOrderNo());// 商户系统的订单id
                withdraw = withdrawService.withdraw(paramMap);

            } else {
                paramMap.put("userorderid", redisIdGeneratorExt.genOrderNo());// 商户系统的订单id
                withdraw = withdrawService.withdraw(paramMap);

                if ("false".equals(withdraw.get("issuccess"))) {
                    CardInfoVo card = new CardInfoVo();
                    card.setBankCode(bankBranchCode); // 开户行支行号
                    card.setBankbranchname(bankBranch); // 开户行支行名称 可选
                    card.setBankProvince(bankprovince); // 开户行所在省
                    card.setBankCity(bankcity); // 开户行所在市
                    card.setBankCardNum(cardno);

                    try {
                        boolean infoUpdate = BankCardRop.infoUpdate(uipo, card);
                    } catch (ApiException e) {
                        logger.error("更新银行卡异常", e);
                    } catch (ParseException e) {
                        logger.error("更新银行卡异常", e);
                    }
                    paramMap.put("userorderid", redisIdGeneratorExt.genOrderNo());// 商户系统的订单id
                    withdraw = withdrawService.withdraw(paramMap);

                }
            }

            logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.WithdrawService.withdraw，返回:"
                    + JsonUtil.bean2JsonStr(withdraw));
            if ("true".equals(withdraw.get("issuccess"))) {
                ar.setSuccess("true".equals(withdraw.get("issuccess")));
                ar.setCode("true".equals(withdraw.get("issuccess")) ? 0 : 500);
                ar.setMsg(withdraw.get("retmsg"));
                ar.addResult("success", true);
            } else {
                /**** 提现失败，开始冲正 *****/
                logger.debug("调用dubbo接口com.rkylin.order.mixservice.baseservice.AntideductService.singleAntiDeduct，参数:"
                        + genOrderNo + ";" + UserPresetConstants.ORG_ID + ";" + redisIdGeneratorExt.genOrderNo());
                antideductService.singleAntiDeduct(genOrderNo, UserPresetConstants.ORG_ID,
                        redisIdGeneratorExt.genOrderNo());
                ar.setSuccess(false);
                ar.setCode(500);
                ar.setMsg("提现失败");
                ar.addResult("success", false);
            }
        }

        return ar;
    }

    /**
     * 
     * Discription:查询交易记录
     * 
     * @param token
     * @return ActionResult
     * @author Administrator
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @since 2016年8月24日
     */
    @ResponseBody
    @RequestMapping("/queryOrder")
    @SystemControllerLog(description = "查询交易记录")
    public ActionResult queryOrder(String token, String orderno, String status, String maxresult, String currentpage)
            throws JsonParseException, JsonMappingException, IOException {
        ActionResult ar = new ActionResult();
        /*
         * if (ValidateUtils.isEmpty(token)) { ar.setSuccess(false); ar.setCode(ErrorCodeEnum.LOGIN_NO.getCode());
         * ar.setMsg("未登录"); logger.debug("未登录"); return ar; }
         */

        UserInfoVo uipo = null;
        try {
            uipo = TokenUtil.getData(token);
        } catch (Exception e) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg("系统异常");
            logger.debug("系统异常");
            return ar;
        }
        /*
         * if (null == uipo || null == uipo.getUserId()) { ar.setSuccess(false);
         * ar.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode()); ar.setMsg("token已失效，请重新登录！");
         * logger.debug("token已失效，请重新登录！"); return ar; }
         */
        // M000003ServiceBean serviceBean = new M000003ServiceBean();
        // serviceBean.setUserId(uipo.getUserId());
        // serviceBean.setConstid(UserPresetConstants.ORG_ID);// 机构码
        // serviceBean.setOrderTypeId("B");// 基础业务为B，扩展业务待定
        // serviceBean.setProductId(UserPresetConstants.PRO_ID);// 产品号
        // serviceBean.setOpertype("3");// 操作类型（修改：2,新增：1,取消4,查询3）
        // serviceBean.setOrderDate(DateUtil.getDate());// 订单日期
        // serviceBean.setOrderTime(DateUtil.getDateTime());// 订单时间
        // if (!ValidateUtils.isEmpty(orderno)) {
        //
        // serviceBean.setUserOrderId(orderno);// 用户订单编号
        // }
        // 以下是可选的参数
        // serviceBean.setGoodsName(null);// 商品名称
        // serviceBean.setAmount("");// 订单金额（若不存在商品概念则必填）
        // serviceBean.setGoodsDetail(null);// 商品描述
        // serviceBean.setNumber(null);// 商品数量
        // serviceBean.setUnitPrice(null);// 单价
        // serviceBean.setAdjustType(null);// 调整类型
        // serviceBean.setAdjustContent(null);// 调整内容
        // serviceBean.setUserRelateId(null);// 关联用户ID（若有第三方则必须填写

        // List<OrderInfo> loi = orderInfoBaseService.queryOrder(serviceBean);

        Map<String, String> map = new HashMap<String, String>();
        // map.put("orderId", );//订单内部id
        // map.put("orderTypeId", "B");// 订单类型
        map.put("userId", uipo.getUserId());// 发生用户id
        map.put("rootInstCd", UserPresetConstants.ORG_ID);// 发生用户机构
        map.put("productId", UserPresetConstants.PRO_ID);// 发生用户产品号
        // map.put("amount", );//发生金额
        // map.put("userRelateId", );//交易相关方
        // map.put("orderTime", DateUtil.getDateTime());// 交易请求时间
        // map.put("orderDate", DateUtil.getDate());// 交易账期
        if (!ValidateUtils.isEmpty(orderno)) {
            map.put("userOrderId", orderno);// 第三方商户流水
        }
        // map.put("statusId", );//状态
        map.put("maxresult", ValidateUtils.isEmpty(maxresult) ? "5" : maxresult);// 每页显示数量
        map.put("currentpage", ValidateUtils.isEmpty(currentpage) ? "1" : currentpage);// 当前页数
        map.put("checkCode", ValidateUtils.isEmpty(status) ? "0" : status);// 当前页数

        logger.debug(
                "调用dubbo接口com.rkylin.order.service.OrderInfoBaseService.queryAll，参数:" + JsonUtil.bean2JsonStr(map));
        List<Map<String, Object>> loi = orderInfoBaseService.queryAllForRongShu(map);
        if (loi == null) {
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg("没查到交易记录");
            ar.addResult("data", "");
            return ar;
        }
        List<OrderInfoVo> change = change(loi, uipo);
        logger.debug(
                "调用dubbo接口com.rkylin.order.service.OrderInfoBaseService.queryAll，返回:" + JsonUtil.bean2JsonStr(change));

        ar.addResult("data", converList(change));
        ar.setSuccess(true);
        return ar;
    }

    /**
     * 
     * Discription:修改交易记录状态为已对账
     * 
     * @param token
     * @param orderno订单编号用,分隔
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月26日
     */
    @ResponseBody
    @RequestMapping("/updateOrder")
    @SystemControllerLog(description = "修改交易记录状态为已对账")
    public ActionResult updateOrder(String token, String orderno) {
        ActionResult ar = new ActionResult();
        /*
         * if (ValidateUtils.isEmpty(token)) { ar.setSuccess(false); ar.setCode(ErrorCodeEnum.LOGIN_NO.getCode());
         * ar.setMsg("未登录"); return ar; }
         */

        UserInfoVo uipo = null;
        try {
            uipo = TokenUtil.getData(token);
        } catch (Exception e) {
            ar.setSuccess(false);
            ar.setMsg("系统异常");
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            return ar;
        }
        /*
         * if (null == uipo || null == uipo.getUserId()) { ar.setSuccess(false);
         * ar.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode()); ar.setMsg("token已失效，请重新登录！"); return ar; }
         */
        boolean su = true;
        List<String> list = new ArrayList<String>();
        String loi = null;
        if (!ValidateUtils.isEmpty(orderno)) {
            String[] temp = orderno.split(",");
            for (String string : temp) {

                // M000003ServiceBean serviceBean = new M000003ServiceBean();
                // serviceBean.setUserId(uipo.getUserId());
                // serviceBean.setConstid(UserPresetConstants.ORG_ID);// 机构码
                // serviceBean.setOrderTypeId("B");// 基础业务为B，扩展业务待定
                // serviceBean.setProductId(UserPresetConstants.PRO_ID);// 产品号
                // serviceBean.setOpertype("2");// 操作类型（修改：2,新增：1,取消4,查询3）
                // serviceBean.setOrderDate(DateUtil.getDate());// 订单日期
                // serviceBean.setOrderTime(DateUtil.getDateTime());// 订单时间
                // serviceBean.setCheckCode(TYPE);
                // serviceBean.setUserOrderId(string);// 用户订单编号
                // serviceBean.setOrder(string);// 用户订单编号

                list.add(string);
            }
            logger.debug("调用dubbo接口com.rkylin.order.service.OrderInfoBaseService.updateOrder，参数:"
                    + JsonUtil.bean2JsonStr(list));
            loi = orderInfoBaseService.updateCheckCodeByOrderId(list);
            logger.debug("调用dubbo接口com.rkylin.order.service.OrderInfoBaseService.updateOrder，返回:"
                    + JsonUtil.bean2JsonStr(loi));
            if (!"ok".equals(loi)) {
                su = false;
                ar.setMsg(loi);
            }
        } else {
            su = false;
            ar.setMsg("订单编号为空");
        }
        // 以下是可选的参数
        /*
         * serviceBean.setGoodsName(null);// 商品名称 serviceBean.setAmount("");// 订单金额（若不存在商品概念则必填）
         * serviceBean.setGoodsDetail(null);// 商品描述 serviceBean.setNumber(null);// 商品数量
         * serviceBean.setUnitPrice(null);// 单价 serviceBean.setAdjustType(null);// 调整类型
         * serviceBean.setAdjustContent(null);// 调整内容 serviceBean.setUserRelateId(null);// 关联用户ID（若有第三方则必须填写
         */

        ar.addResult("data", loi);
        ar.setSuccess(su);
        return ar;
    }

    /**
     * 
     * Discription:查询余额
     * 
     * @param token
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月24日
     */
    @ResponseBody
    @RequestMapping("/getBalance")
    @SystemControllerLog(description = "查询余额")
    public ActionResult getBalance(String token) throws Exception {
        ActionResult ar = new ActionResult();
        /*
         * if (ValidateUtils.isEmpty(token)) { ar.setSuccess(false); ar.setCode(ErrorCodeEnum.LOGIN_NO.getCode());
         * ar.setMsg("未登录"); return ar; }
         */
        User user = new User();

        UserInfoVo uipo = null;
        try {
            uipo = TokenUtil.getData(token);
        } catch (Exception e) {
            ar.setSuccess(false);
            ar.setMsg("系统异常");
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            return ar;
        }
        /*
         * if (null == uipo || null == uipo.getUserId()) { ar.setSuccess(false);
         * ar.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode()); ar.setMsg("token已失效，请重新登录！"); return ar; }
         */
        user.userId = uipo.getUserId();
        user.constId = UserPresetConstants.ORG_ID;// 机构号
        user.productId = UserPresetConstants.PRO_ID;// 产品号(type传1时此字段必传)
        // 以下是可选的参数
        String finAccountId = "";// 账户ID
        /*
         * user.userType = null;// 用户类型 user.role = "";// 角色号 user.uEType = "";// 用户类型 user.payType = "";// 操作交易类型
         * user.userName = "";// 用户名称 user.referUserId = "";// 第三方账户ID 如P2P user.creditType = "";// 授信类型 101额度授信 102单笔授信
         * user.statusID = "1";// 账户状态 1正常 2冻结 3销户(现在传固定值1)
         */
        // BalanceResponse balance = paymentAccountServiceApi.getUserBalance(user, finAccountId, TYPE);
        logger.debug("调用dubbo接口com.rkylin.wheatfield.api.PaymentAccountServiceApi.getUserBalance，参数:"
                + JsonUtil.bean2JsonStr(user));
        BalanceVo balance = accountDubbo.getBalance(user, null, AccountConstants.BALANCE_GET_TYPE_1);
        ar.addResult("data", balance);

        logger.debug("调用dubbo接口com.rkylin.wheatfield.api.PaymentAccountServiceApi.getUserBalance，返回:"
                + JsonUtil.bean2JsonStr(balance));
        // ar.setMsg(balance.getMsg());
        ar.setSuccess(true);
        return ar;

    }

    @ResponseBody
    @RequestMapping("/getCreditBalance")
    @SystemControllerLog(description = "查询信用余额")
    public ActionResult getCreditBalance(String token) throws Exception {
        ActionResult ar = new ActionResult();
        /*
         * if (ValidateUtils.isEmpty(token)) { ar.setSuccess(false); ar.setCode(ErrorCodeEnum.LOGIN_NO.getCode());
         * ar.setMsg("未登录"); return ar; }
         */
        User user = new User();

        UserInfoVo uipo = null;
        try {
            uipo = TokenUtil.getData(token);
        } catch (Exception e) {
            ar.setSuccess(false);
            ar.setMsg("系统异常");
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            return ar;
        }
        /*
         * if (null == uipo || null == uipo.getUserId()) { ar.setSuccess(false);
         * ar.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode()); ar.setMsg("token已失效，请重新登录！"); return ar; }
         */
        user.userId = uipo.getUserId();
        user.constId = UserPresetConstants.ORG_ID;// 机构号
        user.productId = UserPresetConstants.PRO_CREDIT_ID;// 产品号(type传1时此字段必传)
        // 以下是可选的参数
        String finAccountId = "";// 账户ID
        /*
         * user.userType = null;// 用户类型 user.role = "";// 角色号 user.uEType = "";// 用户类型 user.payType = "";// 操作交易类型
         * user.userName = "";// 用户名称 user.referUserId = "";// 第三方账户ID 如P2P user.creditType = "";// 授信类型 101额度授信 102单笔授信
         * user.statusID = "1";// 账户状态 1正常 2冻结 3销户(现在传固定值1)
         */
        // BalanceResponse balance = paymentAccountServiceApi.getUserBalance(user, finAccountId, TYPE);
        logger.debug("调用dubbo接口com.rkylin.wheatfield.api.PaymentAccountServiceApi.getUserBalance，参数:"
                + JsonUtil.bean2JsonStr(user));
        BalanceVo balance = accountDubbo.getBalance(user, null, AccountConstants.BALANCE_GET_TYPE_1);
        ar.addResult("data", balance);

        logger.debug("调用dubbo接口com.rkylin.wheatfield.api.PaymentAccountServiceApi.getUserBalance，返回:"
                + JsonUtil.bean2JsonStr(balance));
        // ar.setMsg(balance.getMsg());
        ar.setSuccess(true);
        return ar;

    }

    private static List<LinkedHashMap<String, Object>> converList(List<OrderInfoVo> loi) {

        List<LinkedHashMap<String, Object>> list = new ArrayList<>();

        Map<String, List<OrderInfoVo>> map2 = aa(loi);

        for (String key : map2.keySet()) {

            LinkedHashMap<String, Object> mapaa = new LinkedHashMap<>();
            mapaa.put("date", key);
            mapaa.put("list", map2.get(key));
            list.add(mapaa);
        }

        return list;

    }

    private static LinkedHashMap<String, List<OrderInfoVo>> aa(List<OrderInfoVo> loi) {
        LinkedHashMap<String, List<OrderInfoVo>> map = new LinkedHashMap<>();
        for (OrderInfoVo o : loi) {
            String date = getDateTime("yyyy-MM", o.getOrderDate());
            if (map.containsKey(date)) {
                map.get(date).add(o);
            } else {
                List<OrderInfoVo> list = new ArrayList<>();
                list.add(o);
                map.put(date, list);
            }

        }
        return map;
    }

    private static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate == null) {
            logger.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }
        return returnValue;
    }

    private final List<OrderInfoVo> change(List<Map<String, Object>> list, UserInfoVo uifv)
            throws JsonParseException, JsonMappingException, IOException {
        List<OrderInfoVo> olist = new ArrayList<>();
        for (Map<String, Object> map : list) {
            OrderInfoVo orderInfo = BeanMapper.map(BeanMapper.map(map, OrderInfoMap.class), OrderInfoVo.class);
            String orderTypeId = orderInfo.getOrderTypeId();
            if ("3001".equals(orderTypeId)) {
                try {
                    UserInfoPo userInfoById = null;
                    if (uifv.getUserId().equals(orderInfo.getUserId())) {

                        if (StringUtils.isNotEmpty(orderInfo.getUserRelateId())) {

                            userInfoById =
                                    userInfoApiService.getUserInfoByUserId(Long.parseLong(orderInfo.getUserRelateId()));
                        } else {
                            logger.info("方法：queryOrder.change,UserRelateId为空:");
                        }
                        orderInfo.setType("1");
                    } else {
                        if (StringUtils.isNotEmpty(orderInfo.getUserId())) {
                            userInfoById =
                                    userInfoApiService.getUserInfoByUserId(Long.parseLong(orderInfo.getUserId()));
                        }
                        orderInfo.setType("2");
                    }
                    if (userInfoById != null) {
                        orderInfo.setUserRelateId(userInfoById.getUserRealName());
                    } else {
                        logger.info("方法：userInfoApiService.getUserInfoByUserId,userId:" + orderInfo.getUserRelateId()
                                + "没查询到用户信息");
                    }
                    // orderInfo.setUserRelateId(orderInfo.getAccountRealName());
                    orderInfo.setGoodsDetail("余额");
                    orderInfo.setOrderTypeId(OrderCodeEnum.MAP.get("3001"));
                } catch (NumberFormatException e) {
                    logger.error("格式化UserRelateId错误", e);
                } catch (Exception e) {
                    logger.error("取得用户真实姓名错误", e);
                }
            } else if (orderTypeId != null && orderTypeId.length() > 1
                    && (orderTypeId.startsWith("B1") || orderTypeId.startsWith("BX"))) {

                // OrderAccountInfo findAccountInfoByOrderId =
                // orderAccountInfoService.findAccountInfoByOrderId(orderInfo.getOrderId(), TYPE);
                // if (findAccountInfoByOrderId != null) {
                // orderInfo.setGoodsDetail(findAccountInfoByOrderId.getBankHeadName() + "(" + findAccountInfoByOrderId
                // .getAccountNumber().substring(findAccountInfoByOrderId.getAccountNumber().length() - 4)
                // + ")");
                // }
                orderInfo.setGoodsDetail(orderInfo.getBankHeadName() + "("
                        + orderInfo.getAccountNumber().substring(orderInfo.getAccountNumber().length() - 4) + ")");
                orderInfo.setOrderTypeId(OrderCodeEnum.MAP.get("B1"));
            } else if (orderTypeId != null && orderTypeId.length() > 1 && orderTypeId.startsWith("B2")) {
                orderInfo.setOrderTypeId(OrderCodeEnum.MAP.get("B2"));

            } else {
                orderInfo.setUserRelateId("");
                orderInfo.setGoodsDetail("余额");
            }

            orderInfo.setStatusId(OrderCodeEnum.MAP.get(orderInfo.getStatusId()));
            orderInfo.setGoodsName(OrderCodeEnum.PAY_TYPE_MAP.get(orderInfo.getGoodsName()));

            orderInfo.setBankHeadName(null);
            orderInfo.setAccountNumber(null);
            olist.add(orderInfo);
        }
        return olist;
    }

    public static void main(String[] args) throws Exception {

        List<OrderInfoVo> loi = new ArrayList<>();

        OrderInfoVo o_1_1 = new OrderInfoVo();
        o_1_1.setOrderDate(DateUtil.convertStringToDate("2016-01-01"));
        o_1_1.setOrderId("o_1_1");
        loi.add(o_1_1);

        OrderInfoVo o_1_2 = new OrderInfoVo();
        o_1_2.setOrderDate(DateUtil.convertStringToDate("2016-01-02"));
        o_1_2.setOrderId("o_1_2");
        loi.add(o_1_2);

        OrderInfoVo o_1_3 = new OrderInfoVo();
        o_1_3.setOrderDate(DateUtil.convertStringToDate("2016-01-03"));
        o_1_3.setOrderId("o_1_3");
        loi.add(o_1_3);

        OrderInfoVo o_2_1 = new OrderInfoVo();
        o_2_1.setOrderDate(DateUtil.convertStringToDate("2016-02-01"));
        o_2_1.setOrderId("o_2_1");
        loi.add(o_2_1);

        OrderInfoVo o_2_2 = new OrderInfoVo();
        o_2_2.setOrderDate(DateUtil.convertStringToDate("2016-02-01"));
        o_2_2.setOrderId("o_2_2");
        loi.add(o_2_1);

        OrderInfoVo o_2_3 = new OrderInfoVo();
        o_2_3.setOrderDate(DateUtil.convertStringToDate("2016-02-01"));
        o_2_3.setOrderId("o_2_3");
        loi.add(o_2_3);

        OrderInfoVo o_2_4 = new OrderInfoVo();
        o_2_4.setOrderDate(DateUtil.convertStringToDate("2016-02-01"));
        o_2_4.setOrderId("o_2_4");
        loi.add(o_2_4);

        // List<LinkedHashMap<String, Object>> list = converList(loi);

        // System.out.println(JsonUtil.bean2JsonStr(list));

        Map<String, Object> map = Maps.newHashMap();

        map.put("orderId", "1");
        map.put("ORDER_TYPE_ID", "1");
        OrderInfoMap orderInfo = BeanMapper.map(map, OrderInfoMap.class);
        OrderInfoVo orderInfo1 = BeanMapper.map(BeanMapper.map(map, OrderInfoMap.class), OrderInfoVo.class);

        System.out.println(orderInfo1.getOrderId());
        System.out.println(orderInfo1.getOrderTypeId());

        String a = "B11111";

        System.out.println(a.substring(0, 2));
    }
}

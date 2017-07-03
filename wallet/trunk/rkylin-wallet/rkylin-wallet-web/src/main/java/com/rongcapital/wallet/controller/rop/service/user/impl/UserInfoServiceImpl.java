package com.rongcapital.wallet.controller.rop.service.user.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.service.LoginAipService;
import com.rongcapital.wallet.api.service.TokenApiService;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.api.vo.BankInfoVo;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.PwdConstants;
import com.rongcapital.wallet.constants.Settings;
import com.rongcapital.wallet.constants.VersionConstants;
import com.rongcapital.wallet.controller.rop.constants.RopErrorCodeEnum;
import com.rongcapital.wallet.controller.rop.response.BaseResponse;
import com.rongcapital.wallet.controller.rop.response.ErrorResponse;
import com.rongcapital.wallet.controller.rop.response.Response;
import com.rongcapital.wallet.controller.rop.service.user.UserInfoService;
import com.rongcapital.wallet.controller.rop.utils.ParamValidateUtils;
import com.rongcapital.wallet.controller.user.UserController;
import com.rongcapital.wallet.dubbo.AccountDubbo;
import com.rongcapital.wallet.dubbo.BankCardDubbo;
import com.rongcapital.wallet.dubbo.vo.BalanceVo;
import com.rongcapital.wallet.rop.PwdRop;
import com.rongcapital.wallet.threads.SendPoolService;
import com.rongcapital.wallet.util.BankUtil;
import com.rongcapital.wallet.util.RedisIdGeneratorExt;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.encryption.EncryptionUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.random.SaltUtil;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class UserInfoServiceImpl implements UserInfoService {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    SendPoolService sendPoolService;

    @Autowired
    private UserInfoApiService userInfoApiService;

    @Autowired
    private LoginAipService loginAipService;

    @Autowired
    private AccountDubbo accountDubbo; // 余额信息

    @Autowired
    private BankCardDubbo bankCardDubbo; // 余额信息

    @Autowired
    private RedisIdGeneratorExt redisIdGeneratorExt;

    @Autowired
    private TokenApiService tokenApiService;

    @Override
    public Response login(Map<String, String[]> requestParams) throws Exception {

        BaseResponse response = new BaseResponse();
        String[] userNames = requestParams.get("username");
        ErrorResponse validateUserName = com.rongcapital.wallet.controller.rop.utils.ParamValidateUtils
                .validateParamIsEmpty(userNames, RopErrorCodeEnum.PARM_CHECK_NULL.getCode(), "username不能为空");
        if (null != validateUserName) {
            return validateUserName;
        }

        String[] pwds = requestParams.get("pwd");
        ErrorResponse validatePwd =
                ParamValidateUtils.validateParamIsEmpty(pwds, RopErrorCodeEnum.PARM_CHECK_NULL.getCode(), "pwd不能为空");
        if (null != validatePwd) {
            return validatePwd;
        }

        String[] userTypes = requestParams.get("usertype");
        ErrorResponse validateUsertype = com.rongcapital.wallet.controller.rop.utils.ParamValidateUtils
                .validateParamIsEmpty(userTypes, RopErrorCodeEnum.PARM_CHECK_NULL.getCode(), "usertype不能为空");
        if (null != validateUsertype) {
            return validateUsertype;
        }

        String userName = userNames[0];
        String pwd = pwds[0];
        String userType = userTypes[0];

        /*-----------------登陆逻辑------------------------*/
        /*----判断用户是否存在-------*/
        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userName);
        if (null == loginPo) {
            response.setResponsecode(RopErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
            response.setMessage(RopErrorCodeEnum.LOGIN_PWD_ERROR.getValue());
            return response;
        }

        // 查询用户信息

        UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());

        logger.info("UserController.login用户登陆信息:{" + JsonUtil.bean2JsonStr(response) + "}");

        if (null == userInfo) {

            response.setResponsecode(RopErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
            response.setMessage(RopErrorCodeEnum.LOGIN_PWD_ERROR.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(response) + "}");
            return response;
        }

        userInfo.setUserName(userName); // 登陆名

        if (userInfo.getStatus() != 1) {
            response.setResponsecode(RopErrorCodeEnum.USER_STATUS_ERR.getCode());
            response.setMessage(RopErrorCodeEnum.USER_STATUS_ERR.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(response) + "}");
            return response;
        }

        // 验证密码
        if (!EncryptionUtil.validate(pwd, loginPo.getPwdSalt(), loginPo.getPwd())) {
            response.setResponsecode(RopErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
            response.setMessage(RopErrorCodeEnum.LOGIN_PWD_ERROR.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(response) + "}");
            return response;
        }

        // tonken处理

        String tonken = EncryptionUtil.md5Encry(String.valueOf(System.currentTimeMillis()), SaltUtil.createSalt());

        AccountInfoQuery query = new AccountInfoQuery();
        query.setRootInstCd(userInfo.getOrgId()); // 机构ID
        query.setProductId(userInfo.getProId());
        query.setAccountName(userInfo.getUserId()); // 用户ID

        // 取银行卡列表
        List<AccountInfo> bankInfoList = bankCardDubbo.getBankList(query);
        if (null != bankInfoList) {
            logger.info("UserController.login取银行卡列表dubbo结果:{" + JsonUtil.bean2JsonStr(bankInfoList) + "}");
        }

        List<BankInfoVo> bankList = BankUtil.converBank(bankInfoList);

        if (null != bankList) {
            logger.info("UserController.login取银行卡列表转换后结果:{" + JsonUtil.bean2JsonStr(bankList) + "}");
        }
        // 查余额

        User u = new User();

        u.userId = userInfo.getUserId();
        u.constId = userInfo.getOrgId();
        u.productId = userInfo.getProId();

        BalanceVo balance = accountDubbo.getBalance(u, null, AccountConstants.BALANCE_GET_TYPE_1);
        if (null != balance) {
            logger.info("UserController.login获取余额信息dubbo返回:{" + JsonUtil.bean2JsonStr(balance) + "}");
        }

        ClientInfo clientInfo = new ClientInfo();
        // tonken处理
        TokenUtil.setData(clientInfo, userInfo, tonken);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setOrgId(userInfo.getOrgId());
        userInfoVo.setUserId(userInfo.getUserId());
        ResultVo vo = PwdRop.isExistsPwd(userInfoVo, PwdConstants.PWD_TYPE_PAY);
        if (vo.isSuccess()) {
            response.addResult("isExistsPayPwd", true);
        } else {
            response.addResult("isExistsPayPwd", false);
        }
        // 拼数据

        response.addResult("userId", userInfo.getUserId());
        response.addResult("userName", userInfo.getUserName());
        response.addResult("userRealName", userInfo.getUserRealName());
        response.addResult("idCard", userInfo.getIdCard());
        response.addResult("orgId", userInfo.getOrgId());
        // result.addResult("bankList", bankList);
        // result.addResult("balance", balance);

        response.addResult("tonken", tonken);
        response.addResult("version", VersionConstants.VESION_NEW);
        response.addResult("assistantUrl", Settings.getInstance().getAssistantUrl());

        // 记录日志
        // UserLoginHistoryPo po = new UserLoginHistoryPo(getIpAddr(request), userInfo.getUserInfoId(),
        // SystemConstants.LOGIN_TYPE_ENTER, ClientConstants.clientMap.get(clientInfo.getClient()));
        // sendPoolService.startServer(po, loginAipService);

        logger.info("UserController.login接口返回:{" + JsonUtil.bean2JsonStr(response) + "}");

        return response;
    }

}

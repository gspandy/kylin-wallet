package com.rongcapital.wallet.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rkylin.wheatfield.pojo.User;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginHistoryPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.service.LoginAipService;
import com.rongcapital.wallet.api.service.TokenApiService;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.api.vo.BankInfoVo;
import com.rongcapital.wallet.api.vo.PersonInfoVo;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.ClientConstants;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.PwdConstants;
import com.rongcapital.wallet.constants.Settings;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.constants.UserInfoConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.constants.VersionConstants;
import com.rongcapital.wallet.dubbo.AccountDubbo;
import com.rongcapital.wallet.dubbo.BankCardDubbo;
import com.rongcapital.wallet.dubbo.vo.BalanceVo;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.rop.AccountRop;
import com.rongcapital.wallet.rop.PwdRop;
import com.rongcapital.wallet.rop.UserRop;
import com.rongcapital.wallet.threads.SendPoolService;
import com.rongcapital.wallet.util.BankUtil;
import com.rongcapital.wallet.util.RedisIdGeneratorExt;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.encryption.EncryptionUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.random.SaltUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

@Controller
@RequestMapping("/user")
public class UserController {

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

    @ResponseBody
    @RequestMapping("/register")
    @SystemControllerLog(description = "注册")
    public ActionResult register(ClientInfo clientInfo, UserInfoVo userInfo) throws Exception {

        ActionResult result = new ActionResult();

        if (true) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            return result;
        }

        if (null == userInfo || StringUtils.isEmpty(userInfo.getIdCard()) || StringUtils.isEmpty(userInfo.getUserName())
                || StringUtils.isEmpty(userInfo.getUserRealName()) || StringUtils.isEmpty(userInfo.getPwd())) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("UserController.register返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        logger.info("UserController.register入参:{" + JsonUtil.bean2JsonStr(userInfo) + "}");

        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userInfo.getUserName());
        if (null != loginPo) {

            result.setCode(ErrorCodeEnum.REGISTER_EXISTS.getCode());
            result.setMsg(ErrorCodeEnum.REGISTER_EXISTS.getValue());
            logger.info("UserController.register返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 补齐信息
        userInfo.setOrgId(UserPresetConstants.ORG_ID);
        userInfo.setProId(UserPresetConstants.PRO_ID);
        userInfo.setIdCardType(AccountConstants.CERTIFICATE_TYPE_IDENTITY);

        // 实名认证

        ResultVo vo = UserRop.identityAuth(userInfo);

        if (!vo.isSuccess()) {
            result.setCode(ErrorCodeEnum.IDENTITYAUTH.getCode());
            result.setMsg(ErrorCodeEnum.IDENTITYAUTH.getValue());
            logger.info("UserController.register返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 通过身份证信息去查是否开过户口

        // 开户
        String operType = AccountConstants.ACCOUNT_OPEN_OPERTYPE_SAVE; // 操作类型（1：新增，2：修改）

        String userId = redisIdGeneratorExt.getRadomUserId();

        userInfo.setUserId(userId); // 系统生成

        ResultVo openVo = AccountRop.openPersonAccount(userInfo, operType);

        if (!openVo.isSuccess()) {
            result.setCode(ErrorCodeEnum.OPEN_ACCOUNT.getCode());
            result.setMsg(null != openVo.getMsg() && !"".equals(openVo.getMsg()) ? openVo.getMsg()
                    : ErrorCodeEnum.OPEN_ACCOUNT.getValue());
            logger.info("UserController.register返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 入库
        UserInfoPo po = new UserInfoPo();
        po.setUserId(userId);
        po.setIdCardType(AccountConstants.CERTIFICATE_TYPE_IDENTITY); // 身份证类型
        po.setIdCard(userInfo.getIdCard());
        po.setUserName(userInfo.getUserName());
        po.setUserRealName(userInfo.getUserRealName());
        /*-----密码处理------*/
        String salt = SaltUtil.createSalt();
        String newPwd = EncryptionUtil.sha256Encry(userInfo.getPwd(), salt);
        po.setSalt(salt);
        po.setPwd(newPwd);
        po.setUserType(Integer.parseInt(UserInfoConstants.ACCOUNT_PROPERTY_PERSONAGE));

        logger.info("UserController.register返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        if (userInfoApiService.register(po)) {

            result.setSuccess(true);
            result.addResult("success", true);
            logger.info("UserController.register返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");

        } else {
            result.setCode(ErrorCodeEnum.REGISTER_ERR.getCode());
            result.setMsg(ErrorCodeEnum.REGISTER_ERR.getValue());
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/login")
    @SystemControllerLog(description = "登录")
    public ActionResult login(ClientInfo clientInfo, String userName, String pwd, String userType,
            HttpServletRequest request) throws Exception {

        ActionResult result = new ActionResult();

        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(userType)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        logger.info("UserController.login入参:{" + JsonUtil.bean2JsonStr(result) + "}");
        if (!userType.equals(UserInfoConstants.ACCOUNT_PROPERTY_PERSONAGE)) {
            result.setCode(ErrorCodeEnum.LOGIN_AUTH.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_AUTH.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 查找用户是否存在
        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userName);
        if (null == loginPo) {
            result.setCode(ErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_PWD_ERROR.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 查询用户信息

        UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());

        logger.info("UserController.login用户登陆信息:{" + JsonUtil.bean2JsonStr(result) + "}");

        if (null == userInfo) {

            result.setCode(ErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_PWD_ERROR.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        userInfo.setUserName(userName); // 登陆名

        if (userInfo.getStatus() != 1) {
            result.setCode(ErrorCodeEnum.USER_STATUS_ERR.getCode());
            result.setMsg(ErrorCodeEnum.USER_STATUS_ERR.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 验证密码
        if (!EncryptionUtil.validate(pwd, loginPo.getPwdSalt(), loginPo.getPwd())) {
            result.setCode(ErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_PWD_ERROR.getValue());
            logger.info("UserController.login返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
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

        // 1 instCode String 机构号 N
        // 2 userId String 用户id N
        // 3 productId String 产品号 Y
        // 4 name String 用户名或公司名称 Y
        // 5 cardNo String 卡号 Y

        User u = new User();

        u.userId = userInfo.getUserId();
        u.constId = userInfo.getOrgId();
        u.productId = userInfo.getProId();

        BalanceVo balance = accountDubbo.getBalance(u, null, AccountConstants.BALANCE_GET_TYPE_1);
        if (null != balance) {
            logger.info("UserController.login获取余额信息dubbo返回:{" + JsonUtil.bean2JsonStr(balance) + "}");
        }

        // tonken处理
        TokenUtil.setData(clientInfo, userInfo, tonken);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setOrgId(userInfo.getOrgId());
        userInfoVo.setUserId(userInfo.getUserId());
        ResultVo vo = PwdRop.isExistsPwd(userInfoVo, PwdConstants.PWD_TYPE_PAY);
        if (vo.isSuccess()) {
            result.addResult("isExistsPayPwd", true);
        } else {
            result.addResult("isExistsPayPwd", false);
        }
        // 拼数据

        result.addResult("userId", userInfo.getUserId());
        result.addResult("userName", userInfo.getUserName());
        result.addResult("userRealName", userInfo.getUserRealName());
        result.addResult("idCard", userInfo.getIdCard());
        result.addResult("orgId", userInfo.getOrgId());
        // result.addResult("bankList", bankList);
        // result.addResult("balance", balance);

        result.addResult("tonken", tonken);
        result.addResult("version", VersionConstants.VESION_100);
        result.addResult("assistantUrl", Settings.getInstance().getAssistantUrl());

        result.setSuccess(true);

        // 记录日志
        UserLoginHistoryPo po = new UserLoginHistoryPo(getIpAddr(request), userInfo.getUserInfoId(),
                SystemConstants.LOGIN_TYPE_ENTER, ClientConstants.clientMap.get(clientInfo.getClient()));
        sendPoolService.startServer(po, loginAipService);

        logger.info("UserController.login接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;

    }

    /**
     * 判断登陆名是否存在 Discription:
     * 
     * @param clientInfo
     * @param loginName
     * @return ActionResult
     * @author libi
     * @since 2016年8月31日
     */
    @ResponseBody
    @RequestMapping("/loginNameIsExist")
    @SystemControllerLog(description = "判断登陆名是否存在")
    public ActionResult loginNameIsExis(ClientInfo clientInfo, String userName, String userType) throws Exception {

        ActionResult result = new ActionResult();

        if (ValidateUtils.isEmpty(userName) || ValidateUtils.isEmpty(userType)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("UserController.loginNameIsExist接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        logger.info("UserController.loginNameIsExist入参:{userName:" + userName + ",userType" + userType + "}");

        if (loginAipService.loginNameIsExist(userName)) {
            result.setCode(ErrorCodeEnum.REGISTER_EXISTS.getCode());
            result.setMsg(ErrorCodeEnum.REGISTER_EXISTS.getValue());

        } else {

            result.setSuccess(true);
            result.addResult("success", true);
        }

        return result;
    }

    /**
     * 判断登陆名是否存在 Discription:
     * 
     * @param clientInfo
     * @param loginName
     * @return ActionResult
     * @author libi
     * @since 2016年8月31日
     */
    @ResponseBody
    @RequestMapping("/validateloginInfo")
    @SystemControllerLog(description = "判断用户信息")
    public ActionResult validateloginInfo(ClientInfo clientInfo, UserInfoVo userInfoVo) throws Exception {

        ActionResult result = new ActionResult();

        if (ValidateUtils.isEmpty(userInfoVo.getUserName()) || ValidateUtils.isEmpty(userInfoVo.getUserRealName())
                || ValidateUtils.isEmpty(userInfoVo.getIdCard())) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("UserController.validateloginInfo接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userInfoVo.getUserName());
        if (null == loginPo) {
            result.setCode(ErrorCodeEnum.REGISTER_NOTEXISTS.getCode());
            result.setMsg(ErrorCodeEnum.REGISTER_NOTEXISTS.getValue());
            logger.info("UserController.validateloginInfo返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }
        UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());
        if (userInfoVo.getIdCard().equals(userInfo.getIdCard())
                && userInfoVo.getUserRealName().equals(userInfo.getUserRealName())) {
            result.setSuccess(true);
            result.addResult("success", true);
            logger.info("UserController.validateloginInfo返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");

        } else {
            result.setCode(ErrorCodeEnum.LOGIN_INFO_VALIDATE_ERROR.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_INFO_VALIDATE_ERROR.getValue());
            logger.info("UserController.validateloginInfo接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");

        }

        return result;

    }

    /**
     * 退出登陆接口 Discription:
     * 
     * @param clientInfo
     * @return ActionResult
     * @author libi
     * @since 2016年9月1日
     */
    @ResponseBody
    @RequestMapping("/loginOut")
    @SystemControllerLog(description = "退出登陆接口")
    public ActionResult loginOut(ClientInfo clientInfo, HttpServletRequest request) throws Exception {

        ActionResult result = new ActionResult();
     
        // 记录日志
        UserInfoVo userInfo = TokenUtil.getData(clientInfo.getToken());
        UserLoginHistoryPo po = new UserLoginHistoryPo(getIpAddr(request), userInfo.getUserInfoId(),
                SystemConstants.LOGIN_TYPE_OUT, ClientConstants.clientMap.get(clientInfo.getClient()));
        sendPoolService.startServer(po, loginAipService);

        TokenUtil.delToken(clientInfo.getToken());
        result.setSuccess(true);
        result.addResult("success", true);
        logger.info("UserController.loginOut接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;
    }

    @ResponseBody
    @RequestMapping("/identityAuth")
    @SystemControllerLog(description = "identityAuth")
    public ActionResult identityAuth(ClientInfo clientInfo, UserInfoVo userInfo) throws Exception {

        ActionResult result = new ActionResult();
        if (null == userInfo || StringUtils.isEmpty(userInfo.getIdCard())
                || StringUtils.isEmpty(userInfo.getUserRealName())) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("UserController.identityAuth接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        logger.info("UserController.identityAuth接口入参:{" + JsonUtil.bean2JsonStr(userInfo) + "}");

        // 补齐信息
        userInfo.setOrgId(UserPresetConstants.ORG_ID);
        userInfo.setProId(UserPresetConstants.PRO_ID);
        userInfo.setIdCardType(AccountConstants.CERTIFICATE_TYPE_IDENTITY);
        // 实名认证
        ResultVo vo = UserRop.identityAuth(userInfo);

        if (vo.isSuccess()) {
            result.setSuccess(true);
            result.addResult("success", true);
            logger.info("UserController.identityAuth接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");
        } else {
            result.setCode(ErrorCodeEnum.IDENTITYAUTH.getCode());
            result.setMsg(ErrorCodeEnum.IDENTITYAUTH.getValue());
            logger.info("UserController.identityAuth接口返回:{" + JsonUtil.bean2JsonStr(result) + "}");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/userAuth")
    @SystemControllerLog(description = "userAuth")
    public ActionResult userAuth(ClientInfo clientInfo, UserInfoVo userInfo) throws Exception {

        ActionResult result = new ActionResult();
        if (ValidateUtils.isEmpty(clientInfo) || ValidateUtils.isEmpty(userInfo.getUserName())
                || ValidateUtils.isEmpty(userInfo.getUserRealName()) || StringUtils.isEmpty(userInfo.getIdCard())) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            return result;
        }

        // 补齐信息
        UserLoginPo loginPo = loginAipService.getLoginInfoByName((userInfo.getUserName()));
        if (!ValidateUtils.isEmpty(loginPo)) {

            PersonInfoVo vo = loginAipService.getPersonInfoByUserId(loginPo.getUserInfoId());
            if (!ValidateUtils.isEmpty(vo)) {

                if (userInfo.getUserRealName().equals(vo.getUserCname())
                        && userInfo.getIdCard().equals(vo.getIdNumber())) {
                    result.setSuccess(true);
                    result.addResult("success", true);
                } else {
                    result.setCode(ErrorCodeEnum.IDENTITYAUTH.getCode());
                    result.setMsg(ErrorCodeEnum.IDENTITYAUTH.getValue());
                    return result;
                }

            } else {
                result.setCode(ErrorCodeEnum.REGISTER_NOTEXISTS.getCode());
                result.setMsg(ErrorCodeEnum.REGISTER_NOTEXISTS.getValue());
                return result;
            }

        } else {
            result.setCode(ErrorCodeEnum.REGISTER_NOTEXISTS.getCode());
            result.setMsg(ErrorCodeEnum.REGISTER_NOTEXISTS.getValue());
        }

        return result;
    }

    /**
     * 
     * Discription:查询用户信息
     * 
     * @param clientInfo
     * @param loginName
     * @param userType
     * @return ActionResult
     * @author Administrator
     * @since 2016年9月2日
     */
    @ResponseBody
    @RequestMapping("/getUserInfo")
    @SystemControllerLog(description = "查询用户信息")
    public ActionResult getUserInfo(ClientInfo clientInfo, String loginName, String userType) throws Exception {

        ActionResult ar = new ActionResult();

        UserInfoVo uipo = TokenUtil.getData(clientInfo.getToken());

        if (ValidateUtils.isEmpty(loginName)) {

            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            return ar;
        }
        if (ValidateUtils.isEmpty(userType) || "2".equals(userType.trim())) {
            UserLoginPo loginInfoByName = loginAipService.getLoginInfoByName(loginName);
            if (null == loginInfoByName) {

                ar.setCode(ErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
                ar.setMsg("没查到相关账户");
                return ar;
            } else {
                UserInfoPo userInfoById = null;
                try {
                    userInfoById = userInfoApiService.getUserInfoById(loginInfoByName.getUserInfoId());
                } catch (Exception e) {
                    logger.error("调用 userInfoApiService.getUserInfoById 异常", e);

                    ar.setMsg("系统异常");
                    ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                    return ar;
                }
                if (null != userInfoById) {

                    ar.addResult("userid", userInfoById.getUserId());
                    ar.addResult("userName", loginInfoByName.getLoginName());
                    ar.addResult("userRealName", userInfoById.getUserRealName());
                    ar.addResult("idCard", userInfoById.getIdCard());

                } else {
                    ar.setSuccess(false);
                    ar.setCode(ErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
                    ar.setMsg("没查到相关账户");
                    return ar;
                }
            }
        } else {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.LOGIN_PWD_ERROR.getCode());
            ar.setMsg("企业账户暂时不提供查询");
            return ar;
        }

        ar.setSuccess(true);
        return ar;
    }

    @ResponseBody
    @RequestMapping("/main")
    public ActionResult main(ClientInfo clientInfo) throws Exception {

        ActionResult result = new ActionResult();

        UserInfoVo userInfo = TokenUtil.getData(clientInfo.getToken());

        AccountInfoQuery query = new AccountInfoQuery();
        query.setRootInstCd(UserPresetConstants.ORG_ID); // 机构ID
        query.setProductId(UserPresetConstants.PRO_ID);
        query.setAccountName(userInfo.getUserId()); // 用户ID

        // 取银行卡列表
        List<AccountInfo> bankInfoList = bankCardDubbo.getBankList(query);
        List<BankInfoVo> bankList = BankUtil.converBank(bankInfoList);

        // 查余额

        // 1 instCode String 机构号 N
        // 2 userId String 用户id N
        // 3 productId String 产品号 Y
        // 4 name String 用户名或公司名称 Y
        // 5 cardNo String 卡号 Y

        User u = new User();

        u.userId = userInfo.getUserId();
        u.constId = userInfo.getOrgId();
        u.productId = userInfo.getProId();

        BalanceVo balance = accountDubbo.getBalance(u, null, AccountConstants.BALANCE_GET_TYPE_1);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setOrgId(UserPresetConstants.ORG_ID);
        userInfoVo.setUserId(userInfo.getUserId());
        ResultVo vo = PwdRop.isExistsPwd(userInfoVo, PwdConstants.PWD_TYPE_PAY);
        if (vo.isSuccess()) {
            result.addResult("isExistsPayPwd", true);
        } else {
            result.addResult("isExistsPayPwd", false);
        }
        // 拼数据

        result.addResult("userId", userInfo.getUserId());
        result.addResult("userName", userInfo.getUserName());
        result.addResult("userRealName", userInfo.getUserRealName());
        result.addResult("idCard", userInfo.getIdCard());
        result.addResult("bankList", bankList);
        result.addResult("balance", balance);
        result.setSuccess(true);
        return result;
    }

    @ResponseBody
    @RequestMapping("/validateToken")
    public ActionResult validateToken(ClientInfo clientInfo, String userId) throws Exception {

        ActionResult result = new ActionResult();

        if (tokenApiService.validateToken(clientInfo.getToken(), userId)) {
            result.setSuccess(true);
            result.addResult("success", true);
        } else {
            result.setCode(6666);
            result.setMsg("token不存在");
            return result;
        }

        return result;
    }

    /**
     * 取ip信息 Discription:
     * 
     * @param request
     * @return String
     * @author libi
     * @since 2016年10月10日
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

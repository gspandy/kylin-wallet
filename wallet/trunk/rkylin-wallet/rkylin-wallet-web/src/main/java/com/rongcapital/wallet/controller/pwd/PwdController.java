package com.rongcapital.wallet.controller.pwd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongcapital.passguard.response.BaseResultVo;
import com.rongcapital.passguard.response.CheckPwdResult;
import com.rongcapital.passguard.response.UserRequestData;
import com.rongcapital.passguard.service.PasswordGuardManageService;
import com.rongcapital.redis.sdr.base.BaseRedisService;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.service.LoginAipService;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.PwdConstants;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.constants.UserInfoConstants;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.util.SmsUtil;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.encryption.EncryptionUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.random.SaltUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.SmsInfoVo;
import com.rongcapital.wallet.vo.UserInfoVo;

/**
 * 密码 Description:
 * 
 * @author: libi
 * @CreateDate: 2016年8月22日
 * @version: V1.0
 */

@Controller
@RequestMapping("/pwd")
public class PwdController {

    private static Logger logger = LoggerFactory.getLogger(PwdController.class);

    @Autowired
    private LoginAipService loginAipService;
    @Autowired
    private PasswordGuardManageService passwordGuardManageService;

    @Autowired
    private UserInfoApiService userInfoApiService;

    @Autowired
    private BaseRedisService<String, String> baseRedisService;

    /**
     * 支付密码操作 Discription:
     * 
     * @param clientInfo
     * @param userId
     * @param pwd
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月22日
     */
    @ResponseBody
    @RequestMapping("/payPwd")
    @SystemControllerLog(description = "支付密码操作")
    public ActionResult pwdPwd(ClientInfo clientInfo, String pwd, String type) throws Exception {

        ActionResult result = new ActionResult();
        if (ValidateUtils.isEmpty(pwd) || ValidateUtils.isEmpty(type)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("PwdController.payPwd返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }
        logger.info("PwdController.payPwd方法入参:{pwd:" + pwd + ",type:" + type + "}");

        UserInfoVo user = TokenUtil.getData(clientInfo.getToken());

        UserRequestData userRequestData = new UserRequestData();
        userRequestData.setUserId(user.getUserId());
        userRequestData.setOrgId(user.getOrgId());
        userRequestData.setProductId(user.getProId());
        userRequestData.setTerminalType(UserRequestData.TerminalType.APP);
        userRequestData.setEncryptPassword(pwd);
        userRequestData.setType(UserRequestData.Type.P);

        logger.info("PwdController.payPwd-dubbo-userRequestData入参:{" + JsonUtil.bean2JsonStr(userRequestData) + "}");

        // 创建，更新，验证
        if (type.equals(PwdConstants.PWD_TYPE_CHECK)) {

            BaseResultVo<CheckPwdResult> resultVo = passwordGuardManageService.checkPassword(userRequestData);
            logger.info("PwdController.payPwd-checkPassword-dubbo返回:{" + JsonUtil.bean2JsonStr(resultVo) + "}");
            if (resultVo.isSuccess()) {

                result.setSuccess(true);
                result.addResult("success", true);
                logger.info("PwdController.payPwd返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
                return result;
            } else {
                result.setCode(ErrorCodeEnum.PAYPWD_ERROR.getCode());
                result.setMsg(resultVo.getErrorMsg());
                logger.info("PwdController.payPwd返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
                return result;
            }

        } else {
            if (type.equals(PwdConstants.PWD_TYPE_SAVE)) {

                userRequestData.setOperType(UserRequestData.OperType.INSERT);
            } else if (type.equals(PwdConstants.PWD_TYPE_UPDATE)) {

                userRequestData.setOperType(UserRequestData.OperType.UPDATE);
            }
            BaseResultVo<String> resultVo = passwordGuardManageService.savePassword(userRequestData);
            logger.info("PwdController.payPwd-savePassword-dubbo返回:{" + JsonUtil.bean2JsonStr(resultVo) + "}");

            if (resultVo.isSuccess()) {
                result.setSuccess(true);
                result.addResult("success", true);

            } else {
                result.setCode(ErrorCodeEnum.PAYPWD_ERROR.getCode());
                result.setMsg(resultVo.getErrorMsg());

            }

        }

        logger.info("PwdController.payPwd返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;
    }

    /**
     * 登陆密码操作 Discription:
     * 
     * @param clientInfo
     * @param userId
     * @param pwd
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月22日
     */
    @ResponseBody
    @RequestMapping("/loginPwd")
    @SystemControllerLog(description = "登陆密码操作")
    public ActionResult loginPwd(ClientInfo clientInfo, String userName, String pwd, String type) throws Exception {
        ActionResult result = new ActionResult();

        if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(type)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("PwdController.loginPwd返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }
        logger.info("PwdController.loginPwd方法入参:{pwd:" + pwd + "type:" + type + "}");

        UserInfoVo user = TokenUtil.getData(clientInfo.getToken());
        userName = user.getUserName();

        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userName);

        if (type.equals(PwdConstants.PWD_LOGIN_UPDATE)) {
            String salt = SaltUtil.createSalt();
            String pwdNew = EncryptionUtil.sha256Encry(pwd, salt);
            loginPo.setPwdSalt(salt);
            loginPo.setPwd(pwdNew);
            loginAipService.updateLoginInfo(loginPo);
            result.addResult("success", true);
            result.setSuccess(true);
        } else if (type.equals(PwdConstants.PWD_LOGIN_VALIDATE)) {

            if (!EncryptionUtil.validate(pwd, loginPo.getPwdSalt(), loginPo.getPwd())) {
                result.setCode(ErrorCodeEnum.PWD_VILIDATE_ERRO.getCode());
                result.setMsg(ErrorCodeEnum.PWD_VILIDATE_ERRO.getValue());

            } else {
                result.setSuccess(true);
                result.addResult("success", true);

            }

        }

        logger.info("PwdController.loginPwd返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;

    }

    /**
     * 老密码验证 Discription:
     * 
     * @param clientInfo
     * @param userName
     * @param pwd
     * @param type
     * @return
     * @throws Exception ActionResult
     * @author libi
     * @since 2016年10月27日
     */
    @ResponseBody
    @RequestMapping("/loginPwdValidate")
    @SystemControllerLog(description = "登陆密码验证")
    public ActionResult loginPwdValidate(ClientInfo clientInfo, String pwd) throws Exception {
        ActionResult result = new ActionResult();

        if (StringUtils.isEmpty(pwd)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("PwdController.loginPwdValidate返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        UserInfoVo user = TokenUtil.getData(clientInfo.getToken());
        String userName = user.getUserName();
        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userName);

        if (!EncryptionUtil.validate(pwd, loginPo.getPwdSalt(), loginPo.getPwd())) {
            result.setCode(ErrorCodeEnum.PWD_VILIDATE_ERRO.getCode());
            result.setMsg(ErrorCodeEnum.PWD_VILIDATE_ERRO.getValue());

        } else {
            result.setSuccess(true);
            result.addResult("success", true);

        }
        logger.info("PwdController.loginPwdValidate返回结果:{"+JsonUtil.bean2JsonStr(result)+"}");return result;
    }

  

    /**
     * 修改密码
     * 
     * @param clientInfo
     * @param userName
     * @param pwd
     * @param type
     * @return
     * @throws Exception ActionResult
     * @author libi
     * @since 2016年10月27日
     */
    @ResponseBody
    @RequestMapping("/loginPwdUpdate")
    @SystemControllerLog(description = "登陆密码修改")
    public ActionResult loginPwdUpdate(ClientInfo clientInfo, String pwd, String newPwd)
            throws Exception {
        ActionResult result = new ActionResult();

        if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(newPwd)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("PwdController.loginPwd返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        UserInfoVo user = TokenUtil.getData(clientInfo.getToken());
        String userName = user.getUserName();

        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userName);
        
        /*--------验证原密码------------*/
        if (!EncryptionUtil.validate(pwd, loginPo.getPwdSalt(), loginPo.getPwd())) {
            result.setCode(ErrorCodeEnum.PWD_VILIDATE_ERRO.getCode());
            result.setMsg(ErrorCodeEnum.PWD_VILIDATE_ERRO.getValue());
            logger.info("PwdController.loginPwdUpdate返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }
        
            String salt = SaltUtil.createSalt();
            String pwdNew = EncryptionUtil.sha256Encry(newPwd, salt);
            loginPo.setPwdSalt(salt);
            loginPo.setPwd(pwdNew);
            loginAipService.updateLoginInfo(loginPo);
            result.addResult("success", true);
            result.setSuccess(true);
       
        logger.info("PwdController.loginPwdUpdate返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;

    }

    @ResponseBody
    @RequestMapping("/loginPwdBack")
    @SystemControllerLog(description = "找回登陆密码")
    public ActionResult loginPwdBack(ClientInfo clientInfo, String userName, String pwd, String userRealName,
            String idCard, String vcode) throws Exception {
        ActionResult result = new ActionResult();

        if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(userRealName)
                || StringUtils.isEmpty(idCard) || StringUtils.isEmpty(vcode)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("PwdController.loginPwdBack返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 判断用户是否存在

        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userName);

        if (null == loginPo) {
            result.setCode(ErrorCodeEnum.REGISTER_NOTEXISTS.getCode());
            result.setMsg(ErrorCodeEnum.REGISTER_NOTEXISTS.getValue());
            logger.info("UserController.loginPwdBack返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 判断验证码是否正确
        SmsInfoVo data = SmsUtil.getData(userName);
        if (ValidateUtils.isEmpty(data)) {
            result.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            result.setMsg("验证码失效，请重新发送！");
            logger.info("UserController.loginPwdBack返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        if (!vcode.equals(data.getVcode())) {
            result.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            result.setMsg("验证码不正确！");
            logger.info("UserController.loginPwdBack返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }
        // 删除验证码
        SmsUtil.delToken(userName);
        // 查询用户信息

        UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());
        if (!userInfo.getUserRealName().equals(userRealName) || !userInfo.getIdCard().equals(idCard)) {
            result.setCode(ErrorCodeEnum.LOGIN_INFO_VALIDATE_ERROR.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_INFO_VALIDATE_ERROR.getValue());
            logger.info("UserController.loginPwdBack返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        String salt = SaltUtil.createSalt();
        String pwdNew = EncryptionUtil.sha256Encry(pwd, salt);
        loginPo.setPwdSalt(salt);
        loginPo.setPwd(pwdNew);
        loginAipService.updateLoginInfo(loginPo);
        result.addResult("success", true);
        result.setSuccess(true);

        logger.info("PwdController.loginPwdBack返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;

    }

    @ResponseBody
    @RequestMapping("/validate")
    @SystemControllerLog(description = "验证验证码")
    public ActionResult validate(String telNum, String vcode) {
        ActionResult ar = new ActionResult();
        if (ValidateUtils.isEmpty(telNum)) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("手机号不能为空！");
            logger.debug("手机号不能为空！");
            return ar;
        }
        if (ValidateUtils.isEmpty(vcode)) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("验证码不能为空！");
            logger.debug("验证码不能为空！");
            return ar;
        }
        // TODO 验证码是否正确
        String object = null;
        try {
            object = baseRedisService.get(SystemConstants.getKeySMS(telNum));
            if (ValidateUtils.isEmpty(object)) {
                ar.setSuccess(false);
                ar.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
                ar.setMsg("验证码失效，请重新发送！");
                logger.debug("验证码失效，请重新发送！");
                return ar;
            }
        } catch (Exception e1) {
            logger.error("SMScontroller.validate", e1);
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            ar.setMsg("验证码获取异常！");
            return ar;
        }

        if (!object.equals(vcode.trim())) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            ar.setMsg("验证码不正确！");
            logger.debug("验证码不正确！");
            return ar;
        }
        try {
            baseRedisService.delete(SystemConstants.getKeySMS(telNum));
        } catch (Exception e) {
            logger.error("SMScontroller.validate", e);
        }

        logger.debug("/sms/validate.do返回" + JsonUtil.bean2JsonStr(ar));
        ar.setSuccess(true);
        ar.setMsg("验证通过！");
        return ar;
    }

    /**
     * 
     * Discription: 生成随机数接口
     * 
     * @param clientInfo
     * @return ActionResult
     * @author Administrator
     * @since 2016年9月5日
     */
    @ResponseBody
    @RequestMapping("/genRandomNum")
    @SystemControllerLog(description = "生成随机数接口")
    public ActionResult genRandomNum(ClientInfo clientInfo) throws Exception {
        ActionResult result = new ActionResult();

        UserInfoVo user = TokenUtil.getData(clientInfo.getToken());

        UserRequestData urd = new UserRequestData();
        urd.setOrgId(user.getOrgId());
        urd.setUserId(user.getUserId());
        urd.setProductId(user.getProId());
        urd.setTerminalType(UserRequestData.TerminalType.APP);
        urd.setType(UserRequestData.Type.P);
        logger.info("PwdController.genRandomNum dubbo入参:{" + JsonUtil.bean2JsonStr(urd) + "}");
        BaseResultVo<String> generatRandomNum = passwordGuardManageService.generatRandomNum(urd);
        logger.info("PwdController.genRandomNum dubbo结果:{" + generatRandomNum + "}");
        result.setMsg(generatRandomNum.getErrorMsg());
        result.setCode(generatRandomNum.isSuccess() ? "0" : ErrorCodeEnum.SYSTEM_ERROR.getCode());
        result.setSuccess(generatRandomNum.isSuccess());
        result.addResult("key", generatRandomNum.getResultData());

        logger.info("PwdController.genRandomNum返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;
    }

    
    /*--------------企业钱包新接口---------------------*/
    @ResponseBody
    @RequestMapping("/loginPwdBackNew")
    @SystemControllerLog(description = "找回登陆密码")
    public ActionResult loginPwdBackNew(ClientInfo clientInfo, String userName, String pwd, 
            String vcode) throws Exception {
        ActionResult result = new ActionResult();

        if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(userName) 
               || StringUtils.isEmpty(vcode)) {
            result.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            result.setMsg(ErrorCodeEnum.PARM_CHECK_NULL.getValue());
            logger.info("PwdController.loginPwdBackNew返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        // 判断用户是否存在

        UserLoginPo loginPo = loginAipService.getLoginInfoByName(userName);
        
        if (null == loginPo) {
            result.setCode(ErrorCodeEnum.REGISTER_NOTEXISTS.getCode());
            result.setMsg(ErrorCodeEnum.REGISTER_NOTEXISTS.getValue());
            logger.info("PwdController.loginPwdBackNew返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }
        
       
        
        //企业用户取法人手机号
        if(loginPo.getUserType()==UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE_INTEGER){
            logger.info("PwdController.loginPwdBackNew 用户类型:企业用户");
            UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());
            userName=userInfo.getCorporateTel();
        }
       
        logger.info("PwdController.loginPwdBackNew 手机号:{" +userName + "}");
        // 判断验证码是否正确
        SmsInfoVo data = SmsUtil.getData(userName);
        if (ValidateUtils.isEmpty(data)) {
            result.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            result.setMsg("验证码失效，请重新发送！");
            logger.info("PwdController.loginPwdBackNew返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }

        if (!vcode.equals(data.getVcode())) {
            result.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            result.setMsg("验证码不正确！");
            logger.info("PwdController.loginPwdBackNew返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            return result;
        }
        // 删除验证码
        SmsUtil.delToken(userName);
        // 查询用户信息

//        UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());
//        if (!userInfo.getUserRealName().equals(userRealName)) {
//            result.setCode(ErrorCodeEnum.LOGIN_INFO_VALIDATE_ERROR.getCode());
//            result.setMsg(ErrorCodeEnum.LOGIN_INFO_VALIDATE_ERROR.getValue());
//            logger.info("UserController.loginPwdBackNew返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
//            return result;
//        }

        String salt = SaltUtil.createSalt();
        String pwdNew = EncryptionUtil.sha256Encry(pwd, salt);
        loginPo.setPwdSalt(salt);
        loginPo.setPwd(pwdNew);
        loginAipService.updateLoginInfo(loginPo);
        result.addResult("success", true);
        result.setSuccess(true);

        logger.info("PwdController.loginPwdBackNew返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
        return result;

    }
}

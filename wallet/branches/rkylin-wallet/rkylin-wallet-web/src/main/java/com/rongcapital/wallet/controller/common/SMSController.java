package com.rongcapital.wallet.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongcapital.redis.sdr.base.BaseRedisService;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.rop.SendSMS;
import com.rongcapital.wallet.util.generate.GenerateSpecialStr;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;

/**
 * 
 * Description:短信
 * 
 * @author: Administrator
 * @CreateDate: 2016年8月23日
 * @version: V1.0
 */
@Controller
@RequestMapping("/sms")
public class SMSController {

    private static Logger logger = LoggerFactory.getLogger(SMSController.class);
    @Autowired
    private BaseRedisService<String, String> baseRedisService;

    /**
     * 
     * Discription:发短信
     * 
     * @param telNum手机号
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月25日
     */
    @ResponseBody
    @RequestMapping("/sendSMS")
    @SystemControllerLog(description = "发短信")
    public ActionResult sendSMS(String telNum) throws Exception {
        // TODO 是否需要加每个手机号每天最多发多少短信的限制

        ActionResult ar = new ActionResult();
        if (ValidateUtils.isEmpty(telNum)) {

            ar.setCode(ErrorCodeEnum.PARM_CHECK_NULL.getCode());
            ar.setMsg("手机号不能为空！");
            logger.debug("手机号不能为空！");
            return ar;
        }

        String object = baseRedisService.get(SystemConstants.getKey(SystemConstants.getKey(telNum)));
        if (!ValidateUtils.isEmpty(object)) {

            ar.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            ar.setMsg("已发送验证码，请稍候重试！");
            logger.info("已发送验证码，请稍候重试！");
            return ar;
        }

        String vcode = GenerateSpecialStr.createRandom(true, 6);// 验证码
        if (!SendSMS.sendSMS(telNum, "融数钱包验证码" + vcode + "(请勿向任何人提供您收到的短信验证码)")) {
            ar.setSuccess(false);
            ar.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
            ar.setMsg("发送短信失败请重试！");
            logger.info("发送短信失败请重试！");

            return ar;
        } else {
            try {
                baseRedisService.set(SystemConstants.getKey(telNum), vcode, 60);
            } catch (Exception e) {
                logger.error("SMScontroller.sendSMS",e);
                ar.setSuccess(false);
                ar.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
                ar.setMsg("发送短信失败请重试！");

                return ar;
            }

        }
        logger.info("/sms/sendSMS.do返回" + JsonUtil.bean2JsonStr(ar));
        ar.setSuccess(true);
        ar.setMsg("发送成功！");
        return ar;
    }

    /**
     * 
     * Discription:验证验证码
     * 
     * @param telNum手机号
     * @param vcode验证码
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月25日
     */
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
            object = baseRedisService.get(SystemConstants.getKey(telNum));
            if (ValidateUtils.isEmpty(object)) {
                ar.setSuccess(false);
                ar.setCode(ErrorCodeEnum.YZMSX_ERROR.getCode());
                ar.setMsg("验证码失效，请重新发送！");
                logger.debug("验证码失效，请重新发送！");
                return ar;
            }
        } catch (Exception e1) {
            logger.error("SMScontroller.validate",e1);
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
            baseRedisService.delete(SystemConstants.getKey(telNum));
        } catch (Exception e) {
            logger.error("SMScontroller.validate",e);
        }

        logger.debug("/sms/validate.do返回" + JsonUtil.bean2JsonStr(ar));
        ar.setSuccess(true);
        ar.setMsg("验证通过！");
        return ar;
    }

}
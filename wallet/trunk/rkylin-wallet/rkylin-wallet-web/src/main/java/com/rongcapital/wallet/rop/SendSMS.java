package com.rongcapital.wallet.rop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.RopResponse;
import com.Rop.api.request.ExternalSessionGetRequest;
import com.Rop.api.request.SmsNoteSendRequest;
import com.Rop.api.response.ExternalSessionGetResponse;
import com.google.common.collect.Maps;
import com.rongcapital.wallet.constants.RopConstants;
import com.rongcapital.wallet.constants.SMSConstants;
import com.rongcapital.wallet.util.generate.GenerateSpecialStr;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;

@Component
public class SendSMS {
    private static Logger logger = LoggerFactory.getLogger(SendSMS.class);
    // private static DefaultRopClient ropClient =
    // new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY, RopConstants.APP_SECRET_Z, "xml");

    protected static Properties smsTempletProperties;

    @Resource(name = "smsTempletProperties")
    public void setSmsTempletProperties(Properties smsTempletProperties) {
        SendSMS.smsTempletProperties = smsTempletProperties;
    }

    public static boolean sendSMS(String mobile, String code, Map<String, String> map) {
        String content = null;
        if (ValidateUtils.isEmpty(code)) {
            code = SMSConstants.SMS_TEMPLET;
        }
        String property = smsTempletProperties.getProperty(code);
        if (!ValidateUtils.isEmpty(property)) {
            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
            content = property;
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                content = content.replace(entry.getKey(), entry.getValue());
            }
            return sendSMS(mobile, content, code);
        }
        return false;
    }

    public static boolean sendSMS(String mobile, String content, String type) {

        String appKey = SMSConstants.map.get(type).split(";")[0];// "53CBC4B8-F6AF-474C-8279-1B5C9A1A6401";
        String appSecret = SMSConstants.map.get(type).split(";")[1]; // "A090C591-9BF8-4652-8E97-5A75DC2C9E38";
        return sendSMS(mobile, content, appKey, appSecret);
    }

    public static boolean sendSMS(String mobile, String content, String appKey, String appSecret) {
        String jsonOrXml = SMSConstants.SMS_JSONORXML;
        String ropUrl = RopConstants.ROP_URL_DX;// "https://api.open.ruixuesoft.com:30005/ropapi";
        DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey, appSecret, jsonOrXml);
        // ruixue.sms.note.send
        SmsNoteSendRequest smsRequest = new SmsNoteSendRequest();
        smsRequest.setChannel_code(SMSConstants.SMS_CHANNEL_CODE);
        smsRequest.setMobile(mobile);
        smsRequest.setContent(content);
        smsRequest.setAppend_code(SMSConstants.SMS_APPEND_CODE);
        RopResponse rsp = null;
        try {
            logger.info("SendSMS.sendSMS-rop入参:" + JsonUtil.bean2JsonStr(smsRequest));
            rsp = ropClient.execute(smsRequest, sessionGet(ropUrl, appKey, appSecret));
            logger.info("SendSMS.sendSMS-rop返回" + JsonUtil.bean2JsonStr(rsp));
            // System.out.println(content);
            // System.out.println(rsp.isSuccess());
            logger.info("发送短信给[" + mobile + "],内容为:" + content);
        } catch (ApiException e1) {

        }
        if (null != rsp) {
            return rsp.isSuccess();
        } else {
            return false;
        }
    }

    public static String sessionGet(String ropUrl, String appKey, String appSecret) {
        String sessionKey = null;
        DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey, appSecret);
        try {
            ExternalSessionGetRequest sessionGetReq = new ExternalSessionGetRequest();

            ExternalSessionGetResponse sessionGetRsp = ropClient.execute(sessionGetReq);
            sessionKey = sessionGetRsp.getSession();
        } catch (Exception ex) {
            // System.out.println(ex.getMessage());
            logger.error("SendSMS.sessionGet异常:", ex);
        } finally {

        }
        return sessionKey;
    }

    public static void main(String[] args) {
        String vcode = GenerateSpecialStr.createRandom(true, 6);// 验证码
        HashMap<String, String> map = Maps.newHashMap();
        map.put("telNum", "13131693489");
        map.put("download", vcode);
        String tem = "账号激活验证码" + vcode + "。";
        String temp = "您的资料已审核通过，打开融数企业钱包APP即可激活，激活账号：13131693489。如未安装，请下载：https://qyqianbao.com。";
        boolean sms = sendSMS("13131693489", tem, SMSConstants.SMS_TEMPLET_DOWNLOAD);
        // boolean sms = sendSMS("13131693489", SMSConstants.SMS_TEMPLET_DOWNLOAD, map);
        System.out.println(sms);
    }
}

package com.rongcapital.wallet.rop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.RopResponse;
import com.Rop.api.request.ExternalSessionGetRequest;
import com.Rop.api.request.SmsNoteSendRequest;
import com.Rop.api.response.ExternalSessionGetResponse;
import com.rongcapital.wallet.constants.RopConstants;
import com.rongcapital.wallet.util.generate.GenerateSpecialStr;
import com.rongcapital.wallet.util.json.JsonUtil;

public class SendSMS {
    private static Logger logger = LoggerFactory.getLogger(SendSMS.class);
    // private static DefaultRopClient ropClient =
    // new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY, RopConstants.APP_SECRET_Z, "xml");

    public static boolean sendSMS(String mobile, String content) {
        String jsonOrXml = "xml";
        String appKey = RopConstants.APP_KEY_DX; // "53CBC4B8-F6AF-474C-8279-1B5C9A1A6401";
        String appSecret = RopConstants.APP_SECRET_DX; // "A090C591-9BF8-4652-8E97-5A75DC2C9E38";
        String ropUrl = RopConstants.ROP_URL_DX;// "https://api.open.ruixuesoft.com:30005/ropapi";
        DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey, appSecret, jsonOrXml);
        // ruixue.sms.note.send
        SmsNoteSendRequest smsRequest = new SmsNoteSendRequest();
        smsRequest.setChannel_code("BF175285-4821-4C41-9462-6EC00F84E57B");
        smsRequest.setMobile(mobile);
        smsRequest.setContent(content);
        smsRequest.setAppend_code("0007");
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
        return rsp.isSuccess();
    }

    public static String sessionGet(String ropUrl, String appKey, String appSecret) {
        String sessionKey = null;
        DefaultRopClient ropClient = new DefaultRopClient(ropUrl, appKey, appSecret);
        try {
            ExternalSessionGetRequest sessionGetReq = new ExternalSessionGetRequest();

            ExternalSessionGetResponse sessionGetRsp = ropClient.execute(sessionGetReq);
            sessionKey = sessionGetRsp.getSession();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {

        }
        return sessionKey;
    }

    public static void main(String[] args) {
        String vcode = GenerateSpecialStr.createRandom(true, 6);// 验证码
        boolean sms = sendSMS("13131693489", "融数钱包验证码" + vcode + "(请勿向任何人提供您收到的短信验证码)");
        System.out.println(sms);
    }
}

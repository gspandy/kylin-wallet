//package com.test.rop;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.Rop.api.DefaultRopClient;
//import com.Rop.api.request.WheatfieldUsercenterPwdLoginpwdbacknewRequest;
//import com.Rop.api.request.WheatfieldUsercenterPwdLoginpwdupdateRequest;
//import com.Rop.api.request.WheatfieldUsercenterPwdLoginpwdvalidateRequest;
//import com.Rop.api.request.WheatfieldUsercenterUserLoginnewRequest;
//import com.Rop.api.response.WheatfieldUsercenterPwdLoginpwdbacknewResponse;
//import com.Rop.api.response.WheatfieldUsercenterPwdLoginpwdupdateResponse;
//import com.Rop.api.response.WheatfieldUsercenterPwdLoginpwdvalidateResponse;
//import com.Rop.api.response.WheatfieldUsercenterUserLoginnewResponse;
//import com.rongcapital.wallet.constants.RopConstants;
//import com.rongcapital.wallet.rop.AccountRop;
//import com.rongcapital.wallet.util.ROPClientUtils;
//import com.rongcapital.wallet.util.json.JsonUtil;
//
///**
// * Created by Administrator on 2016-11-14.
// */
//public class RopUserCenterTest {
//
//    private static Logger logger = LoggerFactory.getLogger(AccountRop.class);
//    private static DefaultRopClient ropClient = new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY,
//            RopConstants.APP_SECRET, RopConstants.RESULT_TYPE_JSON);
//
//    /**
//     * 登陆 Discription:
//     * 
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String userloginnew() throws Exception {
//        WheatfieldUsercenterUserLoginnewRequest request = new WheatfieldUsercenterUserLoginnewRequest();
//
//        request.setClient("andriod");
//        request.setUsername("13681396045");
//        request.setPwd("654321");
//
//        logger.info("MyRop.userLoginNew-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldUsercenterUserLoginnewResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.userLoginNew-rop返回:" + JsonUtil.bean2JsonStr(response));
//
//        String body = response.getBody();
//        return body;
//    }
//    
//    /**
//     * 找回登陆密码
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String loginpwdbacknew() throws Exception {
//        WheatfieldUsercenterPwdLoginpwdbacknewRequest request = new WheatfieldUsercenterPwdLoginpwdbacknewRequest();
//         
//        request.setPwd("11111");
//        request.setUserame("13681396045");
//        request.setVcode("2222");
//        logger.info("MyRop.loginpwdbacknew-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldUsercenterPwdLoginpwdbacknewResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.loginpwdbacknew-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    
//    /**
//     * 修改登陆密码
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String loginpwdupdate() throws Exception {
//        WheatfieldUsercenterPwdLoginpwdupdateRequest request = new WheatfieldUsercenterPwdLoginpwdupdateRequest();
//       
//        request.setPwd("123456");
//        request.setNewpwd("654321");
//        request.setToken("");
//        request.setUserid("");
//      
//        logger.info("MyRop.loginpwdupdate-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldUsercenterPwdLoginpwdupdateResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.loginpwdupdate-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    /**
//     * 验证登陆密码
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String loginpwdvalidate() throws Exception {
//        WheatfieldUsercenterPwdLoginpwdvalidateRequest request = new WheatfieldUsercenterPwdLoginpwdvalidateRequest();
//       
//        request.setPwd("123456");
//        request.setToken("");
//        request.setUserid("");
//
//        logger.info("MyRop.loginpwdvalidate-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldUsercenterPwdLoginpwdvalidateResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.loginpwdvalidate-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    
//    public static void main(String[] args) throws Exception {
//        System.out.println(userloginnew());
//    }
//}

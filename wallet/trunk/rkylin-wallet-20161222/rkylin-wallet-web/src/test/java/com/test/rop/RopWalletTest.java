//package com.test.rop;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.Rop.api.DefaultRopClient;
//import com.Rop.api.request.WheatfieldWalletBankBankdetailRequest;
//import com.Rop.api.request.WheatfieldWalletBankBanklistRequest;
//import com.Rop.api.request.WheatfieldWalletBankBindcardRequest;
//import com.Rop.api.request.WheatfieldWalletBankDelcardRequest;
//import com.Rop.api.request.WheatfieldWalletBankValidatebankRequest;
//import com.Rop.api.request.WheatfieldWalletSmsSendsmsRequest;
//import com.Rop.api.request.WheatfieldWalletTransferGetbalanceRequest;
//import com.Rop.api.request.WheatfieldWalletTransferGetcreditbalanceRequest;
//import com.Rop.api.request.WheatfieldWalletUserIdentityauthRequest;
//import com.Rop.api.response.WheatfieldWalletBankBankdetailResponse;
//import com.Rop.api.response.WheatfieldWalletBankBanklistResponse;
//import com.Rop.api.response.WheatfieldWalletBankBindcardResponse;
//import com.Rop.api.response.WheatfieldWalletBankDelcardResponse;
//import com.Rop.api.response.WheatfieldWalletBankValidatebankResponse;
//import com.Rop.api.response.WheatfieldWalletSmsSendsmsResponse;
//import com.Rop.api.response.WheatfieldWalletTransferGetbalanceResponse;
//import com.Rop.api.response.WheatfieldWalletTransferGetcreditbalanceResponse;
//import com.Rop.api.response.WheatfieldWalletUserIdentityauthResponse;
//import com.rongcapital.wallet.constants.RopConstants;
//import com.rongcapital.wallet.rop.AccountRop;
//import com.rongcapital.wallet.util.ROPClientUtils;
//import com.rongcapital.wallet.util.json.JsonUtil;
//
//public class RopWalletTest {
//
//    private static Logger logger = LoggerFactory.getLogger(AccountRop.class);
//    private static DefaultRopClient ropClient = new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY,
//            RopConstants.APP_SECRET, RopConstants.RESULT_TYPE_JSON);
//
//    /**
//     * 发短信
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String sendsms() throws Exception {
//        WheatfieldWalletSmsSendsmsRequest request = new WheatfieldWalletSmsSendsmsRequest();
//        request.setCode("");
//        request.setTelnum("13681396045");
//        logger.info("MyRop.sendsms-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletSmsSendsmsResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.sendsms-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    
//    /**
//     * 实名认证
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String identityauth() throws Exception {
//        WheatfieldWalletUserIdentityauthRequest request = new WheatfieldWalletUserIdentityauthRequest();
//        request.setIdcard("");
//        request.setUserrealname("");
//        logger.info("MyRop.identityauth-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletUserIdentityauthResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.identityauth-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//   
//    /**
//     * 查询用户绑定银行卡列表
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String banklist() throws Exception {
//        WheatfieldWalletBankBanklistRequest request = new WheatfieldWalletBankBanklistRequest();
//        request.setToken("");
//        request.setUserid("");
//        logger.info("MyRop.banklist-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletBankBanklistResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.banklist-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//  
//    
//    /**
//     * 银行卡明细
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String bankdetail() throws Exception {
//        WheatfieldWalletBankBankdetailRequest request = new WheatfieldWalletBankBankdetailRequest();
//        request.setToken("");
//        request.setUserid("");
//        request.setBankcardnum("");
//        logger.info("MyRop.bankdetail-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletBankBankdetailResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.bankdetail-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    /**
//     * 四要素
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String validatebank() throws Exception {
//        WheatfieldWalletBankValidatebankRequest request = new WheatfieldWalletBankValidatebankRequest();
//        request.setToken("");
//        request.setUserid("");
//        request.setBankcardnum("");
//        logger.info("MyRop.validatebank-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletBankValidatebankResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.validatebank-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    /**
//     * 绑卡
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String bindcard() throws Exception {
//        WheatfieldWalletBankBindcardRequest request = new WheatfieldWalletBankBindcardRequest();
//        request.setToken("");
//        request.setUserid("");
//        request.setBankcardnum("");
//        logger.info("MyRop.bindcard-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletBankBindcardResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.bindcard-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    /**
//     * 解卡
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String delcard() throws Exception {
//        WheatfieldWalletBankDelcardRequest request = new WheatfieldWalletBankDelcardRequest();
//        request.setToken("");
//        request.setUserid("");
//        request.setBankcardnum("");
//        logger.info("MyRop.delcard-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletBankDelcardResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.delcard-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    /**
//     * 查询账户余额
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String getbalance() throws Exception {
//        WheatfieldWalletTransferGetbalanceRequest request = new WheatfieldWalletTransferGetbalanceRequest();
//        request.setToken("");
//        request.setUserid("");    
//        logger.info("MyRop.getbalance-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletTransferGetbalanceResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.getbalance-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//    
//    /**
//     * 信用可用余额
//     * Discription:
//     * @return
//     * @throws Exception String
//     * @author libi
//     * @since 2016年11月16日
//     */
//    public static String getcreditbalance() throws Exception {
//        WheatfieldWalletTransferGetcreditbalanceRequest request = new WheatfieldWalletTransferGetcreditbalanceRequest();
//        request.setToken("");
//        request.setUserid("");    
//        logger.info("MyRop.getbalance-rop入参:" + JsonUtil.bean2JsonStr(request));
//        WheatfieldWalletTransferGetcreditbalanceResponse response = ROPClientUtils.execute(ropClient, request);
//        logger.info("MyRop.getbalance-rop返回:" + JsonUtil.bean2JsonStr(response));
//        String body = response.getBody();
//        return body;
//    }
//  
//}

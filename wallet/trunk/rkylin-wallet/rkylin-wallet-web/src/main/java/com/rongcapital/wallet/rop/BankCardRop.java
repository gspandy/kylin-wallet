package com.rongcapital.wallet.rop;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.domain.Accountinfo;
import com.Rop.api.request.WheatfieldAccountinfoDeleteRequest;
import com.Rop.api.request.WheatfieldAccountinfoQueryRequest;
import com.Rop.api.request.WheatfieldBankaccountBindingNocheckRequest;
import com.Rop.api.request.WheatfieldBankaccountInfoupdateRequest;
import com.Rop.api.request.WheatfieldBanknQueryRequest;
import com.Rop.api.response.WheatfieldAccountinfoDeleteResponse;
import com.Rop.api.response.WheatfieldAccountinfoQueryResponse;
import com.Rop.api.response.WheatfieldBankaccountBindingNocheckResponse;
import com.Rop.api.response.WheatfieldBankaccountInfoupdateResponse;
import com.Rop.api.response.WheatfieldBanknQueryResponse;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.RopConstants;
import com.rongcapital.wallet.constants.UserInfoConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.util.ROPClientUtils;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.CardInfoVo;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class BankCardRop {

    private static DefaultRopClient ropClient =
            new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY, RopConstants.APP_SECRET, "json");

    private static Logger logger = LoggerFactory.getLogger(AccountRop.class);

    /**
     * 绑卡 Discription:
     * 
     * @param user
     * @param cardNo
     * @param userRealName
     * @param tel
     * @return
     * @throws ApiException
     * @throws ParseException boolean
     * @author libi
     * @since 2016年8月24日
     */
    public static ResultVo cardBind(UserInfoVo user, CardInfoVo card) throws Exception {

        ResultVo vo = new ResultVo();
        WheatfieldBankaccountBindingNocheckRequest request = new WheatfieldBankaccountBindingNocheckRequest();

        request.setUserid(user.getUserId()); // 用户ID
        request.setUsertype(user.getUserType()); // 用户类型(1：商户，2：普通用户)
        request.setConstid(user.getOrgId()); // 机构码
        request.setProductid(user.getProId()); // 产品号
        
        
        request.setCertificatetype(user.getIdCardType()); // 开户证件类型0：身份证,1: 户口簿，2：护照
        request.setCertificatenumnumber(user.getIdCard()); // 证件号

        
        request.setAccountnumber(card.getBankCardNum()); // 账号
        request.setAccounttypeid(card.getCardType());
        request.setBankheadname(card.getBankName()); // 开户行总行名称
        request.setBank_code(card.getBankCode()); // 银行代码总行
        request.setCurrency(card.getCurrency()); // 币种（CNY）
        request.setAccountpurpose(card.getPurpose()); // 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
        request.setAccountproperty(card.getAccountProperty()); // 账户属性（1：对公，2：对私）

        request.setAccount_name(card.getUserName()); // 账号名 银行卡或存折上的所有人姓名
        
       
        request.setBankbranchname(card.getBankbranchname());  //支行名称
        request.setBank_branch(card.getBankBranch());  //支行code
        request.setBank_province(card.getBankProvince());  //开户行的在省
        request.setBank_city(card.getBankCity()); //开户行所在市
        

        logger.info("BankCardRop.cardBind-rop入参" + JsonUtil.bean2JsonStr(request));
        WheatfieldBankaccountBindingNocheckResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("BankCardRop.cardBind-rop返回" + JsonUtil.bean2JsonStr(response));

        if (response.isSuccess()) {
            vo.setSuccess(true);
        } else {
            vo.setMsg(response.getMsg());
        }
        return vo;
    }

    /**
     * 解绑银行卡 Discription:
     * 
     * @param user
     * @param card
     * @return
     * @throws Exception ResultVo
     * @author libi
     * @since 2016年9月9日
     */
    public static ResultVo cardDel(UserInfoVo user, String bankCardNum) throws Exception {

        ResultVo vo = new ResultVo();

        WheatfieldAccountinfoDeleteRequest request = new WheatfieldAccountinfoDeleteRequest();

        request.setUserid(user.getUserId()); // 用户ID
        request.setUsertype(user.getUserType()); // 用户类型(1：商户，2：普通用户)
        request.setConstid(user.getOrgId()); // 机构码
        request.setProductid(user.getProId()); // 产品号
        request.setAccountnumber(bankCardNum);

        logger.info("BankCardRop.cardDel-rop入参" + JsonUtil.bean2JsonStr(request));
        WheatfieldAccountinfoDeleteResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("BankCardRop.cardDel-rop返回" + JsonUtil.bean2JsonStr(response));

        if (response.isSuccess()) {
            vo.setSuccess(true);
        } else {
            vo.setMsg(response.getMsg());
        }
        return vo;
    }

    public static void main(String[] args) throws Exception {

        /*---------查询银行卡----------*/
        // UserInfoVo user = new UserInfoVo();
        // user.setOrgId(UserPresetConstants.ORG_ID); // M666666
        // String cardNo = "6227000010110225556";
        // try {
        // BankCardRop.cardDetail(user, cardNo);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        /*--------------绑卡------------*/
        // UserInfoVo user1 = new UserInfoVo();
        // user1.setOrgId(UserPresetConstants.ORG_ID);
        // user1.setProId(UserPresetConstants.PRO_ID);
        // user1.setUserId("201609031423340001"); // 用户ID
        // user1.setUserType(UserInfoConstants.ACCOUNT_PROPERTY_PERSONAGE); // 用户类型(1：商户，2：普通用户)
        // user1.setIdCardType(CardConstants.CERTIFICATE_TYPE_IDENTITY); //// 开户证件类型0：身份证,1: 户口簿，
        // user1.setIdCard("430626198409216759"); //// 证件号
        //
        // CardInfoVo card = new CardInfoVo();
        // card.setBankCardNum("6227000010110225556"); // 账号
        // card.setCardType(CardConstants.CARD_TYPE_BANK); // 卡类型
        // card.setBankName("中国建设银行"); // 开户行总行名称
        // card.setBankCode("105"); //// 银行代码总行
        // card.setCurrency(CardConstants.CARD_CURRENCY_CNY);// 币种（CNY）
        // card.setPurpose(CardConstants.CARD_PURPOSE_OTHER); //// 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
        // card.setAccountProperty(CardConstants.CARD_PROPERTY_PRIVATE);// 账户属性（1：对公，2：对私）
        // card.setUserName("李碧"); // 账号名 银行卡或存折上的所有人姓名
        //
        // try {
        // ResultVo vo = BankCardRop.cardBind(user1, card);
        // System.out.println(vo);
        // } catch (Exception e) {
        //
        // }

        /*-------------------解绑----------------*/
        UserInfoVo user1 = new UserInfoVo();
        user1.setOrgId(UserPresetConstants.ORG_ID);
        user1.setProId(UserPresetConstants.PRO_ID);
        user1.setUserId("201609031423340001"); // 用户ID
        user1.setUserType(UserInfoConstants.ACCOUNT_PROPERTY_PERSONAGE);
        ResultVo vo = BankCardRop.cardDel(user1, "6227000010110225556");
        System.out.println(vo);
    }

    /**
     * 
     * Discription:完善银行卡信息
     * 
     * @param user
     * @param card
     * @return
     * @throws ApiException
     * @throws ParseException boolean
     * @author Administrator
     * @since 2016年8月26日
     */
    public static boolean infoUpdate(UserInfoVo user, CardInfoVo card) throws ApiException, ParseException {
        WheatfieldBankaccountInfoupdateRequest request = new WheatfieldBankaccountInfoupdateRequest();

        request.setUserid(user.getUserId()); // 用户ID
        request.setUsertype(user.getUserType()); // 用户类型(1：商户，2：普通用户)
        request.setConstid(user.getOrgId()); // 机构码
        request.setProductid(user.getProId()); // 产品号
        request.setAccountnumber(card.getBankCardNum()); // 账号

        request.setBank_branch(card.getBankCode()); // 开户行支行号
        request.setBankbranchname(card.getBankbranchname()); // 开户行支行名称 可选
        request.setBank_province(card.getBankProvince()); // 开户行所在省
        request.setBank_city(card.getBankCity()); // 开户行所在市
        logger.info("BankCardRop.infoUpdate-rop入参" + JsonUtil.bean2JsonStr(request));
        WheatfieldBankaccountInfoupdateResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("BankCardRop.infoUpdate-rop返回" + JsonUtil.bean2JsonStr(response));
        return response.isSuccess();
    }

    /**
     * 
     * Discription:查询银行卡信息
     * 
     * @param user
     * @return
     * @throws ApiException List<Accountinfo>
     * @author Administrator
     * @since 2016年8月26日
     */
    public static List<Accountinfo> accountinfoQuery(UserInfoVo user) throws ApiException {
        WheatfieldAccountinfoQueryRequest request = new WheatfieldAccountinfoQueryRequest();
        request.setUserid(user.getUserId()); // 用户ID
        request.setUsertype(user.getUserType()); // 用户类型(1：商户，2：普通用户)
        request.setConstid(user.getOrgId()); // 机构码
        request.setProductid(user.getProId()); // 产品号
        request.setObjorlist("2");
        logger.info("BankCardRop.accountinfoQuery-rop入参" + JsonUtil.bean2JsonStr(request));
        WheatfieldAccountinfoQueryResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("BankCardRop.accountinfoQuery-rop返回" + JsonUtil.bean2JsonStr(response));
        return response.getAccountinfos();
    }

    /**
     * 
     * Discription:查询银行信息-新
     * 
     * @param bankcode银行code： 如三个入参都为空，将会查询所有总行信息 如该入参不为空，将会根据银行code获取所有支行信息
     * @param citycode城市code：如入参全部为空，将会查询所有总行信息 如该入参不为空并且bankcode入参不为空，将会查询该城市下的所有bankcode的支行信息
     *            如该入参不为空bankcode为空，将会获取该城市下所有支行信息
     * @return
     * @throws ApiException List<com.Rop.api.domain.BankInfo>
     * @author Administrator
     * @since 2016年8月29日
     */
    public static List<com.Rop.api.domain.BankInfo> banknQuery(String bankcode, String citycode) throws ApiException {
        WheatfieldBanknQueryRequest request = new WheatfieldBanknQueryRequest();
        request.setBankcode(bankcode); // 银行code： 如三个入参都为空，将会查询所有总行信息 如该入参不为空，将会根据银行code获取所有支行信息 注：(前提
                                       // 该入参不为空)status填写的状态值将不会进行条件查询
        request.setCitycode(citycode);// 城市code：如入参全部为空，将会查询所有总行信息 如该入参不为空并且bankcode入参不为空，将会查询该城市下的所有bankcode的支行信息
                                      // 如该入参不为空bankcode为空，将会获取该城市下所有支行信息
        // request.setStatus(null); // 机构码
        logger.info("BankCardRop.banknQuery-rop入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldBanknQueryResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("BankCardRop.banknQuery-rop返回" + JsonUtil.bean2JsonStr(response));
        return response.getBankinfos();
    }

    public static Accountinfo checkBankNo(UserInfoVo uipo, String bankNo) {
        List<Accountinfo> cardDetail = null;
        try {
            cardDetail = BankCardRop.accountinfoQuery(uipo);
        } catch (ApiException e) {
            logger.error("查询银行卡异常！", e);
        }
        Accountinfo ainfo = null;
        for (Accountinfo accountinfo : cardDetail) {
            if (bankNo.equals(accountinfo.getAccount_number())) {
                ainfo = accountinfo;
                break;
            }
        }
        return ainfo;
    }

}

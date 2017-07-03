package com.rongcapital.wallet.rop;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.WheatfieldCheckstandCardbinQueryRequest;
import com.Rop.api.request.WheatfieldPycreditIdentityAuthPhotoRequest;
import com.Rop.api.request.WheatfieldPycreditPersonBankChecksRequest;
import com.Rop.api.response.WheatfieldCheckstandCardbinQueryResponse;
import com.Rop.api.response.WheatfieldPycreditIdentityAuthPhotoResponse;
import com.Rop.api.response.WheatfieldPycreditPersonBankChecksResponse;
import com.rkylin.gateway.dto.identityAuthPhoto.IdentityAuthPhotoCisReport;
import com.rkylin.gateway.dto.identityAuthPhoto.IdentityAuthPhotoRespDto;
import com.rkylin.gateway.dto.personBankChecks.PersonBankChecksInfo;
import com.rkylin.gateway.dto.personBankChecks.PersonBankChecksInfoCisReport;
import com.rkylin.gateway.dto.personBankChecks.PersonBankChecksRespDto;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.RopConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.util.ROPClientUtils;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.xml.XmlUtil;
import com.rongcapital.wallet.vo.BankInfo;
import com.rongcapital.wallet.vo.CardInfoVo;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class UserRop {

    private static Logger logger = LoggerFactory.getLogger(AccountRop.class);
    private static DefaultRopClient ropClient = new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY,
            RopConstants.APP_SECRET, RopConstants.RESULT_TYPE_JSON);

    /**
     * 个人银行账户核查（四元素） Discrip Discription:
     * 
     * @param user
     * @param cardInfo
     * @return
     * @throws Exception ResultVo
     * @author libi
     * @since 2016年8月30日
     */
    public static ResultVo cardValidate(UserInfoVo user, CardInfoVo cardInfo) throws Exception {

        ResultVo vo = new ResultVo();
       
        WheatfieldPycreditPersonBankChecksRequest request = new WheatfieldPycreditPersonBankChecksRequest();

        request.setOrg_no(user.getOrgId()); // 机构码
        request.setQuery_name(cardInfo.getUserName()); // 被查者姓名
        request.setId_type(CardConstants.CARD_TYPE_CERTIFICATE_VALIDATE); // 证件类型（目前只支持身份证：0）
        request.setId_code(cardInfo.getIdCard()); // 证件号码
        request.setAccount_no(cardInfo.getBankCardNum()); // 银行账户号
        request.setMobile(cardInfo.getTel()); // 银行预留手机号
        // request.setReal_time(1);
        request.setBank_no(cardInfo.getBankCode());
        logger.debug("UserRop.cardValidate-rop入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldPycreditPersonBankChecksResponse response = ROPClientUtils.execute(ropClient, request);
        logger.debug("UserRop.cardValidate-rop返回:" + JsonUtil.bean2JsonStr(response));
        String body = response.getBody();
        Map<String, Object> map = JsonUtil.jsonStr2Map(body);

        Map<String, Object> map1 = (Map<String, Object>) map.get("wheatfield_pycredit_person_bank_checks_response");

        String retcode = null != map1.get("retcode") ? (String) map1.get("retcode") : null;
        String pycode = null != map1.get("pycode") ? (String) map1.get("pycode") : null;

        if (StringUtils.isNotEmpty(retcode) && StringUtils.isNotEmpty(pycode)) {
            if ("R100000".equals(retcode) && "1".equals(pycode)) {
                if (null != map1.get("content")) {
                    PersonBankChecksRespDto po =
                            XmlUtil.parseRsp(map1.get("content").toString(), PersonBankChecksRespDto.class);
                    PersonBankChecksInfoCisReport report = po.getCisReport();
                    PersonBankChecksInfo info = report.getPersonBankCheckInfo();
                    if ("1".equals(info.getTreatResult())) {
                        if ("1".equals(info.getItem().getStatus())) {
                            vo.setSuccess(true);
                            return vo;
                        } else if ("2".equals(info.getItem().getStatus())) {
                            vo.setMsg("信息不一致");
                        } else {
                            vo.setMsg(info.getItem().getMessage());
                        }
                    }

                }
            }
        } else {
            vo.setCode(ErrorCodeEnum.BANK_CHECK.getCode());
            vo.setMsg(ErrorCodeEnum.BANK_CHECK.getValue());
        }
        return vo;
    }

    /**
     * 查询银行卡信息 Discription:
     * 
     * @param user
     * @param cardNo
     * @return
     * @throws ApiException
     * @throws ParseException boolean
     * @author libi
     * @since 2016年8月24日
     */
    public static BankInfo cardDetail(UserInfoVo user, String cardNo) throws Exception {
        WheatfieldCheckstandCardbinQueryRequest request = new WheatfieldCheckstandCardbinQueryRequest();

        request.setConstid(user.getOrgId()); // 机构ID
        request.setCardno(cardNo); // 卡号

        logger.info("UserRop.cardDetail-rop入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldCheckstandCardbinQueryResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("UserRop.cardDetail-rop返回:" + JsonUtil.bean2JsonStr(response));

        String body = response.getBody();
        Map<String, Object> map = JsonUtil.jsonStr2Map(body);
        Map<String, Object> map1 = (Map<String, Object>) map.get("wheatfield_checkstand_cardbin_query_response");
        if (null != map1) {
            if (null != map1.get("is_success") && (boolean) map1.get("is_success")) {

                return new BankInfo(cardNo, (String) map1.get("bankcode"), (String) map1.get("bankname"),
                        (String) map1.get("cardtype"));
            }
        }

        return null;
    }

    /**
     *
     * Discription:
     * 
     * @param user
     * @return
     * @throws ApiException
     * @throws ParseException ResultVo
     * @author libi
     * @since 2016年8月30日
     */
    public static ResultVo identityAuth(UserInfoVo user) throws Exception {

        /*
         * org_no 必须 varchar 机构码 query_name 必须 varchar 被查询者姓名 id_type 必须 int 证件类型（目前只支持身份证：0） id_code 必须 varchar 证件号码
         * real_time 可选 int 是否实时（0非实时，1实时） summary 可选 varchar 摘要 remark 可选 varchar 备注
         */
      
        ResultVo vo = new ResultVo();
        WheatfieldPycreditIdentityAuthPhotoRequest request = new WheatfieldPycreditIdentityAuthPhotoRequest();
        request.setOrg_no(user.getOrgId()); // 机构ID
        request.setQuery_name(user.getUserRealName());
        request.setId_type(Integer.parseInt(user.getIdCardType()));
        request.setId_code(user.getIdCard());
        request.setReal_time(1);
        
        logger.info("UserRop.identityAuth-rop入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldPycreditIdentityAuthPhotoResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("UserRop.identityAuth-rop返回:" + JsonUtil.bean2JsonStr(response));
        String body = response.getBody();

        Map<String, Object> map = JsonUtil.jsonStr2Map(body);

        Map<String, Object> map1 = (Map<String, Object>) map.get("wheatfield_pycredit_identity_auth_photo_response");

        String code = null != map1.get("pycode") ? (String) map1.get("pycode") : null;
        String retcode = null != map1.get("retcode") ? (String) map1.get("retcode") : null;

        if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(retcode)) {
            if ("R100000".equals(retcode) && "1".equals(code)) {

                if (null != map1.get("content")) {

                    IdentityAuthPhotoRespDto po =
                            XmlUtil.parseRsp(map1.get("content").toString(), IdentityAuthPhotoRespDto.class);
                    IdentityAuthPhotoCisReport report = po.getCisReport();
                    String result = report.getPoliceCheckInfo().getItem().getResult(); // 认证结果ID,1:姓名和公民身份号码一致。2：公民身份号码一致，姓名不一致。3：库中无此号，请到户籍所在地进行核实。
                    if (result.equals("1")) {
                        vo.setSuccess(true);
                        return vo;
                    }
                }
            }
        }
        vo.setCode(ErrorCodeEnum.IDENTITYAUTH.getCode());
        vo.setMsg(ErrorCodeEnum.IDENTITYAUTH.getValue());
        return vo;
    }

    public static void main(String[] args) {

        /*-----------------*/
        UserInfoVo u = new UserInfoVo();
        u.setOrgId(UserPresetConstants.ORG_ID);
        u.setUserRealName("王成伟");
        u.setIdCardType(String.valueOf(CardConstants.CARD_TYPE_CERTIFICATE_VALIDATE));
        u.setIdCard("150428198604031519");

        try {
            ResultVo vo = UserRop.identityAuth(u);
            System.out.println(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 6227000010110225556
        /*---------个人银行账户核查-----------*/
//        UserInfoVo u = new UserInfoVo();
//        u.setOrgId(UserPresetConstants.ORG_ID);
//        u.setProId(UserPresetConstants.PRO_ID);
//        
//        
//        CardInfoVo cardInfo = new CardInfoVo();
        
       
//        cardInfo.setBankCardNum("6217000010018526676"); // 卡号
//        cardInfo.setIdCardType(CardConstants.CARD_TYPE_CERTIFICATE_VALIDATE);
//        cardInfo.setIdCard("130229198803271816");
//        cardInfo.setUserName("崔超");
//        cardInfo.setTel("18311218155");

        
//        "idCard":"500234199011082851","bankCardNum":"6214830125012681","bankCity":"","tel":"13552883791","bankName":"招商银行","purpose":"","cardType":"","bankProvince":"","bankCode":"308",
//        
//        cardInfo.setBankCardNum("6214830125012681"); // 卡号
//        cardInfo.setIdCardType(CardConstants.CARD_TYPE_CERTIFICATE_VALIDATE);
//        cardInfo.setIdCard("500234199011082851");
//        cardInfo.setUserName("杨芳");
//        cardInfo.setTel("13552883791");

//        try {
//           // ResultVo vo = UserRop.cardValidate(u, cardInfo);
//            BankInfo b= UserRop.cardDetail(u, "6214830160931241");
//            System.out.println(b.toString());
//        } catch (Exception e) {
//          
//        }

    }
}

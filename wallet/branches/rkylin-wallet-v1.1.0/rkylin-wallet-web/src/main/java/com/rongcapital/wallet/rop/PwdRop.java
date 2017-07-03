package com.rongcapital.wallet.rop;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.WheatfieldPasswordCheckRequest;
import com.Rop.api.request.WheatfieldPasswordQueryRequest;
import com.Rop.api.request.WheatfieldPasswordSaveRequest;
import com.Rop.api.response.WheatfieldPasswordCheckResponse;
import com.Rop.api.response.WheatfieldPasswordQueryResponse;
import com.Rop.api.response.WheatfieldPasswordSaveResponse;
import com.rongcapital.wallet.constants.PwdConstants;
import com.rongcapital.wallet.constants.RopConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.util.ROPClientUtils;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class PwdRop {

    private static DefaultRopClient ropClient =
            new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY, RopConstants.APP_SECRET);

    private static Logger logger = LoggerFactory.getLogger(PwdRop.class);

    /**
     * 设置或修改支付密码 Discription:
     * 
     * @param type
     * @param invoiceDate
     * @param batch
     * @param filePath
     * @return
     * @throws ApiException
     * @throws ParseException String
     * @author libi
     * @since 2016年8月24日
     */
    public static ResultVo saveOrUpdate(UserInfoVo user, String pwd, String pwdType, String operationType)
            throws ApiException, ParseException {

        ResultVo vo = new ResultVo();
        WheatfieldPasswordSaveRequest request = new WheatfieldPasswordSaveRequest();
        request.setPassword(pwd); // 支付密码
        request.setUserid(user.getUserId()); // 用户ID
        request.setConstid(user.getOrgId()); // 机构ID
        request.setProductid(user.getProId()); // 产品ID
        request.setPasswordtype(pwdType); // 密码类型
        request.setOpertype(operationType); // 操作类型
        logger.debug("调rop接口Wheatfield。Password。Save参数" + JsonUtil.bean2JsonStr(request));
        WheatfieldPasswordSaveResponse response = ROPClientUtils.execute(ropClient, request);
        logger.debug("调rop接口Wheatfield。Password。Save返回" + JsonUtil.bean2JsonStr(response));
        if (response.isSuccess()) {
            vo.setSuccess(true);
        } else {
            vo.setMsg(response.getMsg());
        }
        return vo;
    }

    /**
     * 验证是否设置过支付密码 Discription:
     * 
     * @param user
     * @param pwd
     * @param type
     * @return
     * @throws ApiException
     * @throws ParseException boolean
     * @author libi
     * @since 2016年8月24日
     */
    public static ResultVo isExistsPwd(UserInfoVo user, String type) throws ApiException, ParseException {
        ResultVo vo = new ResultVo();

        WheatfieldPasswordQueryRequest request = new WheatfieldPasswordQueryRequest();
        request.setUserid(user.getUserId()); // 用户ID
        request.setConstid(user.getOrgId()); // 机构ID
        request.setPasswordtype(type); // 密码类型
        logger.info("PwdRop.isExistsPwd-rop入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldPasswordQueryResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("PwdRop.isExistsPwd-rop返回:" + JsonUtil.bean2JsonStr(response));
        if (response.isSuccess()) {
            vo.setSuccess(true);
        } else {
            vo.setMsg(response.getMsg());
        }
        return vo;
    }

    /**
     * 
     * Discription:验证密码是否正确
     * 
     * @param user
     * @param type
     * @return
     * @throws ApiException
     * @throws ParseException ResultVo
     * @author Administrator
     * @since 2016年8月26日
     */
    public static ResultVo checkPwd(UserInfoVo user, String type, String pwd) throws ApiException, ParseException {
        ResultVo vo = new ResultVo();

        WheatfieldPasswordCheckRequest request = new WheatfieldPasswordCheckRequest();
        request.setUserid(user.getUserId()); // 用户ID
        request.setConstid(user.getOrgId()); // 机构ID
        request.setProductid(user.getProId());
        request.setPasswordtype(type); // 密码类型
        request.setPassword(pwd); // 密码
        logger.info("PwdRop.checkPwd-rop入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldPasswordCheckResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("PwdRop.checkPwd-rop入参:" + JsonUtil.bean2JsonStr(response));
        if (response.isSuccess()) {
        } else {
            vo.setMsg(response.getMsg());
        }
        vo.setSuccess(true);
        return vo;
    }

    public static void main(String[] args) {
        /*----------验证支付密码--------------*/
         UserInfoVo user = new UserInfoVo();
         user.setOrgId(UserPresetConstants.ORG_ID);
         user.setUserId("201610181820100001");
         try {
         ResultVo vo = PwdRop.isExistsPwd(user, PwdConstants.PWD_TYPE_PAY);
         System.out.println(vo.toString());
         } catch (Exception e) {
         e.fillInStackTrace();
         }

        /*---------设置密码--------*/
//        UserInfoVo u = new UserInfoVo();
//        u.setOrgId(UserPresetConstants.ORG_ID);
//        u.setUserId("rsqb_2016082516550001");
//        u.setProId(UserPresetConstants.PRO_ID);
//        String pwd = "123456"; // 支付密码
//        String pwdType = PwdConstants.PWD_TYPE_PAY;
//        String operationType = PwdConstants.PWD_TYPE_UPDATE_VALUE;
//
//        try {
//            ResultVo vo = PwdRop.saveOrUpdate(u, pwd, pwdType, operationType);
//            System.out.println(vo.toString());
//        } catch (Exception e) {
//
//        }
    }
}

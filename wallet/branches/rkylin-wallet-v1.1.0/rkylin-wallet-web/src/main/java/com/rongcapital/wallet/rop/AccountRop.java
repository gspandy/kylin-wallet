package com.rongcapital.wallet.rop;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.WheatfieldPersonAccountoprRequest;
import com.Rop.api.response.WheatfieldPersonAccountoprResponse;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.RopConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.util.ROPClientUtils;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

public class AccountRop {

    private static DefaultRopClient ropClient =
            new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY, RopConstants.APP_SECRET);
    private static Logger logger = LoggerFactory.getLogger(AccountRop.class);

    /**
     * kaihu Discription:
     * 
     * @param user
     * @return
     * @throws ApiException
     * @throws ParseException ResultVo
     * @author libi
     * @since 2016年8月30日
     */
    public static ResultVo openPersonAccount(UserInfoVo user, String operType) throws ApiException, ParseException {
        ResultVo vo = new ResultVo();
        
        WheatfieldPersonAccountoprRequest request = new WheatfieldPersonAccountoprRequest();

        request.setConstid(user.getOrgId());
        request.setProductid(user.getProId());
        request.setUserid(user.getUserId());

        request.setPersonchnname(user.getUserRealName());
        request.setCertificatetype(null != user.getIdCardType() && !"".equals(user.getIdCardType())
                ? user.getIdCardType() : String.valueOf(CardConstants.CARD_TYPE_CERTIFICATE_VALIDATE));
        request.setCertificatenumber(user.getIdCard());
        request.setOpertype(operType);
        request.setMobiletel(user.getUserName());
        logger.info("AccountRop.openPersonAccount-dubbo入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldPersonAccountoprResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("AccountRop.openPersonAccount-dubbo反回:" + JsonUtil.bean2JsonStr(response));

        if (response.isSuccess()) {
            vo.setSuccess(true);
        } else {
            vo.setMsg(response.getMsg());
        }
        return vo;

        // personchnname 必须 varchar 中文姓名（当操作类型是2：修改时，此项目为可选）
        // certificatetype 必须 varchar 证件类型,0身份证;1护照;2军官证;3士兵证;4回乡证;5户口本;6外国护照;7其它 （当操作类型是2：修改时，此项目为可选）
        // certificatenumber 必须 varchar 证件号（当操作类型是2：修改时，此项目为可选）
        // userid 必须 varchar 接入机构中设置的用户ID
        // constid 必须 varchar 机构码
        // productid 必须 varchar 产品号
        // opertype 必须 varchar 操作类型（1：新增，2：修改）

        // personengname 可选 varchar 英文姓名
        // persontype 可选 varchar 个人类别 默认1
        // personsex 可选 varchar 性别（1：女，2：男）
        // birthday 可选 varchar 出生日期,YYYYMMDD
        // mobiletel 可选 varchar 手机号码
        // fixtel 可选 varchar 固定电话号码
        // email 可选 varchar 电子邮箱
        // post 可选 varchar 邮编
        // address 可选 varchar 地址
        // remark 可选 varchar 备注
        // role 可选 varchar 角色号
        // username 可选 varchar 用户姓名

    }
    

    public static void main(String[] args) {
        String personChnName = "李碧"; // 中文姓名 //当操作类型是2：修改时，此项目为可选
        String certificateType = "0"; // 证件类型,0身份证;1护照;2军官证;3士兵证;4回乡证;5户口本;6外国护照;7其它 当操作类型是2：修改时，此项目为可选）
        String certificateNumber = "430626198409216759"; // 证件号（当操作类型是2：修改时，此项目为可选）

        String userId = "rsqb_201608311655000"; // 接入机构中设置的用户ID
        String constId = UserPresetConstants.ORG_ID; // 机构号
        String productId = UserPresetConstants.PRO_ID; // 产品号
        String username = "李碧"; // 用户姓名
        String operType = "1"; // 操作类型（1：新增，2：修改）

        UserInfoVo po = new UserInfoVo();
        po.setIdCardType(certificateType);
        po.setIdCard(certificateNumber);
        po.setUserId(userId);
        po.setOrgId(constId);
        po.setProId(productId);
        po.setUserRealName(username);
        try {
            ResultVo vo = AccountRop.openPersonAccount(po, operType);
            System.out.println(vo);
        } catch (Exception e) {

        }

    }
}

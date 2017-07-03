package com.rongcapital.wallet.rop;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Rop.api.ApiException;
import com.Rop.api.DefaultRopClient;
import com.Rop.api.domain.CityCode;
import com.Rop.api.request.WheatfieldCityQueryRequest;
import com.Rop.api.request.WheatfieldOrderTranstowithdrawRequest;
import com.Rop.api.response.WheatfieldCityQueryResponse;
import com.Rop.api.response.WheatfieldOrderTranstowithdrawResponse;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.RopConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.util.ROPClientUtils;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ResultVo;

public class CommonRop {

    private static DefaultRopClient ropClient =
            new DefaultRopClient(RopConstants.ROP_URL, RopConstants.APP_KEY, RopConstants.APP_SECRET, "json");

    private static Logger logger = LoggerFactory.getLogger(AccountRop.class);

    /**
     * 
     * Discription:查询省市
     * 
     * @param citycode省：（空） ;市：省Code
     * @return
     * @throws ApiException List<CityCode>
     * @author Administrator
     * @since 2016年8月29日
     */
    public static List<CityCode> banknQuery(String citycode) throws ApiException {
        WheatfieldCityQueryRequest request = new WheatfieldCityQueryRequest();
        request.setCitycode(citycode);// 省：（空） ;市：省Code
        logger.info("CommonRop.banknQuery-rop入参:" + JsonUtil.bean2JsonStr(request));
        WheatfieldCityQueryResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("CommonRop.banknQuery-rop返回" + JsonUtil.bean2JsonStr(response));
        return response.getCitycodes();
    }

    public static ResultVo creditWithdrawals() throws Exception {

        ResultVo vo = new ResultVo();
        WheatfieldOrderTranstowithdrawRequest request = new WheatfieldOrderTranstowithdrawRequest();

//         request.setAmount(100l); //消费金额
//         request.setRequestno("111122"); // 用户单号
//         request.setRequesttime(new Date()); // 请求时间
//        
//         request.setMerchantcode(UserPresetConstants.ORG_ID); //出钱方商户编码
//         request.setProductid(UserPresetConstants.PRO_CREDIT_ID); //出钱方产品号
//         request.setUserid("201609021546470001"); //出钱方用户ID
//        
//         request.setIntermerchantcode(UserPresetConstants.ORG_ID);// 入钱方商户编号
//         request.setInterproductid(UserPresetConstants.PRO_ID); //入钱方产品号
//         request.setUserrelateid("201609021546470001"); // 入钱方用户ID
//        
//         request.setTransfee(0l); // 转账手续费（单位为分）
//         request.setWithdrawfee(0l); // 提现手续费（单位为分）

        logger.info("CommonRop.creditWithdrawals-rop入参:" + JsonUtil.bean2JsonStr(request));

        WheatfieldOrderTranstowithdrawResponse response = ROPClientUtils.execute(ropClient, request);
        logger.info("CommonRop.creditWithdrawals-rop返回" + JsonUtil.bean2JsonStr(response));
        if (response.isSuccess()) {
            vo.setSuccess(true);
        } else {
            vo.setCode(ErrorCodeEnum.CREDIT_WITHDR_ERROR.getCode());
            vo.setMsg(response.getMsg());
        }
        return vo;
    }
    
    public static void main(String[] args) {
        
        try {
            ResultVo vo= creditWithdrawals();
            System.out.println(vo.toString());
        } catch (Exception e) {
           
        }
    }
}

package com.rongcapital.wallet.controller.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Rop.api.ApiException;
import com.Rop.api.domain.BankInfo;
import com.Rop.api.domain.CityCode;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.rop.BankCardRop;
import com.rongcapital.wallet.rop.CommonRop;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ActionResult;

@Controller
@RequestMapping("/banknQuery")
public class BanknQueryController {

    private static Logger logger = LoggerFactory.getLogger(BanknQueryController.class);

    @ResponseBody
    @RequestMapping("/query")
    @SystemControllerLog(description = "banknQuery/query")
    public ActionResult query(String bankcode, String citycode) {
        ActionResult ar = new ActionResult();
        try {
            List<BankInfo> banknQuery = BankCardRop.banknQuery(bankcode, citycode);
            ar.addResult("data", banknQuery);
            ar.setSuccess(true);
        } catch (ApiException e) {
            logger.error("banknQuerye.query",e);
            ar.setMsg("系统调用rop异常，请重试！");
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        }
        logger.debug("/banknQuery/query.do返回" + JsonUtil.bean2JsonStr(ar));
       
        return ar;
    }

    /**
     * 
     * Discription:查询省市
     * 
     * @param citycode省：（空） ;市：省Code
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月29日
     */
    @ResponseBody
    @RequestMapping("/queryCity")
    @SystemControllerLog(description = "查询省市")
    public ActionResult queryCity(String citycode) {
        ActionResult ar = new ActionResult();
        try {
            List<CityCode> banknQuery = CommonRop.banknQuery(citycode);
            ar.addResult("data", banknQuery);
            ar.setSuccess(true);
        } catch (ApiException e) {
            logger.error("banknQuerye.queryCity",e);       
            ar.setMsg("系统调用rop异常，请重试！");
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        }

        logger.debug("/banknQuery/queryCity.do返回" + JsonUtil.bean2JsonStr(ar));
       
        return ar;
    }
}

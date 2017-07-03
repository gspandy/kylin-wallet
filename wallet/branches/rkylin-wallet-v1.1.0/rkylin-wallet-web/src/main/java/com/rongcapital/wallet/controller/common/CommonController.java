package com.rongcapital.wallet.controller.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rkylin.order.domain.M000003ServiceBean;
import com.rkylin.order.service.OrderInfoBaseService;
import com.rongcapital.redis.sdr.base.BaseRedisService;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.Settings;
import com.rongcapital.wallet.constants.SystemConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.rop.BankCardRop;
import com.rongcapital.wallet.util.RedisIdGeneratorExt;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.date.DateUtil;
import com.rongcapital.wallet.util.encryption.EncryptionUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.UserInfoVo;

/**
 * 
 * Description:通用
 * 
 * @author: Administrator
 * @CreateDate: 2016年8月23日
 * @version: V1.0
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    private static Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private OrderInfoBaseService orderInfoBaseService;
    @Autowired
    private RedisIdGeneratorExt redisIdGeneratorExt;
    @Autowired
    private BaseRedisService<String, String> baseRedisService;

    /**
     * Discription:生成订单编号
     * 
     * @param token
     * @return ActionResult
     * @author Administrator
     * @since 2016年8月31日
     */
    @ResponseBody
    @RequestMapping("/genOrderNo")
    @SystemControllerLog(description = "生成订单编号")
    public ActionResult genOrderNo(String token) {

        ActionResult ar = new ActionResult();
        try {

            ar.addResult("data", redisIdGeneratorExt.genOrderNo());
        } catch (Exception e) {
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getValue());
        }
        logger.debug("/common/genOrderNo.do返回" + JsonUtil.bean2JsonStr(ar));
        ar.setSuccess(true);
        return ar;
    }

    @ResponseBody
    @RequestMapping("/getUrl")
    @SystemControllerLog(description = "getUrl")
    public ActionResult getUrl(String token, String amount, String bankNo, String bankName) {

        ActionResult ar = new ActionResult();
        try {

            UserInfoVo uipo = TokenUtil.getData(token);

            if (ValidateUtils.isEmpty(bankNo)) {
                ar.setSuccess(false);
                ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                ar.setMsg("银行卡号不能为空");
                return ar;
            }
            if (ValidateUtils.isEmpty(amount)) {
                ar.setSuccess(false);
                ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                ar.setMsg("金额不能为空");
                return ar;
            }

            if (null == BankCardRop.checkBankNo(uipo, bankNo)) {
                ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                ar.setMsg("非绑定卡不能充值！");
                logger.debug("非绑定卡不能充值！");
                return ar;
            }
            String generateOrder = redisIdGeneratorExt.genOrderNo();
            String ordertime = DateUtil.getCurrentStrDateFormat(DateUtil.YYYYMMDDHHMMSS);
            String time = DateUtil.getCurrentStrDateFormat(DateUtil.YYYY_MM_DD);
            M000003ServiceBean serviceBean = new M000003ServiceBean();
            serviceBean.setUserId(uipo.getUserId());
            serviceBean.setConstid(UserPresetConstants.ORG_ID);// 机构码
            serviceBean.setOrderTypeId(Settings.getInstance().getOrdertypeid());// 基础业务为B，扩展业务待定
            serviceBean.setProductId(UserPresetConstants.PRO_ID);// 产品号
            serviceBean.setOpertype("1");// 操作类型（修改：2,新增：1,取消4,查询3）
            serviceBean.setOrderDate(time);// 订单日期
            serviceBean.setOrderTime(ordertime);// 订单时间
            serviceBean.setAmount(amount);
            serviceBean.setUserOrderId(generateOrder);
            serviceBean.setAdjustContent(bankNo);
            serviceBean.setGoodsName("23");
            serviceBean.setRemark("余额充值");

            logger.debug("调用dubbo接口com.rkylin.order.service.OrderInfoBaseService.saveServiceOrder，参数:"
                    + JsonUtil.bean2JsonStr(serviceBean));
            Map<String, String> saveServiceOrder = orderInfoBaseService.saveServiceOrder(serviceBean);
            logger.debug("调用dubbo接口com.rkylin.order.service.OrderInfoBaseService.saveServiceOrder，返回:"
                    + JsonUtil.bean2JsonStr(saveServiceOrder));
            if ("false".equals(saveServiceOrder.get("issuccess").toString())) {

                ar.setSuccess(false);
                ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                ar.setMsg(saveServiceOrder.get("retmsg"));
                return ar;
            }

            StringBuffer temp = new StringBuffer();
            temp.append("merchantNo=").append(UserPresetConstants.ORG_ID).append("&orderNo=")
                    .append(saveServiceOrder.get("orderid")).append("&orderAmount=").append(amount).append("&payType=")
                    .append("23").append("&orderTime=").append(ordertime).append("&signType=").append("1")
                    .append("&version=").append("v1.0").append("&key=").append(Settings.getInstance().getKey());
            String encode = EncryptionUtil.md5Encry(temp.toString());
            StringBuffer url = new StringBuffer();

            url.append(Settings.getInstance().getUrl()).append("?merchantNo=").append(UserPresetConstants.ORG_ID)
                    .append("&orderNo=").append(saveServiceOrder.get("orderid")).append("&orderAmount=").append(amount)
                    .append("&payType=23").append("").append("&orderTime=").append(ordertime).append("&signType=")
                    .append("1").append("&version=").append("v1.0").append("&signMsg=").append(encode)
                    .append("&charset=1&busiCode=101&orderMark=1&notifyUrl=")
                    .append(Settings.getInstance().getNotifyUrl()).append("&amtType=01&payerName=")
                    .append(URLEncoder.encode(uipo.getUserRealName(), "UTF-8")).append("&pageUrl=")
                    .append(Settings.getInstance().getPageUrl());// .append("&payerPhone=").append(uipo.getTel());
            ar.addResult("url", url.toString());

            ar.addResult("urlnew", Settings.getInstance().getUrl());

        } catch (Exception e) {
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getValue());
        }

        logger.debug("/common/getUrl.do返回" + JsonUtil.bean2JsonStr(ar));
        ar.setSuccess(true);
        return ar;
    }

    @RequestMapping("/recharge")
    public ModelAndView recharge(HttpServletRequest request) throws UnsupportedEncodingException {
        ModelAndView mav = new ModelAndView("params/recharge/recharge");

        request.setCharacterEncoding("utf-8");
        StringBuffer signSb = new StringBuffer();

        signSb.append("merchanNo=");
        String merchanNo = "";
        if (request.getParameter("merchanNo") != null)
            merchanNo = request.getParameter("merchanNo");
        signSb.append(merchanNo);

        signSb.append("&payType=");
        String payType = "";
        if (request.getParameter("payType") != null)
            payType = request.getParameter("payType");
        signSb.append(payType);

        signSb.append("&orderNo=");
        String orderNo = "";
        if (request.getParameter("orderNo") != null)
            orderNo = request.getParameter("orderNo");
        signSb.append(orderNo);
        mav.addObject("orderNo", orderNo);

        signSb.append("&payOrderNo=");
        String payOrderNo = "";
        if (request.getParameter("payOrderNo") != null)
            payOrderNo = request.getParameter("payOrderNo");
        signSb.append(payOrderNo);
        mav.addObject("payOrderNo", payOrderNo);

        signSb.append("&payStatus=");
        String payStatus = "";
        if (request.getParameter("payStatus") != null)
            payStatus = request.getParameter("payStatus");
        signSb.append(payStatus);

        signSb.append("&orderTime=");
        String orderTime = "";
        if (request.getParameter("orderTime") != null)
            orderTime = request.getParameter("orderTime");
        signSb.append(orderTime);
        mav.addObject("orderTime", orderTime);

        signSb.append("&orderAmount=");
        String orderAmount = "";
        if (request.getParameter("orderAmount") != null)
            orderAmount = request.getParameter("orderAmount");
        signSb.append(orderAmount);
        mav.addObject("orderAmount", orderAmount);

        signSb.append("&bankCode=");
        String bankCode = "";
        if (request.getParameter("bankCode") != null)
            bankCode = request.getParameter("bankCode");
        signSb.append(bankCode);

        signSb.append("&orderPayTime=");
        String orderPayTime = "";
        if (request.getParameter("orderPayTime") != null)
            orderPayTime = request.getParameter("orderPayTime");
        signSb.append(orderPayTime);
        mav.addObject("orderPayTime", orderPayTime);

        String plain = signSb.toString();

        return new ModelAndView("redirect:" + Settings.getInstance().getRedirectUrl() + "?" + plain);
    }

    @RequestMapping("/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        StringBuffer signSb = new StringBuffer();

        signSb.append("merchanNo=");
        String merchanNo = "";
        if (request.getParameter("merchanNo") != null)
            merchanNo = request.getParameter("merchanNo");
        signSb.append(merchanNo);

        signSb.append("&payType=");
        String payType = "";
        if (request.getParameter("payType") != null)
            payType = request.getParameter("payType");
        signSb.append(payType);

        signSb.append("&orderNo=");
        String orderNo = "";
        if (request.getParameter("orderNo") != null)
            orderNo = request.getParameter("orderNo");
        signSb.append(orderNo);

        signSb.append("&payOrderNo=");
        String payOrderNo = "";
        if (request.getParameter("payOrderNo") != null)
            payOrderNo = request.getParameter("payOrderNo");
        signSb.append(payOrderNo);

        signSb.append("&payStatus=");
        String payStatus = "";
        if (request.getParameter("payStatus") != null)
            payStatus = request.getParameter("payStatus");
        signSb.append(payStatus);

        signSb.append("&orderTime=");
        String orderTime = "";
        if (request.getParameter("orderTime") != null)
            orderTime = request.getParameter("orderTime");
        signSb.append(orderTime);

        signSb.append("&orderAmount=");
        String orderAmount = "";
        if (request.getParameter("orderAmount") != null)
            orderAmount = request.getParameter("orderAmount");
        signSb.append(orderAmount);

        signSb.append("&bankCode=");
        String bankCode = "";
        if (request.getParameter("bankCode") != null)
            bankCode = request.getParameter("bankCode");
        signSb.append(bankCode);

        signSb.append("&orderPayTime=");
        String orderPayTime = "";
        if (request.getParameter("orderPayTime") != null)
            orderPayTime = request.getParameter("orderPayTime");
        signSb.append(orderPayTime);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("returnCode=000000&returnMsg=处理成功");
        } catch (IOException e) {
            logger.error("通知返回异常", e);
        } finally {
            if (out != null) {
                out.flush();
            }
        }

    }

    @ResponseBody
    @RequestMapping("/getTips")
    @SystemControllerLog(description = "getTips")
    public ActionResult getTips(String token) {
        ActionResult ar = new ActionResult();
        try {
            ar.addResult("tips", baseRedisService.get(SystemConstants.getKeyTIPS("tips")));
            ar.addResult("singleLimit", baseRedisService.get(SystemConstants.getKeyTIPS("singleLimit")));
        } catch (Exception e) {
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getValue());
            logger.error("redis异常", e);
        }
        logger.info("/common/getTips.do返回" + JsonUtil.bean2JsonStr(ar));
        ar.setSuccess(true);
        return ar;
    }

    @ResponseBody
    @RequestMapping("/changeTips")
    @SystemControllerLog(description = "changeTips")
    public ActionResult changeTips(String tips, String singleLimit) {
        ActionResult ar = new ActionResult();
        try {
            baseRedisService.set(SystemConstants.getKeyTIPS("tips"), tips, -1);
            baseRedisService.set(SystemConstants.getKeyTIPS("singleLimit"), singleLimit, -1);
            ar.addResult("tips", baseRedisService.get(SystemConstants.getKeyTIPS("tips")));
            ar.addResult("singleLimit", baseRedisService.get(SystemConstants.getKeyTIPS("singleLimit")));
        } catch (Exception e) {
            ar.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
            ar.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getValue());
            logger.error("redis异常", e);
        }
        logger.info("/common/changeTips.do返回" + JsonUtil.bean2JsonStr(ar));
        ar.setSuccess(true);
        return ar;
    }
}
package com.rongcapital.wallet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.Rop.api.internal.cache.ValidateUtil;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.controller.rop.constants.Constants;
import com.rongcapital.wallet.controller.rop.constants.SpringBeanConstants;
import com.rongcapital.wallet.controller.rop.response.ErrorResponse;
import com.rongcapital.wallet.controller.rop.response.Response;
import com.rongcapital.wallet.controller.rop.service.security.ISecurityService;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;

public class LoginFilter extends HttpServlet implements Filter {

    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private final static String LOGIN_PATH_ROOT = "/"; // root
    private final static String LOGIN_PATH_LOGIN = "/user/login.do"; // 登陆
    private final static String LOGIN_PATH_REGIST = "/user/register.do"; // 注册
    private final static String LOGIN_PATH_LOGIN_EXISTS = "/user/loginNameIsExist.do"; // 用户是否存在
    private final static String IDENTITY_AUTH = "user/identityAuth.do"; // 实名认证
    private final static String LOGIN_PWD_BACK = "/pwd/loginPwdBack.do"; // 找回登陆密码
    private final static String USER_AUTH = "/user/userAuth.do"; // 实名认证

    private final static String SMS_SEND = "/sms/sendSMS.do"; // 发短信
    private final static String SMS_VALIDATE = "/sms/validate.do"; // 验证短信
    private final static String COMMON_RECHARGE = "/common/recharge.do"; // 冲值成功跳转
    private final static String COMMON_NOTIFY = "/common/notify.do"; // 充值成功后台通知
    private final static String COMMON_CHANGETIPS = "/common/changeTips.do"; // 充值成功后台通知

    public final static String ROP_URL = "/ropapi.do"; // rop

    // 导开户数据
    private final static String BATH_REGISTER = "/temp/bathRegPerson.do";
    private final static String BATH_REGISTER_COMPANY = "/temp/bathRegCompany.do";

    // 新接口
    private final static String LOGIN_PATH_LOGIN_NEW = "/user/loginNew.do"; // 登陆
    private final static String LOGIN_PWD_BACK_NEW = "/pwd/loginPwdBackNew.do";

    public final static Map<String, String> mapUrl = new CaseInsensitiveMap();
    public final static String CHARSET = "UTF-8";

    @Autowired
    private ISecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 取到请求的url根目录
        String conPath = filterConfig.getServletContext().getContextPath();

        if (null != conPath && conPath.length() > 0) {
            mapUrl.put(conPath + LOGIN_PATH_ROOT, "");
            mapUrl.put(conPath + LOGIN_PATH_LOGIN, "");
            mapUrl.put(conPath + LOGIN_PATH_REGIST, "");
            mapUrl.put(conPath + LOGIN_PATH_LOGIN_EXISTS, "");
            mapUrl.put(conPath + IDENTITY_AUTH, "");
            mapUrl.put(conPath + SMS_SEND, "");
            mapUrl.put(conPath + SMS_VALIDATE, "");
            mapUrl.put(conPath + LOGIN_PWD_BACK, "");
            mapUrl.put(conPath + USER_AUTH, "");
            mapUrl.put(conPath + COMMON_RECHARGE, "");
            mapUrl.put(conPath + COMMON_NOTIFY, "");
            mapUrl.put(conPath + COMMON_CHANGETIPS, "");
            mapUrl.put(conPath + ROP_URL, "");
            mapUrl.put(conPath + BATH_REGISTER, "");
            mapUrl.put(conPath + BATH_REGISTER_COMPANY, "");
            mapUrl.put(conPath + LOGIN_PATH_LOGIN_NEW, "");
            mapUrl.put(conPath + LOGIN_PWD_BACK_NEW, "");
        } else {

            mapUrl.put(LOGIN_PATH_ROOT, "");
            mapUrl.put(LOGIN_PATH_LOGIN, "");
            mapUrl.put(LOGIN_PATH_REGIST, "");
            mapUrl.put(LOGIN_PATH_LOGIN_EXISTS, "");
            mapUrl.put(IDENTITY_AUTH, "");
            mapUrl.put(SMS_SEND, "");
            mapUrl.put(SMS_VALIDATE, "");
            mapUrl.put(LOGIN_PWD_BACK, "");
            mapUrl.put(USER_AUTH, "");
            mapUrl.put(COMMON_RECHARGE, "");
            mapUrl.put(COMMON_NOTIFY, "");
            mapUrl.put(COMMON_CHANGETIPS, "");
            mapUrl.put(ROP_URL, "");
            mapUrl.put(BATH_REGISTER, "");
            mapUrl.put(BATH_REGISTER_COMPANY, "");
            mapUrl.put(LOGIN_PATH_LOGIN_NEW, "");
            mapUrl.put(LOGIN_PWD_BACK_NEW, "");
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain)
            throws IOException, ServletException {

        ActionResult result = new ActionResult();

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rsp;

        String url = request.getRequestURI();

        logger.info("LoginFilter.doFilter请求连接:{" + url + "}");

        /*-------rop---*/
        if (url.indexOf(ROP_URL) != -1) {
            vilidateRop(request.getParameterMap(), result);
            if (!"".equals(result.getMsg())) {
                writeResult(response, result);
                return;
            }
        }

        String token = request.getParameter("token");
        String userId = request.getParameter("userId");

       

        if (mapUrl.containsKey(url))

        {
   
            chain.doFilter(new ParmRequestWrapper(request), response);
            return;
        }

        if (null == token || "".equals(token) || null == userId || "".equals(userId)) {

            result.setSuccess(false);
            result.setCode(ErrorCodeEnum.LOGIN_NO.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_NO.getValue());
            writeResult(response, result);
           
        } else {
            boolean tokenExis = true;
            try {
                tokenExis = TokenUtil.validateToken(token, userId);
                if (!tokenExis) {
                    result.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode());
                    result.setMsg(ErrorCodeEnum.LOGIN_TIMEOUT.getValue());
                    writeResult(response, result);
                   
                } else {
                    TokenUtil.refreshToken(token, userId); // 刷新token                  
                    chain.doFilter(new ParmRequestWrapper(request), response);
                }
            } catch (Exception e) {
                logger.error("验证Token错误validateToken", e);
                result.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                result.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getValue());

            }

        }
    }

    /**
     * 
     * Discription:
     * 验证rop参数
     * @return ActionResult
     * @author libi
     * @since 2016年11月8日
     */
    private void vilidateRop(Map<String, String[]> requestParams, ActionResult result) {

        logger.info("requestParams:"+JsonUtil.bean2JsonStr(requestParams));
        Response res = securityService.verifyRequest(requestParams);
        if (null != res) {
            ErrorResponse errRes = (ErrorResponse) res;
            result.setCode(errRes.getCode());
            result.setMsg(errRes.getMsg());
            return;
        }
        String[] method = requestParams.get(Constants.SYS_PARAM_METHOD);       
        // String[] format = requestParams.get(Constants.SYS_PARAM_FORMAT);
        if (ValidateUtils.isEmpty((method)) || method[0].split("\\.").length != 5) {
            result.setCode("00000");
            result.setMsg("系统参数有误");
            logger.error("rop配置参数必须为ruixue.wheatfield.sysname.controller.method");
            return;
        }

    }
    
    /**
     * 写客户端
     * Discription:
     * @param response
     * @param result void
     * @author libi
     * @since 2016年11月9日
     */
    private void writeResult(HttpServletResponse response, ActionResult result) {
        response.setCharacterEncoding(CHARSET);
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JsonUtil.bean2JsonStr(result));
        } catch (IOException e) {
            logger.error("无token或userId返回json数据处理失败", e);
        } finally {
            if (out != null) {
                out.flush();
            }
        }
    }
}

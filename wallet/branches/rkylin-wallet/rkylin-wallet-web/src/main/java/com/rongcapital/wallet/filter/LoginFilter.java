package com.rongcapital.wallet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.util.TokenUtil;
import com.rongcapital.wallet.util.json.JsonUtil;
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
    private final static String COMMON_RECHARGE = "/common/recharge.do"; //冲值成功跳转
    private final static String COMMON_NOTIFY = "/common/notify.do"; // 充值成功后台通知 
     
    private final static Map<String, String> mapUrl = new HashMap<>();

    // private final static List<String> list = new ArrayList<>();

    private static String WEB_URL = null;
    private static Boolean WEB_URL_FILTER = true;
    // private final static List<String> list2 = new ArrayList<>();

    private final static String CHARSET = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

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
        // list.add(LOGIN_PATH_ROOT);
        // list.add(LOGIN_PATH_LOGIN);
        // list.add(WEB_URL + LOGIN_PATH_REGIST);
        // list.add(WEB_URL + LOGIN_PATH_LOGIN_EXISTS);
        // list.add(IDENTITY_AUTH);
        // list.add(SMS_SEND);
        // list.add(SMS_VALIDATE);
        // list.add(LOGIN_PWD_BACK);
        //
        // list2.add(LOGIN_PATH_ROOT);
        // list2.add(WEB_URL + LOGIN_PATH_LOGIN);
        // list2.add(WEB_URL + LOGIN_PATH_REGIST);
        // list2.add(WEB_URL + LOGIN_PATH_LOGIN_EXISTS);
        // list2.add(WEB_URL + IDENTITY_AUTH);
        // list2.add(WEB_URL + SMS_SEND);
        // list2.add(WEB_URL + SMS_VALIDATE);
        // list2.add(WEB_URL + LOGIN_PWD_BACK);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rsp;

        String url = request.getRequestURI();
        if (WEB_URL == null) {
            if (!url.equals("/")) {
                WEB_URL = request.getContextPath();
                if (null == WEB_URL || WEB_URL.length() == 0) {
                    WEB_URL_FILTER = false;
                }
            }
        }

        if (WEB_URL!=null && WEB_URL_FILTER) {
            url = url.substring(url.indexOf(WEB_URL) + WEB_URL.length());
        }

        String token = request.getParameter("token");
        String userId = request.getParameter("userId");

        logger.info("LoginFilter.doFilter请求连接:{" + url + "}");

        logger.info("LoginFilter.doFilter参数:{token:" + token + ",userId:" + userId + "}");

        if (mapUrl.containsKey(url)) {
            chain.doFilter(request, response);
            return;
        }
        // if (url.indexOf(WEB_URL) != -1) {
        //
        // if (list2.contains(url)) {
        // chain.doFilter(request, response);
        // return;
        // }
        // } else {
        // if (list.contains(url)) {
        // chain.doFilter(request, response);
        // return;
        // }
        // }

        ActionResult result = new ActionResult();
        if (null == token || "".equals(token) || null == userId || "".equals(token)) {

            result.setSuccess(false);
            result.setCode(ErrorCodeEnum.LOGIN_NO.getCode());
            result.setMsg(ErrorCodeEnum.LOGIN_NO.getValue());
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

        } else {
            boolean tokenExis = true;
            try {
                tokenExis = TokenUtil.validateToken(token, userId);
                if (!tokenExis) {
                    result.setCode(ErrorCodeEnum.LOGIN_TIMEOUT.getCode());
                    result.setMsg(ErrorCodeEnum.LOGIN_TIMEOUT.getValue());
                    response.setCharacterEncoding(CHARSET);
                    response.setContentType("application/json");
                    PrintWriter out = null;
                    try {
                        out = response.getWriter();
                        out.append(JsonUtil.bean2JsonStr(result));
                    } catch (IOException e) {
                        logger.error("有token有userId返回json数据处理失败", e);
                    } finally {
                        if (out != null) {
                            out.flush();

                        }
                    }
                } else {
                    chain.doFilter(request, response);
                }
            } catch (Exception e) {
                logger.error("验证Token错误validateToken", e);
                result.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
                result.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getValue());

            }

        }
    }

    public static void main(String[] args) {
        String a = "/rkylin-wallet-web";
        String b = "/rkylin-wallet-web/user/user.do";
        System.out.println(b.indexOf(a));
        System.out.println(b.substring(b.indexOf(a) + a.length()));
        // "/rkylin-wallet-web/user.";
    }
}

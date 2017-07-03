package com.rongcapital.wallet.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ActionResult;

public class ExceptionHandler implements HandlerExceptionResolver {

    private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private final static String CHARSET = "UTF-8";

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        ModelAndView mv = new ModelAndView();

        ActionResult result = new ActionResult();
        result.setCode(ErrorCodeEnum.SYSTEM_ERROR.getCode());
        result.setMsg(ErrorCodeEnum.SYSTEM_ERROR.getValue());
        response.setCharacterEncoding(CHARSET);
        response.setContentType("application/json");
        PrintWriter out = null;
        try { 
            logger.info("ExceptionHandler.resolveException返回结果:{" + JsonUtil.bean2JsonStr(result) + "}");
            logger.error(ErrorCodeEnum.SYSTEM_ERROR.getValue(), ex);
            out = response.getWriter();
            out.append(JsonUtil.bean2JsonStr(result));
        } catch (Exception e) {
            logger.error("ExceptionHandler.resolveException", e);
        } finally {
            if (out != null) {
                out.flush();

            }
        }

        return mv;
    }

}

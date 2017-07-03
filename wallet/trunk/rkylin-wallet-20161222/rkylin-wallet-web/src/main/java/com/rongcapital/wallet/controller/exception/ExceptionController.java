package com.rongcapital.wallet.controller.exception;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.controller.user.UserController;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.ClientInfo;
import com.rongcapital.wallet.vo.UserInfoVo;

/**
 * 异常处理 Description:
 * 
 * @author: libi
 * @CreateDate: 2016年9月12日
 * @version: V1.0
 */
@Controller
@RequestMapping("/exception")
public class ExceptionController {

    private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    
    @ResponseBody
    @RequestMapping("/error403")
    @SystemControllerLog(description = "403错误")
    public ActionResult error403() {

        ActionResult result = new ActionResult();
        result.setCode(ErrorCodeEnum.SYSTEM_ERROR_403.getCode());
        result.setMsg(ErrorCodeEnum.SYSTEM_ERROR_403.getValue());
        return result;

    }
    
    
    @ResponseBody
    @RequestMapping("/error404")
    @SystemControllerLog(description = "404错误")
    public ActionResult error404() {

        ActionResult result = new ActionResult();
        result.setCode(ErrorCodeEnum.SYSTEM_ERROR_404.getCode());
        result.setMsg(ErrorCodeEnum.SYSTEM_ERROR_404.getValue());
        return result;

    }
    

    
    @ResponseBody
    @RequestMapping("/error500")
    @SystemControllerLog(description = "500错误")
    public ActionResult error500() {

        ActionResult result = new ActionResult();
        result.setCode(ErrorCodeEnum.SYSTEM_ERROR_500.getCode());
        result.setMsg(ErrorCodeEnum.SYSTEM_ERROR_500.getValue());
        return result;

    }
}

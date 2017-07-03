package com.rongcapital.wallet.controller.rop;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import org.springframework.web.servlet.view.RedirectView;
import com.rongcapital.wallet.controller.rop.constants.Constants;
import com.rongcapital.wallet.controller.rop.constants.SpringBeanConstants;
import com.rongcapital.wallet.controller.rop.response.ErrorResponse;
import com.rongcapital.wallet.controller.rop.response.Response;
import com.rongcapital.wallet.controller.rop.service.security.ISecurityService;
import com.rongcapital.wallet.controller.rop.utils.LogUtils;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.controller.rop.utils.ModelAndViewUtils;
import com.rongcapital.wallet.log.SystemControllerLog;

@Controller
public class RopController {
    private static Logger logger = LoggerFactory.getLogger(RopController.class);

    @Resource(name = SpringBeanConstants.SECURITY_SERVICE)
    private ISecurityService securityService;

    private String systemErrorCode;

    @RequestMapping("/ropapi")
    public ModelAndView execute(HttpServletRequest request) throws Exception {

        Map<String, String[]> requestParams = request.getParameterMap();
        logger.info("入参:" + JsonUtil.bean2JsonStr(requestParams));
        // logger.info("-----------appkey--------------"+requestParams.get(Constants.SYS_PARAM_APP_KEY)[0]);
        // 1. 验证request参数
        // ErrorResponse response = (ErrorResponse) securityService.verifyRequest(requestParams);
        //
        // System.out.println(JsonUtil.bean2JsonStr(response));
        // if(null!=response){
        // String url = "forward:/ropError.do";
        // ModelMap mmap = new ModelMap();
        // request.setAttribute("aaaa", "aaaa");
        // mmap.put("code", "000");
        // mmap.put("msg", "系统参数验证不通过");
        // return new ModelAndView(url, mmap);
        // }

        String method = request.getParameter(Constants.SYS_PARAM_METHOD);

        String format = request.getParameter(Constants.SYS_PARAM_FORMAT);
        // method pattern ruixu.appid.beanid.invokemethodname
        // 如果验证通过

        long start = System.currentTimeMillis();
        try {

            String[] strs = method.split("\\.");

            String conName = strs[3];
            String methodName = strs[4];
            logger.info("conName:" + conName);
            logger.info("methodName:" + methodName);    
            String url = "forward:/" + conName + "/" + methodName + ".do";            
            logger.info("跳转前的url:" + url);
            return new ModelAndView(url, null);

        } catch (Exception e) {
            logger.error("rop controller 调用方法" + method + "失败：", e);
            // response = new ErrorResponse(this.systemErrorCode, "调用" + method + "异常");
        }
        long end = System.currentTimeMillis();
        logger.debug("调用接口" + method + "耗时" + (end - start) + "毫秒");

        return null;
    }

    // @ResponseBody
    // @RequestMapping("/ropError")
    // public ActionResult ropError(ActionResult result,HttpServletRequest request) throws Exception {
    // System.out.println(JsonUtil.bean2JsonStr(request.getParameterMap()));
    // System.out.println("执行了");
    // return result;
    // }
    //
    // public static String columnToProperty(String column) {
    // StringBuilder result = new StringBuilder();
    // // 快速检查
    // if (column == null || column.isEmpty()) {
    // // 没必要转换
    // return "";
    // } else if (!column.contains("_")) {
    // // 不含下划线，仅将首字母小写
    // return column.substring(0, 1).toLowerCase() + column.substring(1);
    // } else {
    // // 用下划线将原始字符串分割
    // String[] columns = column.split("_");
    // for (String columnSplit : columns) {
    // // 跳过原始字符串中开头、结尾的下换线或双重下划线
    // if (columnSplit.isEmpty()) {
    // continue;
    // }
    // // 处理真正的驼峰片段
    // if (result.length() == 0) {
    // // 第一个驼峰片段，全部字母都小写
    // result.append(columnSplit.toLowerCase());
    // } else {
    // // 其他的驼峰片段，首字母大写
    // result.append(columnSplit.substring(0, 1).toUpperCase())
    // .append(columnSplit.substring(1).toLowerCase());
    // }
    // }
    // return result.toString();
    // }
    //
    // }

    public static void main(String[] args) {

    }
}

package com.rongcapital.wallet.controller.temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.service.LoginAipService;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.SMSConstants;
import com.rongcapital.wallet.constants.Settings;
import com.rongcapital.wallet.constants.UserInfoConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.dubbo.AccountDubbo;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.rop.AccountRop;
import com.rongcapital.wallet.rop.SendSMS;
import com.rongcapital.wallet.rop.UserRop;
import com.rongcapital.wallet.threads.SendPoolService;
import com.rongcapital.wallet.util.RedisIdGeneratorExt;
import com.rongcapital.wallet.util.UserExcelRead;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.CompanyVo;
import com.rongcapital.wallet.vo.ResultVo;
import com.rongcapital.wallet.vo.UserInfoVo;

@Controller
@RequestMapping("/temp")
public class TempController {

    private static Logger logger = LoggerFactory.getLogger(TempController.class);

    @Autowired
    SendPoolService sendPoolService;

    @Autowired
    private UserInfoApiService userInfoApiService;

    @Autowired
    private LoginAipService loginAipService;

    @Autowired
    private RedisIdGeneratorExt redisIdGeneratorExt;

    @Autowired
    private AccountDubbo accountDubbo;

    private String filePathPerson = Settings.getInstance().getFilePathPerson();

    private String filePathCompany = Settings.getInstance().getFilePathCompany();

    private static final String user_pwd = "1234!qaz"; // 客户端密码

    private static final String salt = "sJt4ChymaV53LIaduKqEnU+wxEw=";

    private static final String downUrl = "https://qyqianbao.com/";

    private static final String pwd = "239662baaa4044f5305130ebec883bfb37f36f066542e8e3466a121de7e47003";

    private static final String send_key = "1234!qaz!abcdef";

    @ResponseBody
    @RequestMapping("/bathRegPerson")
    @SystemControllerLog(description = "注册")
    public ActionResult bathRegPerson(String key, String vilidate) throws Exception {

        ActionResult result = new ActionResult();
        if (null == key || !key.equals(send_key)) {
            result.setCode("5555");
            result.setMsg("没有权限操作");
            return result;
        }
        File file = new File(filePathPerson);
        if (!file.exists()) {
            result.setCode("6666");
            result.setMsg("找不到：{" + filePathPerson + "}文件");
            return result;
        }

        Integer num = 0;
        List<UserInfoVo> list = UserExcelRead.getListPerson(file);
        List<String> sussessList = new ArrayList<>();

        if (null == list || list.size() == 0) {
            result.setCode("3434");
            result.setMsg("未找到注册的企业信息");
        }

        logger.info("TempController.bathRegPerson 读取到注册用户条数:{" + list.size() + "}");

        for (int i = 0; i < list.size(); i++) {
            UserInfoVo userInfo = list.get(i);
            logger.info("TempController.bathRegPerson 正在注册第:{" + (i + 1) + "}个用户");
            logger.info("TempController.bathRegPerson 用户信息:{" + JsonUtil.bean2JsonStr(userInfo) + "}");

            if (null == userInfo.getUserName() || null == userInfo.getUserRealName()) {
                logger.info("TempController.bathRegister 第:{" + (i + 1) + "}个用户信息不完整");
                logger.info("TempController.bathRegister 第:{" + (i + 1) + "}个用户注册失败");
                continue;
            }
            
            if (!isMobileNO(userInfo.getUserName())) {
                logger.info("TempController.bathRegister 用户信息:{" + userInfo.getUserName() + "}手机号格式不正确");
                continue;
            }

            UserLoginPo loginPo = loginAipService.getLoginInfoByName(userInfo.getUserName());
            if (null != loginPo) {
                logger.info("TempController.bathRegPerson返回结果:{" + userInfo.getUserName() + "}已经存在");
                logger.info("TempController.bathRegPerson 第:{" + (i + 1) + "}个用户注册失败");
                continue;
            }

            // 补齐信息
            userInfo.setOrgId(UserPresetConstants.ORG_ID);
            userInfo.setProId(UserPresetConstants.PRO_ID);
            userInfo.setIdCardType(AccountConstants.CERTIFICATE_TYPE_IDENTITY);

            // 实名认证
            if (null==vilidate || vilidate.equals("1")) {
                ResultVo vo = UserRop.identityAuth(userInfo);

                if (!vo.isSuccess()) {
                    logger.info("TempController.bathRegPerson返回结果:{" + userInfo.getUserName() + "}实名认证不通过");
                    logger.info("TempController.bathRegPerson 第:{" + (i + 1) + "}个用户注册失败");
                    continue;
                }
            }

            // 开户操作
            String operType = AccountConstants.ACCOUNT_OPEN_OPERTYPE_SAVE; // 操作类型（1：新增，2：修改）

            String userId = redisIdGeneratorExt.getRadomUserId();

            userInfo.setUserId(userId); // 系统生成

            ResultVo openVo = AccountRop.openPersonAccount(userInfo, operType);

            if (!openVo.isSuccess()) {

                String msg = null != openVo.getMsg() && !"".equals(openVo.getMsg()) ? openVo.getMsg()
                        : ErrorCodeEnum.OPEN_ACCOUNT.getValue();

                logger.info("TempController.bathRegPerson返回结果:{" + userInfo.getUserName() + "}开户失败:{失败信息：" + msg + "}");
                logger.info("TempController.bathRegPerson 第:{" + (i + 1) + "}个用户注册失败");
                continue;
            }

            // 入库
            UserInfoPo po = new UserInfoPo();
            po.setUserId(userId);
            po.setIdCardType(AccountConstants.CERTIFICATE_TYPE_IDENTITY); // 身份证类型
            po.setIdCard(userInfo.getIdCard());
            po.setUserName(userInfo.getUserName());
            po.setUserRealName(userInfo.getUserRealName());
            po.setSalt(salt);
            po.setPwd(pwd);
            po.setUserType(Integer.parseInt(UserInfoConstants.ACCOUNT_PROPERTY_PERSONAGE));
            if (userInfoApiService.register(po)) {
                num++;
                sussessList.add((i + 1) + "----" + po.getUserName());
                logger.info("UserController.bathRegPerson:{" + po.getUserName() + "}注册成功");
                logger.info("TempController.bathRegPerson 第:{" + (i + 1) + "}个用户注册成功");
                SendSMS.sendSMS(po.getUserName(), SMSConstants.SMS_TEMPLET_DOWNLOAD,
                        getSmsMap(po.getUserName(), downUrl)); // 发送短信通知
            } else {
                logger.info("TempController.bathRegPerson 第:{" + (i + 1) + "}个用户注册失败");
            }

        }
        logger.info("TempController.bathRegPerson 注册成功列表：" + JsonUtil.bean2JsonStr(sussessList));
        result.setSuccess(true);
        result.addResult("num", num);
        return result;
    }

    @ResponseBody
    @RequestMapping("/bathRegCompany")
    @SystemControllerLog(description = "注册")
    public ActionResult bathRegCompany(String key) throws Exception {

        ActionResult result = new ActionResult();
        if (null == key || !key.equals(send_key)) {
            result.setCode("5555");
            result.setMsg("没有权限操作");
            return result;
        }
        File file = new File(filePathCompany);
        if (!file.exists()) {
            result.setCode("6666");
            result.setMsg("找不到：{" + filePathCompany + "}文件");
            return result;
        }

        Integer num = 0;
        List<CompanyPo> list = UserExcelRead.getListCompany(file);
        
        List<String> sussessList = new ArrayList<>();
        
        if (null == list || list.size() == 0) {
            result.setCode("3434");
            result.setMsg("未找到注册的用户信息");
        }

        logger.info("TempController.bathRegCompany 注册个数:{" + list.size() + "}");

        for (int i = 0; i < list.size(); i++) {
            CompanyPo companyPo = list.get(i);
            logger.info("TempController.bathRegCompany 注册第:{" + i + 1 + "}个用户");
            logger.info("TempController.bathRegCompany 用户信息:{" + JsonUtil.bean2JsonStr(companyPo) + "}");

            if (null == companyPo.getLoginName() || null == companyPo.getCompanyName()
                    || null == companyPo.getBusLince() || null == companyPo.getUserName()
                    || null == companyPo.getLoginName()) {
                logger.info("TempController.bathRegCompany 用户信息:{" + JsonUtil.bean2JsonStr(companyPo) + "}不完整");
                continue;
            }

            if (!isMobileNO(companyPo.getLoginName())) {
                logger.info("TempController.bathRegCompany 用户信息:{" + companyPo.getLoginName() + "}手机号格式不正确");
                continue;
            }

            UserLoginPo loginPo = loginAipService.getLoginInfoByName(companyPo.getLoginName());
            if (null != loginPo) {
                logger.info("TempController.bathRegCompany返回结果:{" + companyPo.getLoginName() + "}已经存在");
                continue;
            }

            // 补齐信息
            companyPo.setConstId(UserPresetConstants.ORG_ID);
            companyPo.setProductId(UserPresetConstants.PRO_ID);
            companyPo.setUserType(UserInfoConstants.USER_TYPE_MERCHANT);

            // 开户操作
            String operType = AccountConstants.ACCOUNT_OPEN_OPERTYPE_SAVE; // 操作类型（1：新增，2：修改）

            String userId = redisIdGeneratorExt.getRadomUserId();

            companyPo.setUserId(userId); // 系统生成

            ResultVo openVo = accountDubbo.openCompanyAccount(companyPo);

            if (!openVo.isSuccess()) {

                String msg = null != openVo.getMsg() && !"".equals(openVo.getMsg()) ? openVo.getMsg()
                        : ErrorCodeEnum.OPEN_ACCOUNT.getValue();

                logger.info(
                        "TempController.bathRegCompany返回结果:{" + companyPo.getUserName() + "}开户失败:{失败信息：" + msg + "}");
                continue;
            }

            // 入库

           
            companyPo.setUserId(userId);          
            companyPo.setSalt(salt);
            companyPo.setPwd(pwd);
            companyPo.setLoginUserType(Integer.parseInt(UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE)); // 企业
            if (userInfoApiService.registerCompany(companyPo)) {
                num++;
              
                sussessList.add((i + 1) + "----" + companyPo.getLoginName());
                logger.info("UserController.bathRegCompany:{" + companyPo.getLoginName() + "}注册成功");
                logger.info("TempController.bathRegCompany 第:{" + (i + 1) + "}个用户注册成功");
                SendSMS.sendSMS(companyPo.getLoginName(), SMSConstants.SMS_TEMPLET_DOWNLOAD,
                        getSmsMap(companyPo.getLoginName(), downUrl)); // 发送短信通知
            } else {
                logger.info("UserController.bathRegCompany:{" + companyPo.getLoginName() + "}注册失败");
            }

        }
        logger.info("TempController.bathRegPerson 注册成功列表：" + JsonUtil.bean2JsonStr(sussessList));
        result.setSuccess(true);
        result.addResult("num", num);
        return result;
    }

    public static boolean isMobileNO(String mobiles) {

        return mobiles.length() == 11;

    }

    private Map<String, String> getSmsMap(String tel, String url) {
        Map<String, String> map = Maps.newHashMap();
        map.put("telNum", tel);
        map.put("download", url);
        return map;
    }

    public static void main(String[] args) throws IOException {

        System.out.println(isMobileNO("136813960451"));

    }

}

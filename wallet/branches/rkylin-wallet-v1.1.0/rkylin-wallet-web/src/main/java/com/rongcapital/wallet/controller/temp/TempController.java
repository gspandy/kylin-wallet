package com.rongcapital.wallet.controller.temp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Rop.api.internal.cache.ValidateUtil;
import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.rongcapital.wallet.api.po.CompanyPo;
import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.po.UserWhitePo;
import com.rongcapital.wallet.api.service.LoginAipService;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.constants.AccountConstants;
import com.rongcapital.wallet.constants.CardConstants;
import com.rongcapital.wallet.constants.ErrorCodeEnum;
import com.rongcapital.wallet.constants.SMSConstants;
import com.rongcapital.wallet.constants.Settings;
import com.rongcapital.wallet.constants.UserInfoConstants;
import com.rongcapital.wallet.constants.UserPresetConstants;
import com.rongcapital.wallet.constants.WhiteConstants;
import com.rongcapital.wallet.dubbo.AccountDubbo;
import com.rongcapital.wallet.log.SystemControllerLog;
import com.rongcapital.wallet.rop.AccountRop;
import com.rongcapital.wallet.rop.BankCardRop;
import com.rongcapital.wallet.rop.SendSMS;
import com.rongcapital.wallet.rop.UserRop;
import com.rongcapital.wallet.threads.SendPoolService;
import com.rongcapital.wallet.util.RedisIdGeneratorExt;
import com.rongcapital.wallet.util.UserExcelRead;
import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.util.validate.ValidateUtils;
import com.rongcapital.wallet.vo.ActionResult;
import com.rongcapital.wallet.vo.BankInfo;
import com.rongcapital.wallet.vo.CardInfoVo;
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

    private String filePathPerson = Settings.getInstance().getFilePathPerson(); // 个人开户

    private String filePathCompany = Settings.getInstance().getFilePathCompany(); // 企业开户（绑上，设置白明单)

    private String filePathWhite = Settings.getInstance().getFilePathWhite(); // 设置白名单关系

    private String filePathCompanyWhite = Settings.getInstance().getFilePathCompanyWhite(); // 白名单用户

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
            if (null == vilidate || vilidate.equals("1")) {
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

    /**
     * 企业入库,绑卡，设置白名单等 Discription:
     * 
     * @param key
     * @return
     * @throws Exception ActionResult
     * @author libi
     * @since 2017年1月6日
     */
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
        List<CompanyPo> list = new ArrayList<>();// 企业注册信息
        Map<String, String[]> mapWhite = new HashMap<>(); // 白名单信息
        Map<String, String> telMap = new HashMap<>(); // 法人手机号;

        Map<String, Integer> mapSuccess = new HashMap<>(); // 注册，绑上，白名单成功与否

        UserExcelRead.getListCompany(file, list, mapWhite, telMap);

        logger.info("TempController.bathRegCompany 解析excel文件结果如下：");

        logger.info("企业注册信息:{" + JsonUtil.bean2JsonStr(list) + "}");

        logger.info("白名单信息:{" + JsonUtil.bean2JsonStr(mapWhite) + "}");

        logger.info("企业授权人手机号:{" + JsonUtil.bean2JsonStr(mapWhite) + "}");

        List<String> sussessList = new ArrayList<>(); // 反回注册成功的企业信息

        Map<String, String> mapUserId = new HashMap<>(); // 存放企业对应的用户ID

        if (list.size() == 0) {
            result.setCode("3434");
            result.setMsg("未找到注册的用户信息");
        }

        logger.info("注册企业个数:{" + list.size() + "}");

        for (int i = 0; i < list.size(); i++) {
            CompanyPo companyPo = list.get(i);
            logger.info("正在 注册第:{" + (i + 1) + "}个用户");

            logger.info("企业信息如下:{" + JsonUtil.bean2JsonStr(companyPo) + "}");

            if (null == companyPo.getLoginName() || null == companyPo.getCompanyName()
                    || null == companyPo.getBusLince() || null == companyPo.getUserName()
                    || null == companyPo.getCorporateTel()) {
                logger.info("校验企业信息不完整，校验参数有([companyName,BusLince,UserName,CorporateTel");
                continue;
            }

            logger.info("判断用户是否存在?");
            UserLoginPo loginPo = loginAipService.getLoginInfoByName(companyPo.getLoginName());

            if (null != loginPo) {
                logger.info("TempController.bathRegCompany返回结果:{" + companyPo.getLoginName() + "}已经存在");
                continue;
            }

            // 补齐信息
            companyPo.setConstId(UserPresetConstants.ORG_ID);
            companyPo.setProductId(UserPresetConstants.PRO_ID);
            companyPo.setUserType(UserInfoConstants.USER_TYPE_MERCHANT);

            String userId = redisIdGeneratorExt.getRadomUserId();

            logger.info("生成的用户ID：" + userId);

            companyPo.setUserId(userId); // 系统生成

            ResultVo openVo = null;
            try {
                logger.info("开户操作入参:" + JsonUtil.bean2JsonStr(companyPo));
                openVo = accountDubbo.openCompanyAccount(companyPo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!openVo.isSuccess()) {

                String msg = null != openVo.getMsg() && !"".equals(openVo.getMsg()) ? openVo.getMsg()
                        : ErrorCodeEnum.OPEN_ACCOUNT.getValue();

                logger.info("开户失败:{失败信息：" + msg + "}");
                continue;
            }

            // 入库

            companyPo.setUserId(userId);
            companyPo.setSalt(salt);
            companyPo.setPwd(pwd);
            companyPo.setLoginUserType(Integer.parseInt(UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE)); // 企业
            logger.info("入库操作入参:" + JsonUtil.bean2JsonStr(companyPo));
            if (userInfoApiService.registerCompany(companyPo)) {
                num++;
                sussessList.add((i + 1) + "----" + companyPo.getLoginName());
                mapUserId.put(companyPo.getLoginName(), userId); // 存放注册成功的userId

                mapSuccess.put(companyPo.getCompanyName(), 1);
                logger.info("{" + companyPo.getLoginName() + "}注册成功");
                logger.info("第:{" + (i + 1) + "}个用户注册成功");

            } else {
                logger.info("UserController.bathRegCompany:{" + companyPo.getLoginName() + "}注册失败");
            }

            logger.info("进入绑卡操作");
            bindCard(companyPo, userId, mapSuccess);

        }

        // 白名单操作
        if (mapWhite.size() > 0) {
            saveWhite(mapWhite, mapUserId, WhiteConstants.APP_ID_WAN, mapSuccess);
        }

        // 发消息
        // for (String keyName : mapSuccess.keySet()) {
        // if (mapSuccess.get(keyName) == 3) {
        // SendSMS.sendSMS(keyName, SMSConstants.SMS_TEMPLET_DOWNLOAD, getSmsMap(telMap.get(keyName), downUrl)); //
        // 发送短信通知
        //
        // }
        //
        // }

        logger.info("TempController.bathRegCompany 注册成功列表：" + JsonUtil.bean2JsonStr(sussessList));
        logger.info("TempController.bathRegCompany 操作成功列表：" + JsonUtil.bean2JsonStr(mapSuccess));
        result.setSuccess(true);
        result.addResult("num", num);
        return result;
    }

    /**
     * 开白名单用户 Discription:
     * 
     * @param key
     * @return
     * @throws Exception ActionResult
     * @author libi
     * @since 2017年1月6日
     */
    @ResponseBody
    @RequestMapping("/bathRegCompanyWhite")
    @SystemControllerLog(description = "注册")
    public ActionResult bathRegCompanyWhite(String key) throws Exception {

        ActionResult result = new ActionResult();
        if (null == key || !key.equals(send_key)) {
            result.setCode("5555");
            result.setMsg("没有权限操作");
            return result;
        }

        File file = new File(filePathCompanyWhite);

        if (!file.exists()) {
            result.setCode("6666");
            result.setMsg("找不到：{" + filePathCompanyWhite + "}文件");
            return result;
        }

        Integer num = 0;
        List<CompanyPo> list = new ArrayList<>();// 企业注册信息

        UserExcelRead.getListCompanyWhite(file, list);

        logger.info("TempController.bathRegCompanyWhite 解析excel文件结果如下：");

        logger.info("企业注册信息:{" + JsonUtil.bean2JsonStr(list) + "}");

        List<String> sussessList = new ArrayList<>(); // 反回注册成功的企业信息

        if (list.size() == 0) {
            result.setCode("3434");
            result.setMsg("未找到注册的用户信息");
        }

        logger.info("注册企业个数:{" + list.size() + "}");

        for (int i = 0; i < list.size(); i++) {
            CompanyPo companyPo = list.get(i);
            logger.info("正在 注册第:{" + (i + 1) + "}个用户");

            logger.info("企业信息如下:{" + JsonUtil.bean2JsonStr(companyPo) + "}");

            if (ValidateUtils.isEmpty(companyPo.getCompanyName()) || ValidateUtils.isEmpty(companyPo.getBusLince())
                    || ValidateUtils.isEmpty(companyPo.getBankName()) || ValidateUtils.isEmpty(companyPo.getBankCode())
                    || ValidateUtils.isEmpty(companyPo.getComBankNum())) {
                logger.info("校验企业信息不完整，校验参数有([companyName,BusLince,UserName,CorporateTel");
                continue;
            }

            logger.info("判断用户是否存在?");
            UserLoginPo loginPo = loginAipService.getLoginInfoByName(companyPo.getLoginName());

            if (null != loginPo) {
                logger.info("TempController.bathRegCompany返回结果:{" + companyPo.getLoginName() + "}已经存在");
                continue;
            }

            // 补齐信息
            companyPo.setConstId(UserPresetConstants.ORG_ID);
            companyPo.setProductId(UserPresetConstants.PRO_ID);
            companyPo.setUserType(UserInfoConstants.USER_TYPE_MERCHANT);

            String userId = redisIdGeneratorExt.getRadomUserId();

            logger.info("生成的用户ID：" + userId);

            companyPo.setUserId(userId); // 系统生成

            ResultVo openVo = null;
            try {
                logger.info("开户操作入参:" + JsonUtil.bean2JsonStr(companyPo));
                openVo = accountDubbo.openCompanyAccount(companyPo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!openVo.isSuccess()) {

                String msg = null != openVo.getMsg() && !"".equals(openVo.getMsg()) ? openVo.getMsg()
                        : ErrorCodeEnum.OPEN_ACCOUNT.getValue();

                logger.info("开户失败:{失败信息：" + msg + "}");
                continue;
            }

            logger.info("进入绑卡操作");
            ResultVo vo = bindCardCompanyWhite(companyPo, userId);
            if (!vo.isSuccess()) {
                result.setCode("0000");
                result.setMsg(vo.getMsg());
                return result;
            }
            
            // 入库

            companyPo.setUserId(userId);
            companyPo.setSalt(salt);
            companyPo.setPwd(pwd);
            companyPo.setLoginUserType(Integer.parseInt(UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE)); // 企业
            logger.info("入库操作入参:" + JsonUtil.bean2JsonStr(companyPo));
            if (userInfoApiService.registerCompany(companyPo)) {
                num++;
                logger.info("{" + companyPo.getLoginName() + "}注册成功");
                logger.info("第:{" + (i + 1) + "}个用户注册成功");

            } else {
                logger.info("UserController.bathRegCompany:{" + companyPo.getLoginName() + "}注册失败");
            }

        }

        logger.info("TempController.bathRegPerson 注册成功列表：" + JsonUtil.bean2JsonStr(sussessList));
        result.setSuccess(true);
        result.addResult("num", num);
        return result;
    }

    /**
     * 保存白名单信息 Discription:
     * 
     * @param list
     * @param useIdMap
     * @param appId
     * @return boolean
     * @author libi
     * @since 2016年12月14日
     */
    private void saveWhite(Map<String, String[]> mapWhite, Map<String, String> userIdMap, String applyId,
            Map<String, Integer> mapSuccess) throws Exception {

        logger.info("TempController.bathRegPerson 设置白名单列表：{" + JsonUtil.bean2JsonStr(userIdMap) + "}");
        List<String> sussessList = new ArrayList<>();

        for (String key : mapWhite.keySet()) {

            String[] values = mapWhite.get(key); // 对应的白名单信息

            if (null == values || values.length == 0) {
                logger.info("TempController.bathRegPerson 设置白名单：" + key + ":对应的values为空");
                continue;
            }

            /*------构建入库数据-----------*/
            List<UserWhitePo> listWhite = new ArrayList<>();

            List<String> whiteErrAry = new ArrayList<>();

            String mainUserId = getWhiteUserId(key, userIdMap);

            if (null == mainUserId) {
                logger.info("TempController.bathRegPerson 设置白名单：" + key + ":取主企业userId失败");
                continue;
            }

            for (String v : values) {
                UserWhitePo po = new UserWhitePo();
                po.setUserId(mainUserId);
                po.setApplyId(applyId); // 应用ID

                String refUserId = getWhiteUserId(v, userIdMap);
                if (null == refUserId) {
                    logger.info("TempController.bathRegPerson 设置白名单：" + v + ":取企业userId失败");
                    whiteErrAry.add(v);
                    continue;
                }
                po.setRefId(refUserId);
                po.setRefName(v); // 关联名称
                listWhite.add(po);
            }

            // 所有关联信息都正常
            if (whiteErrAry.size() > 0) {
                logger.info("TempController.bathRegPerson 设置白名单：" + key + ":失败,原因，" + "{"
                        + JsonUtil.bean2JsonStr(whiteErrAry) + "}信息错误");
            }

            logger.info(
                    "TempController.bathWhite 正在设置{" + key + "}的白名单信息" + "{" + JsonUtil.bean2JsonStr(listWhite) + "}");

            List<UserWhitePo> newRef = new ArrayList<>();
            for (UserWhitePo po : listWhite) {
                /*-----------判断是否已经存在关联关系----------------*/
                if (userInfoApiService.whiteExists(po)) {
                    logger.info("TempController.bathWhite关联信息已经存在:{" + JsonUtil.bean2JsonStr(listWhite) + "}");
                    continue;
                } else {
                    newRef.add(po);
                }

            }
            if (newRef.size() > 0) {
                userInfoApiService.bathWhiteList(newRef);
            }
            mapSuccess.put(key, mapSuccess.containsKey(key) ? mapSuccess.get(key) : 1); // 成功
            sussessList.add(key);
        }

        logger.info("TempController.bathWhite设置白名单成功列表:{" + JsonUtil.bean2JsonStr(sussessList) + "}");
    }

    private String getWhiteUserId(String key, Map<String, String> userIdMap) throws Exception {

        if (userIdMap.containsKey(key)) {
            return userIdMap.get(key);
        }
        // 从库里取
        UserLoginPo loginPo = loginAipService.getLoginInfoByName(key); // 库里取
        if (null == loginPo) {
            logger.info("TempController.bathWhite取userId失败，原因:{" + key + "}企业不存在");
            return null;
        }

        UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());

        return userInfo.getUserId();

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

    /**
     * 白名单用户绑卡 Discription:
     * 
     * @param companyPo
     * @param userId
     * @param mapSuccess
     * @throws Exception void
     * @author libi
     * @since 2017年1月6日
     */
    private ResultVo bindCardCompanyWhite(CompanyPo companyPo, String userId) throws Exception {

        CardInfoVo cardInfo = new CardInfoVo();
        UserInfoVo userVo = new UserInfoVo();
        userVo.setOrgId(UserPresetConstants.ORG_ID); // 机构码
        userVo.setProId(UserPresetConstants.PRO_ID); // 产品号
        userVo.setUserId(userId); // 用户ID

        userVo.setUserType(UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE); // 用户类型(1：商户，2：普通用户)

        cardInfo.setCurrency(CardConstants.CARD_CURRENCY_CNY);// 币种（CNY）

        logger.info("{" + companyPo.getCompanyName() + "}开始绑对公卡:" + companyPo.getComBankNum());

        userVo.setIdCardType(String.valueOf("x")); // 开户证件类型0：身份证,1: 户口簿，2：护照 x 其他
        userVo.setIdCard(companyPo.getBusLince()); // 证件号(营业执照)

        cardInfo.setAccountProperty(CardConstants.CARD_PROPERTY_PUBLIC);// 账户属性（1：对公，2：对私）
        cardInfo.setPurpose(CardConstants.CARD_PURPOSE_TX); // 提现卡
        cardInfo.setBankCardNum(companyPo.getComBankNum()); // 卡号
        cardInfo.setCardType(CardConstants.CARD_TYPE_BANK); // 卡类型,默认为银行
        cardInfo.setUserName(companyPo.getCompanyName()); // 公司名

        cardInfo.setBankName(companyPo.getBankName()); // 开户行总行名称
        cardInfo.setBankCode(companyPo.getBankCode()); // 银行代码总行

        logger.info("{" + companyPo.getCompanyName() + "}绑对公卡入参  userVo:" + JsonUtil.bean2JsonStr(userVo));
        logger.info("{" + companyPo.getCompanyName() + "}绑对公卡入参  cardInfo:" + JsonUtil.bean2JsonStr(cardInfo));
        ResultVo resultVo = BankCardRop.cardBind(userVo, cardInfo);
        if (resultVo.isSuccess()) {

            logger.info("{" + companyPo.getCompanyName() + "}绑对公卡:" + companyPo.getComBankNum() + "成功");
        } else {
            logger.info("{" + companyPo.getCompanyName() + "}绑对公卡失败:" + companyPo.getComBankNum() + "{"
                    + resultVo.getMsg() + "}");

        }
        return resultVo;
    }

    /**
     * 绑卡
     *
     * @return
     */
    private void bindCard(CompanyPo companyPo, String userId, Map<String, Integer> mapSuccess) throws Exception {

        boolean isSus = false;
        CardInfoVo cardInfo = new CardInfoVo();
        UserInfoVo userVo = new UserInfoVo();

        // 公共信息
        userVo.setOrgId(UserPresetConstants.ORG_ID); // 机构码
        userVo.setProId(UserPresetConstants.PRO_ID); // 产品号
        userVo.setUserId(userId); // 用户ID

        userVo.setUserType(UserInfoConstants.ACCOUNT_PROPERTY_ENTERPRISE); // 用户类型(1：商户，2：普通用户)

        cardInfo.setCurrency(CardConstants.CARD_CURRENCY_CNY);// 币种（CNY）

        // 绑定对公卡
        if (null != companyPo.getComBankNum()) {
            logger.info("{" + companyPo.getCompanyName() + "}开始绑对公卡:" + companyPo.getComBankNum());

            userVo.setIdCardType(String.valueOf("x")); // 开户证件类型0：身份证,1: 户口簿，2：护照 x 其他
            userVo.setIdCard(companyPo.getBusLince()); // 证件号(营业执照)

            cardInfo.setAccountProperty(CardConstants.CARD_PROPERTY_PUBLIC);// 账户属性（1：对公，2：对私）
            cardInfo.setPurpose(CardConstants.CARD_PURPOSE_TX); // 提现卡
            cardInfo.setBankCardNum(companyPo.getComBankNum()); // 卡号
            cardInfo.setCardType(CardConstants.CARD_TYPE_BANK); // 卡类型,默认为银行
            cardInfo.setUserName(companyPo.getCompanyName()); // 公司名

            if (null != companyPo.getBankName() && null != companyPo.getBankCode()) {
                cardInfo.setBankName(companyPo.getBankName()); // 开户行总行名称
                cardInfo.setBankCode(companyPo.getBankCode()); // 银行代码总行

                logger.info("{" + companyPo.getCompanyName() + "}绑对公卡入参  userVo:" + JsonUtil.bean2JsonStr(userVo));
                logger.info("{" + companyPo.getCompanyName() + "}绑对公卡入参  cardInfo:" + JsonUtil.bean2JsonStr(cardInfo));
                ResultVo resultVo = BankCardRop.cardBind(userVo, cardInfo);
                if (resultVo.isSuccess()) {
                    isSus = true;
                    logger.info("{" + companyPo.getCompanyName() + "}绑对公卡:" + companyPo.getComBankNum() + "成功");
                } else {
                    logger.info("{" + companyPo.getCompanyName() + "}绑对公卡失败:" + companyPo.getComBankNum() + "{"
                            + resultVo.getMsg() + "}");

                }
            } else {
                logger.info("TempController.bathRegCompany{" + companyPo.getCompanyName() + "}绑对公卡:"
                        + companyPo.getComBankNum() + "{总行名称或总行代码为空}");

            }
        }

        // 绑定对私卡
        if (null != companyPo.getBindBankNum() && null != companyPo.getBindIdCard()
                && null != companyPo.getBindRealName()) {
            logger.info("{" + companyPo.getCompanyName() + "}开始绑对公私卡:" + companyPo.getBindBankNum());

            userVo.setIdCardType(String.valueOf(CardConstants.CARD_TYPE_CERTIFICATE_VALIDATE)); // 开户证件类型0：身份证,1:
                                                                                                // 户口簿，2：护照
            userVo.setIdCard(companyPo.getBindIdCard()); // 证件号

            cardInfo.setAccountProperty(CardConstants.CARD_PROPERTY_PRIVATE);// 账户属性（1：对公，2：对私）
            cardInfo.setPurpose(CardConstants.CARD_PURPOSE_OTHER); // 其他

            // 实名认证
            // UserInfoVo authVo=new UserInfoVo();
            // authVo.setOrgId(userVo.getOrgId());
            // authVo.setUserRealName(companyPo.getBindRealName());
            // authVo.setIdCardType("0");
            // authVo.setIdCard(companyPo.getBindIdCard());
            //
            // logger.info("实名认证入参:"+JsonUtil.bean2JsonStr(authVo));
            //
            // ResultVo vo = UserRop.identityAuth(authVo);
            //
            // if (!vo.isSuccess()) {
            //
            // logger.info("{" + companyPo.getCompanyName() + "}绑对私卡 绑卡人实名认证失败:"
            // + companyPo.getBindRealName() +companyPo.getBindIdCard()+ "{" + ErrorCodeEnum.BANK_CARD_ERROR.getValue()
            // + "}");
            // return;
            // }

            // BankInfo bankInfo = UserRop.cardDetail(userVo, companyPo.getBindBankNum());
            // if (null != bankInfo) {
            // cardInfo.setBankCardNum(companyPo.getBindBankNum()); // 卡号
            // cardInfo.setCardType(CardConstants.CARD_TYPE_BANK); // 卡类型,默认为银行
            // if (CardConstants.CARD_TYPE_ROP_CREDIT_CODE.equals(bankInfo.getCardType())) {
            // cardInfo.setCardType(CardConstants.CARD_TYPE_CREDIT); // 信用卡
            // }
            // cardInfo.setBankName(bankInfo.getBankName()); // 开户行总行名称
            // cardInfo.setBankCode(bankInfo.getBankCode()); // 银行代码总行
            // cardInfo.setUserName(companyPo.getBindRealName()); // 绑上人姓名
            //
            // logger.info("{" + companyPo.getCompanyName() + "}绑对公私卡,调用绑上接口入参 userVo:"+JsonUtil.bean2JsonStr(userVo));
            // logger.info("{" + companyPo.getCompanyName() + "}绑对公私卡,调用绑上接口入参
            // cardInfo:"+JsonUtil.bean2JsonStr(cardInfo));
            //
            // ResultVo resultVo = BankCardRop.cardBind(userVo, cardInfo);
            // if (resultVo.isSuccess()) {
            // isSus = true;
            // logger.info("{" + companyPo.getCompanyName() + "}绑对私卡:"
            // + companyPo.getBindBankNum() + "{success}");
            // } else {
            // isSus = false;
            // logger.info("TempController.bathRegCompany{" + companyPo.getCompanyName() + "}绑对私卡:"
            // + companyPo.getBindBankNum() + "{" + resultVo.getMsg() + "}");
            // }
            // } else {
            // isSus = false;
            // logger.info("{" + companyPo.getCompanyName() + "}绑对私卡:"
            // + companyPo.getBindBankNum() + "{" + ErrorCodeEnum.BANK_CARD_ERROR.getValue() + "}");
            //
            // }

            cardInfo.setBankCardNum(companyPo.getBindBankNum()); // 卡号
            cardInfo.setCardType(CardConstants.CARD_TYPE_BANK); // 卡类型,默认为银行

            cardInfo.setBankName(companyPo.getBindBankName()); // 开户行总行名称
            cardInfo.setBankCode(companyPo.getBindBankCode()); // 银行代码总行
            cardInfo.setUserName(companyPo.getBindRealName()); // 绑上人姓名

            logger.info("{" + companyPo.getCompanyName() + "}绑对公私卡,调用绑上接口入参 userVo:" + JsonUtil.bean2JsonStr(userVo));
            logger.info(
                    "{" + companyPo.getCompanyName() + "}绑对公私卡,调用绑上接口入参 cardInfo:" + JsonUtil.bean2JsonStr(cardInfo));

            ResultVo resultVo = BankCardRop.cardBind(userVo, cardInfo);
            if (resultVo.isSuccess()) {
                isSus = true;
                logger.info("{" + companyPo.getCompanyName() + "}绑对私卡:" + companyPo.getBindBankNum() + "{success}");
            } else {
                isSus = false;
                logger.info("TempController.bathRegCompany{" + companyPo.getCompanyName() + "}绑对私卡:"
                        + companyPo.getBindBankNum() + "{" + resultVo.getMsg() + "}");
            }
        } else {
            isSus = false;
            logger.info("{" + companyPo.getCompanyName() + "}绑对私卡:" + companyPo.getBindBankNum() + "{"
                    + ErrorCodeEnum.BANK_CARD_ERROR.getValue() + "}");

        }

        // 绑卡成功
        if (isSus) {
            logger.info("{" + companyPo.getCompanyName() + "}绑卡成功，包括对私卡与对公卡");
            mapSuccess.put(companyPo.getCompanyName(), mapSuccess.containsKey(companyPo.getCompanyName())
                    ? mapSuccess.get(companyPo.getCompanyName()) : 1);
        }
    }

    /**
     * 导入白名单 Discription:
     * 
     * @param key
     * @param vilidate
     * @return
     * @throws Exception ActionResult
     * @author libi
     * @since 2016年12月13日
     */
    @ResponseBody
    @RequestMapping("/bathWhite")
    @SystemControllerLog(description = "批量导入白名单用户")
    public ActionResult bathWhite(String key, String appId) throws Exception {

        ActionResult result = new ActionResult();
        if (null == key || !key.equals(send_key)) {
            result.setCode("5555");
            result.setMsg("没有权限操作");
            return result;
        }
        File file = new File(filePathWhite);
        if (!file.exists()) {
            result.setCode("6666");
            result.setMsg("找不到：{" + filePathWhite + "}文件");
            return result;
        }

        Integer num = 0;
        List<UserWhitePo> list = UserExcelRead.getWhiteList(file);
        List<String> sussessList = new ArrayList<>();

        if (null == list || list.size() == 0) {
            result.setCode("3434");
            result.setMsg("未找到任何信息");
        }

        logger.info("TempController.bathWhite 读取条数:{" + list.size() + "}");

        for (int i = 0; i < list.size(); i++) {
            UserWhitePo po = list.get(i);
            po.setApplyId(appId); // 应用ID

            logger.info("TempController.bathWhite 正在操作第:{" + (i + 1) + "}条信息");
            logger.info("TempController.bathWhite 关联信息:{" + JsonUtil.bean2JsonStr(po) + "}");

            if (null == po.getComName()) {
                logger.info("TempController.bathWhite 第:{" + (i + 1) + "}个关联信息不完整");
                logger.info("TempController.bathWhite 第:{" + (i + 1) + "}个关联失败");
                continue;
            }

            UserLoginPo loginPo = loginAipService.getLoginInfoByName(po.getComName());

            if (null == loginPo) {
                logger.info("TempController.bathWhite返回结果:{" + po.getComName() + "}企业不存在");
                continue;
            }

            UserInfoPo userInfo = userInfoApiService.getUserInfoById(loginPo.getUserInfoId());

            po.setUserId(userInfo.getUserId());
            logger.info("TempController.bathWhite:{" + po.getComName() + "}用户ID为：" + userInfo.getUserId());

            String[] listRef = po.getRefComNameArry();

            if (null != listRef && listRef.length > 0) {
                List<UserWhitePo> newRef = new ArrayList<>();
                for (String refName : listRef) {
                    if (refName.equals(po.getComName())) {
                        logger.info("TempController.bathWhite返回结果:{" + refName + "}不能自己关联自己");
                        continue;
                    }
                    UserLoginPo loginPoR = loginAipService.getLoginInfoByName(refName);
                    if (null == loginPoR) {
                        logger.info("TempController.bathWhite返回结果:{" + refName + "}企业不存在");
                        continue;
                    }
                    UserInfoPo userInfoA = userInfoApiService.getUserInfoById(loginPoR.getUserInfoId());
                    po.setRefId(userInfoA.getUserId());
                    po.setRefName(refName); // 关联名称

                    /*-----------判断是否已经存在关联关系----------------*/
                    if (userInfoApiService.whiteExists(po)) {
                        logger.info("TempController.bathWhite:{" + JsonUtil.bean2JsonStr(po) + "}关联关系已经存在");
                        continue;
                    } else {
                        newRef.add(po);
                    }

                }

                if (newRef.size() > 0) {

                    userInfoApiService.bathWhiteList(newRef);
                    num++;
                    sussessList.add(po.getComName() + ":" + JsonUtil.bean2JsonStr(newRef));
                }
            }
        }
        logger.info("TempController.bathWhite 关联列表：" + JsonUtil.bean2JsonStr(sussessList));
        result.setSuccess(true);
        result.addResult("num", num);
        return result;
    }

}

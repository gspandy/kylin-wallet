package com.rongcapital.wallet.constants;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.rongcapital.wallet.util.json.JsonUtil;
import com.rongcapital.wallet.vo.BankTypeVo;

public enum ErrorCodeEnum {
    
    /*-----用户模块--------*/
    LOGIN_NO("10000", "请先登陆"), 
    LOGIN_TIMEOUT("10001", "登陆信息过期"), 
    LOGIN_PWD_ERROR("10002", "用户名或密码错误"),
    USER_STATUS_ERR("10003","用户状态不正常"),
    LOGIN_AUTH("10004","暂时只提供个人用户登陆"),
    REGISTER_EXISTS("10005","用户名已经存在"),
    REGISTER_NOTEXISTS("10006","用户名不存在"),
    IDENTITYAUTH("10007", "身份证信息验证失败"), 
    OPEN_ACCOUNT("10008", "开户失败"), 
    REGISTER_ERR("10009","注册失败"),
    TOKEN_NOTEXISTS("10010","token不存在"),
    LOGIN_INFO_VALIDATE_ERROR("10010", "用户与身份信息不一致"), 
    /*------参数验证-----------*/
    PARM_CHECK_NULL("20000", "入参不正确，请检查必传参数"), 
    
    PARM_REGISTER_NO("29999", "暂不提供注册"), 
    /*-----------银行卡-----------*/
    BANK_CHECK("30000", "银行卡信息验证失败"),  
    BANK_CARD_ERROR("30001","未查到银行卡信息"),
    BANK_BIND_ERROR("30002","绑上失败"),
    BANKLIST_NOT_SEARCH("30003","未找到绑定的银行卡"),
    BANK_VALIDATE("30004","只能绑储蓄卡"),
    BANK_VALIDATE_ON("30005","只能绑自己的卡"),
    BANK_DEL_ERROR("30006","解绑失败"),
    BANK_TYPE_ERROR("30007","暂不支持该银行卡"),
    /*-----------密码------------*/
 
    PAYPWD_ERROR("40000", "密码验证错误!"),     
    PWD_VILIDATE_ERRO("40001","密码验证错误"),
    
    /*-----------充值-转账----提现--------*/
    CREDIT_WITHDR_ERROR("6000","信用提现失败"),
   
    
    /*--------通用----------*/
    YZMSX_ERROR("50001", "验证码错误"),
   

    SYSTEM_ERROR("90000", "接口调用异常"),
    SYSTEM_ERROR_403("403", "禁止访问"),
    SYSTEM_ERROR_404("404", "找不到请求地址"),    
    SYSTEM_ERROR_500("500", "服务器内部出错");
    private String code;
    private String value;

    private ErrorCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

  

    public String getCode() {
        return code;
    }



    public void setCode(String code) {
        this.code = code;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
//        List<BankTypeVo> list=CardConstants.BANK_LIST;
//        System.out.println(JsonUtil.bean2JsonStr(list));
        
        String a="[{\"bankCode\":\"102\",\"bankName\":\"�й�\"";
        String str = "测试字符转换 hello word"; //默认环境，已是UTF-8编码
        try {
            String strGBK = URLEncoder.encode(a, "UTF8");
            System.out.println(strGBK);
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.rongcapital.wallet.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rongcapital.wallet.vo.BankTypeVo;

public class CardConstants {

    public static final Map<String,String> BANK_MAP = new LinkedHashMap<String,String>();
    public static final List<BankTypeVo> BANK_LIST = new ArrayList<BankTypeVo>();
  
    static {
        BANK_MAP.put("102", "中国工商银行");
        BANK_MAP.put("103", "中国农业银行");
        BANK_MAP.put("104", "中国银行");
        BANK_MAP.put("105", "中国建设银行");
        BANK_MAP.put("302", "中信银行");
        BANK_MAP.put("303", "中国光大银行");
        BANK_MAP.put("304", "华夏银行");
        BANK_MAP.put("305", "中国民生银行");
        BANK_MAP.put("309", "兴业银行");
        BANK_MAP.put("307", "深圳平安银行");
        BANK_MAP.put("310", "上海浦东发展银行");
        BANK_MAP.put("313", "北京银行");
        BANK_MAP.put("313", "上海银行");
        BANK_MAP.put("308", "招商银行");
        BANK_LIST.add(new BankTypeVo("102", "中国工商银行"));
        BANK_LIST.add(new BankTypeVo("103", "中国农业银行")); //
        BANK_LIST.add(new BankTypeVo("104", "中国银行")); //
        BANK_LIST.add(new BankTypeVo("105", "中国建设银行")); //
        BANK_LIST.add(new BankTypeVo("302", "中信银行")); //
        BANK_LIST.add(new BankTypeVo("303", "中国光大银行")); //
        BANK_LIST.add(new BankTypeVo("304", "华夏银行")); //
        BANK_LIST.add(new BankTypeVo("305", "中国民生银行")); //
        BANK_LIST.add(new BankTypeVo("309", "兴业银行")); //
        BANK_LIST.add(new BankTypeVo("4105840", "深圳平安银行"));
        BANK_LIST.add(new BankTypeVo("310", "上海浦东发展银行")); //
        BANK_LIST.add(new BankTypeVo("4031000", "北京银行")); //
        BANK_LIST.add(new BankTypeVo("4012900", "上海银行")); // 上海银行
        BANK_LIST.add(new BankTypeVo("308", "招商银行")); // 招商银行
    }
    public static final String CARD_TYPE_CERTIFICATE = "1";
    public static final Integer CARD_TYPE_CERTIFICATE_VALIDATE = 0; // 身份证
    public static final String CARD_CURRENCY_CNY = "CNY"; // 币种
    public static final String CARD_PURPOSE_OTHER = "2"; // 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
    public static final String CARD_PURPOSE_TX= "3"; // 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
    
    public static final String CARD_PROPERTY_PUBLIC = "1"; // 账户属性对公
    public static final String CARD_PROPERTY_PRIVATE = "2"; // 账户属性对私
    public static final String CERTIFICATE_TYPE_IDENTITY = "0"; // 开户证件类型0：身份证,1: 户口簿，2：护照,3.军官证,
    public static final String CARD_TYPE_BANK = "00"; // 账号类型 00银行卡，01存折，02信用卡。不填默认为银行卡00。
    public static final String CARD_TYPE_CREDIT = "02"; // 账号类型 00银行卡，01存折，02信用卡。不填默认为银行卡00。
    public static final String CARD_TYPE_ROP_SAVE = "2"; // 银行卡类型 2_储蓄卡 3_信用卡
    public static final String CARD_TYPE_ROP_CREDIT = "3";
    public static final String CARD_TYPE_ROP_SAVE_VALUE = "储蓄卡"; // 银行卡类型 2_储蓄卡 3_信用卡
    public static final String CARD_TYPE_ROP_CREDIT_VALUE = "信用卡";
    public static final String CARD_TYPE_ROP_SAVE_CODE = "2"; // 银行卡类型 2_储蓄卡 3_信用卡
    public static final String CARD_TYPE_ROP_CREDIT_CODE = "3";
}

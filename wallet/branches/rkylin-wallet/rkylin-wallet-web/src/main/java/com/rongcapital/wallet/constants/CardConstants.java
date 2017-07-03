package com.rongcapital.wallet.constants;

public class CardConstants {

    public static final String CARD_TYPE_CERTIFICATE = "1";
    public static final Integer CARD_TYPE_CERTIFICATE_VALIDATE = 0; // 身份证

    public static final String CARD_CURRENCY_CNY = "CNY"; // 币种
    public static final String CARD_PURPOSE_OTHER = "2"; // 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
    public static final String CARD_PROPERTY_PRIVATE = "2"; // 账户属性（1：对公，2：对私）
    public static final String CERTIFICATE_TYPE_IDENTITY = "0"; // 开户证件类型0：身份证,1: 户口簿，2：护照,3.军官证,
    public static final String CARD_TYPE_BANK = "00"; // 账号类型 00银行卡，01存折，02信用卡。不填默认为银行卡00。
    public static final String   CARD_TYPE_ROP_SAVE="2";        //银行卡类型 2_储蓄卡 3_信用卡
    public static final String   CARD_TYPE_ROP_CREDIT="3";
    public static final String   CARD_TYPE_ROP_SAVE_VALUE="储蓄卡";        //银行卡类型 2_储蓄卡 3_信用卡
    public static final String   CARD_TYPE_ROP_CREDIT_VALUE="信用卡";
    public static final String   CARD_TYPE_ROP_SAVE_CODE="2";        //银行卡类型 2_储蓄卡 3_信用卡
    public static final String   CARD_TYPE_ROP_CREDIT_CODE="3";
}

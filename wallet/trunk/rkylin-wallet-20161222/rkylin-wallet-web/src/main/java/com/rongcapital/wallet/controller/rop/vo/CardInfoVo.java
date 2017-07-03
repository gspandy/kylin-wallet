package com.rongcapital.wallet.controller.rop.vo;

import java.util.Date;

public class CardInfoVo {

    /**
     * 卡号
     */
    private String bankCardNum;
    /**
     * 卡类型(//账号类型 00银行卡，01存折，02信用卡。不填默认为银行卡00。 )
     */
    private String cardType;
    
    /**
     * 总行名称
     */
    private String bankName;
    /**
     * 开户行支付名称
     */
    private String bankbranchname;

    /**
     * 手机号
     */
    private String tel;
    /**
     * 币种
     */
    private String currency;

    /**
     * 开户日期
     */
    private Date openDate;
    /**
     * 账户用途
     */
    private String descRiption;
    /**
     * 账户目的(1:结算卡，2：其他卡, 3：提现卡,4:结算提现一体卡)
     */
    private String purpose;

    /**
     * 账户属性（1：对公，2：对私）
     */
    private String accountProperty;

    /**
     * 证件类型
     */
    private Integer idCardType;
    /**
     * 证件号
     */
    private String idCard;
    /**
     * 开卡人的名字
     */
    private String userName;;
    /**
     * 第三方用户ID(子账户时不能为空，主账户时为空)
     */
    private String referUserId;

    /**
     * 银行代码总行
     */
    private String bankCode;

    /**
     * 开户行支行号
     */
    private String bankBranch;
    /**
     * 开户行所在省
     */
    private String bankProvince;
    /**
     * 开户行所在市
     */
    private String bankCity;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankbranchname() {
        return bankbranchname;
    }

    public void setBankbranchname(String bankbranchname) {
        this.bankbranchname = bankbranchname;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getDescRiption() {
        return descRiption;
    }

    public void setDescRiption(String descRiption) {
        this.descRiption = descRiption;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAccountProperty() {
        return accountProperty;
    }

    public void setAccountProperty(String accountProperty) {
        this.accountProperty = accountProperty;
    }

    public Integer getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(Integer idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReferUserId() {
        return referUserId;
    }

    public void setReferUserId(String referUserId) {
        this.referUserId = referUserId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

}

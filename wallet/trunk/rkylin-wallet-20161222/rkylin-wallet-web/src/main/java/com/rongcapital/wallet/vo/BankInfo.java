package com.rongcapital.wallet.vo;

import com.rongcapital.wallet.constants.CardConstants;

public class BankInfo {

    private String cardNo;
    private String bankCode;
    private String bankName;
    private String cardType;
    private String cardTypeName=CardConstants.CARD_TYPE_ROP_SAVE;

    public BankInfo() {

    }

    public BankInfo(String cardNo, String bankCode, String bankName, String cardtype) {

        this.cardNo=cardNo;
        this.bankCode=bankCode;
        this.bankName=bankName;
        this.cardType=cardtype;
        if(cardtype.equals(CardConstants.CARD_TYPE_ROP_CREDIT)){
            this.cardTypeName=CardConstants.CARD_TYPE_ROP_CREDIT_VALUE;
        }   
       
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

   

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

}

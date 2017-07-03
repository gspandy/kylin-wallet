package com.rongcapital.wallet.vo;

public class BankTypeVo {

    public BankTypeVo() {

    }

    public BankTypeVo(String bankCode) {
        this.bankCode=bankCode;
    }
    public BankTypeVo(String bankCode, String bankName) {
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

    private String bankCode;
    private String bankName;

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

    public boolean equals(Object obj) {
        if (obj instanceof BankTypeVo) {
            BankTypeVo vo = (BankTypeVo) obj;

            return (bankCode.equals(vo.getBankCode()));
        }
        return false;
    }

    public int hashCode() {

        return bankCode.hashCode();

    }
}

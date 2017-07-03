package com.rongcapital.wallet.dubbo.vo;

public class BalanceVo {

    private Long amount; // 账户余额
    private Long balanceCredit; // 贷记余额
    private Long balanceFrozon; // 冻结余额
    private Long balanceOverLimit; // 可信用消费余额
    private Long balanceSettle; // 提现余额
    private Long balanceUsable; // 可用余额
    private String finAccountId; // 账户ID

    public BalanceVo() {

    }

    public BalanceVo(Long amount, Long balanceCredit, Long balanceFrozon, Long balanceOverLimit, Long balanceSettle,
            Long balanceUsable, String finAccountId) {

        this.amount = amount;
        this.balanceCredit = balanceCredit;
        this.balanceFrozon = balanceFrozon;
        this.balanceOverLimit = balanceOverLimit;
        this.balanceSettle = balanceSettle;
        this.balanceUsable = balanceUsable;
        this.finAccountId = finAccountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalanceCredit() {
        return balanceCredit;
    }

    public void setBalanceCredit(Long balanceCredit) {
        this.balanceCredit = balanceCredit;
    }

    public Long getBalanceFrozon() {
        return balanceFrozon;
    }

    public void setBalanceFrozon(Long balanceFrozon) {
        this.balanceFrozon = balanceFrozon;
    }

    public Long getBalanceOverLimit() {
        return balanceOverLimit;
    }

    public void setBalanceOverLimit(Long balanceOverLimit) {
        this.balanceOverLimit = balanceOverLimit;
    }

    public Long getBalanceSettle() {
        return balanceSettle;
    }

    public void setBalanceSettle(Long balanceSettle) {
        this.balanceSettle = balanceSettle;
    }

    public Long getBalanceUsable() {
        return balanceUsable;
    }

    public void setBalanceUsable(Long balanceUsable) {
        this.balanceUsable = balanceUsable;
    }

    public String getFinAccountId() {
        return finAccountId;
    }

    public void setFinAccountId(String finAccountId) {
        this.finAccountId = finAccountId;
    }

}

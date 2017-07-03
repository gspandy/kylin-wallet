package com.rongcapital.wallet.dubbo.vo;

/**
 * 用户信息 Description:
 * 
 * @author: libi
 * @CreateDate: 2016年9月8日
 * @version: V1.0
 */
public class FinanaceAccountVo {

    private String orgId;
    private String userId;
    private String proId;
    private String finAccountId;
    private String finTypeId;

    public FinanaceAccountVo() {

    }

    public FinanaceAccountVo(String orgId, String proId, String userId, String finAccountId, String finTypeId) {
        this.orgId = orgId;
        this.proId = proId;
        this.userId = userId;
        this.finAccountId = finAccountId;
        this.finTypeId = finTypeId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getFinAccountId() {
        return finAccountId;
    }

    public void setFinAccountId(String finAccountId) {
        this.finAccountId = finAccountId;
    }

    public String getFinTypeId() {
        return finTypeId;
    }

    public void setFinTypeId(String finTypeId) {
        this.finTypeId = finTypeId;
    }

}

package com.rongcapital.wallet.controller.rop.vo;

/**
 * tonken用户信息类 Description:
 * 
 * @author: libi
 * @CreateDate: 2016年8月24日
 * @version: V1.0
 */
public class UserInfoVo {

    private Long userInfoId;
    
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户号
     */
    private String userCode;
    
    private String accountId;
    
    

    private String[] accountType;
    
    
   

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String[] getAccountType() {
        return accountType;
    }

    public void setAccountType(String[] accountType) {
        this.accountType = accountType;
    }

    private String pwd;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    private String userType;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 机构ID
     */
    private String orgId;
    /**
     * 产品ID
     */
    private String proId;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 用户登陆名
     */
    private String userName;
    /**
     * 用户姓名
     */
    private String userRealName;

    /**
     * 证件类型
     */
    private String idCardType;
    /**
     * 证件号
     */
    private String idCard;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * 手机号
     */
    private String tel;

    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}

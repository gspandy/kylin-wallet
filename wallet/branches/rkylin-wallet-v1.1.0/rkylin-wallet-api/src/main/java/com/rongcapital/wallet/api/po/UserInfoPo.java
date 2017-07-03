package com.rongcapital.wallet.api.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息 Description:
 * 
 * @author: Administrator
 * @CreateDate: 2016年1月25日
 * @version: V1.0
 */
public class UserInfoPo implements Serializable {

    private static final long serialVersionUID = 1209862271383735785L;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户类型
     */
    private Integer userType;
    private String salt;

    private String userCode;

    private Long userInfoId;
    
    private String userId;
    
    /*-----企业用户信息-----------*/
    
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 营业执照
     */
    private String buslince;
    

    private String corporateTel;  //法人手机号
    
    
    
    
    public String getCorporateTel() {
        return corporateTel;
    }

    public void setCorporateTel(String corporateTel) {
        this.corporateTel = corporateTel;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    
    /**
     * 证件类型
     */
    private String idCardType;
    /**
     * 密码
     */
    private String pwd;

    /**
     * 真空姓名
     */
    private String userRealName;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 产品ID
     */
    private String proId;
    private String idCard;

    private int status;

    public String getIdCard() {
        return idCard;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

   

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }
    
    

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBuslince() {
        return buslince;
    }

    public void setBuslince(String buslince) {
        this.buslince = buslince;
    }

    
}

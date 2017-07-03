/**
 * <p>Title: ${file_name}</p>
 * <p>Description:${project_name} </p>
 * <p>Copyright:${date} </p>
 * <p>Company: rongshu</p>
 * <p>author: ${user}</p>
 * <p>package: ${package_name}</p>
 * @version v1.0.0
 */
package com.rongcapital.wallet.po;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * @author: Administrator
 * @CreateDate: 2016-8-31
 * @version: V1.0
 */
public class UserInfo implements Serializable {
    private Long userInfoId;

    /** 用户ID */
    private String userId;

    private String userCode;

    /** 用户类型  1商户  2个人 */
    private Integer userType;

    /** 用户状态  1正常  0禁用  9删除 */
    private Integer status;

    private Integer version;

    /** 记录创建时间 */
    private Date createdTime;

    /** 记录更新时间 */
    private Date updatedTime;

    private PersonInfo personInfo;
    
    private CompanyInfo companyInfo;
    
    private static final long serialVersionUID = 1L;

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

   

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

   

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

   
}
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
 * @CreateDate: 2016-10-11
 * @version: V1.0
 */
public class UserWhite implements Serializable {
    /** 主键ID */
    private Long id;

    /** 用户ID */
    private String userId;

    /** 关联的USER_ID */
    private String refId;

    /** 关联名称 */
    private String refName;

    /** 1.供应商，2.分销商 */
    private Integer refType;

    /** 应用ID */
    private String applyId;

    /** 记录创建时间 */
    private Date createdTime;

    /** 记录更新时间 */
    private Date updatedTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId == null ? null : refId.trim();
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName == null ? null : refName.trim();
    }

   

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId == null ? null : applyId.trim();
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
}
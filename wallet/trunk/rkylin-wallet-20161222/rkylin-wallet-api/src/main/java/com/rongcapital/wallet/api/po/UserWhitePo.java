package com.rongcapital.wallet.api.po;

import java.io.Serializable;

public class UserWhitePo implements Serializable {

    /** 用户ID */
    private String userId;

    /** 关联的USER_ID */
    private String refId;

    /** 关联名称 */
    private String refName;

    /** 类型1.经销商 2.分销商 */
    private Integer refType;
    /** 应用ID */
    private String applyId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

  
}

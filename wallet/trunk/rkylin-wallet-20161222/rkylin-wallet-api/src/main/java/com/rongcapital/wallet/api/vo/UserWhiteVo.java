package com.rongcapital.wallet.api.vo;

import java.io.Serializable;

public class UserWhiteVo implements Serializable {

    public UserWhiteVo(){
        
    }
 
     public UserWhiteVo(String refId,String refName,Integer refType){
        
         this.refId=refId;
         this.refName=refName;
         this.refType=refType;
    }
    /** 关联的USER_ID */
    private String refId;

    /** 关联名称 */
    private String refName;

    /** 类型1.经销商 2.分销商 */
    private Integer refType;

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

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    
}

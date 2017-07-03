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
     
     
     public UserWhiteVo(String refId,String refName,Integer refType,String userName,String userRealname){
         
         this.refId=refId;
         this.refName=refName;
         this.refType=refType;
         this.userName=userName;
         this.userRealname=userRealname;
         
    }
     
    /** 关联的USER_ID */
    private String refId;

    /** 关联名称 */
    private String refName;

    /** 类型1.经销商 2.分销商 */
    private Integer refType;
    
    private String userName;  //登陆名
    
    private String userRealname;  //真实姓名

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    
}

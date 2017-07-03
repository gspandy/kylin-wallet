package com.rongcapital.wallet.api.po;

import java.io.Serializable;

public class CompanyPo implements Serializable{
    
    private String loginName; //登陆名  
    private String salt;  //密码盐值
    private String pwd;  //密码
    private Integer loginUserType; //用户类型(1企业,2个人)
    private String companyName; // 企业名称 N
    private String busLince; // 营业执照 N
    private String userId; // 接入机构中设置的用户ID N
    private String userType; // 用户类型(1：商户 ) N
    private String constId; // 机构号 N
    private String productId; // 产品号 N
    private String userName; // 用户名称，及接入机构的用户名 N
    
    private String shortName; // 企业简称 Y
    private String mcc; // MCC码（行业类别） Y
    private String post; // 邮编 Y
    private String connect; // 联系方式 Y
    private String address; // 地址 Y
    private String acuntOpnLince; // 开户许可证 Y
    private String companyCode; // 企业编号 Y
    private String taxRegCardf; // 税务登记证1 Y
    private String taxRegCards; // 税务登记证2 Y
    private String organCertificate; // 组织结构代码证 Y
    private String corporateName; // 法人姓名 Y
    private String corporateIdentity; // 法人身份证 Y
    private String busPlaceCtf; // 经营场所实地认证 Y
    private String loanCard; // 贷款卡 Y
    private String remark; // 备注 Y
    private String role; // 角色号（场地方上送10） Y
    
    
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public Integer getLoginUserType() {
        return loginUserType;
    }
    public void setLoginUserType(Integer loginUserType) {
        this.loginUserType = loginUserType;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getBusLince() {
        return busLince;
    }
    public void setBusLince(String busLince) {
        this.busLince = busLince;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getConstId() {
        return constId;
    }
    public void setConstId(String constId) {
        this.constId = constId;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getMcc() {
        return mcc;
    }
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }
    public String getPost() {
        return post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public String getConnect() {
        return connect;
    }
    public void setConnect(String connect) {
        this.connect = connect;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAcuntOpnLince() {
        return acuntOpnLince;
    }
    public void setAcuntOpnLince(String acuntOpnLince) {
        this.acuntOpnLince = acuntOpnLince;
    }
    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public String getTaxRegCardf() {
        return taxRegCardf;
    }
    public void setTaxRegCardf(String taxRegCardf) {
        this.taxRegCardf = taxRegCardf;
    }
    public String getTaxRegCards() {
        return taxRegCards;
    }
    public void setTaxRegCards(String taxRegCards) {
        this.taxRegCards = taxRegCards;
    }
    public String getOrganCertificate() {
        return organCertificate;
    }
    public void setOrganCertificate(String organCertificate) {
        this.organCertificate = organCertificate;
    }
    public String getCorporateName() {
        return corporateName;
    }
    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
    public String getCorporateIdentity() {
        return corporateIdentity;
    }
    public void setCorporateIdentity(String corporateIdentity) {
        this.corporateIdentity = corporateIdentity;
    }
    public String getBusPlaceCtf() {
        return busPlaceCtf;
    }
    public void setBusPlaceCtf(String busPlaceCtf) {
        this.busPlaceCtf = busPlaceCtf;
    }
    public String getLoanCard() {
        return loanCard;
    }
    public void setLoanCard(String loanCard) {
        this.loanCard = loanCard;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    
}

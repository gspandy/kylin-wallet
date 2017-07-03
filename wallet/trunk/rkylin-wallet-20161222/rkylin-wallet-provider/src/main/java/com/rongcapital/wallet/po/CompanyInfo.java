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
 * @CreateDate: 2016-10-20
 * @version: V1.0
 */
public class CompanyInfo implements Serializable {
    private Long id;

    private Long userInfoId;

    /** 企业名称 */
    private String companyName;

    /** 企业简称 */
    private String companyShortName;

    /** 企业类型 */
    private Integer companyType;

    /** 用户名称及接入机构的用户名 */
    private String userName;
    

    /** 行业编号 */
    private String mcc;

    /** 邮编 */
    private String post;

    /** 传真 */
    private String fax;

    /** 省份 */
    private String province;

    /** 地市 */
    private String city;

    /** 联系人 */
    private String contacts;

    /** 联系方式 */
    private String contactsInfo;

    /** 注册资金 */
    private String registFinacnce;

    /** 员工数 */
    private String members;

    /** 地址 */
    private String address;

    /** 网址 */
    private String website;

    /** 营业执照号 */
    private String buslince;

    /** 开户许可证 */
    private String acuntOpnLince;

    /** 税务登记证1 */
    private String taxregCard1;

    /** 税务登记证2 */
    private String taxregCard2;

    /** 住址机构代码证 */
    private String organCertificate;

    /** 法人姓名 */
    private String corporateName;

    /** 法人身份证 */
    private String corporateIdentity;

    /** 法人电话 */
    private String corporateTel;

    /** 法人邮箱 */
    private String corporateMail;

    /** 经营场所实地认证 */
    private String busPlaceCtf;

    /** 贷款卡 */
    private String loanCard;

    /** 备用1 */
    private String otherinfo1;

    /** 备用2 */
    private String otherinfo2;

    /** 备用3 */
    private String otherinfo3;

    /** 备注 */
    private String remark;

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
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName == null ? null : companyShortName.trim();
    }

    

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc == null ? null : mcc.trim();
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post == null ? null : post.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getContactsInfo() {
        return contactsInfo;
    }

    public void setContactsInfo(String contactsInfo) {
        this.contactsInfo = contactsInfo == null ? null : contactsInfo.trim();
    }

    public String getRegistFinacnce() {
        return registFinacnce;
    }

    public void setRegistFinacnce(String registFinacnce) {
        this.registFinacnce = registFinacnce == null ? null : registFinacnce.trim();
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members == null ? null : members.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getBuslince() {
        return buslince;
    }

    public void setBuslince(String buslince) {
        this.buslince = buslince == null ? null : buslince.trim();
    }

    public String getAcuntOpnLince() {
        return acuntOpnLince;
    }

    public void setAcuntOpnLince(String acuntOpnLince) {
        this.acuntOpnLince = acuntOpnLince == null ? null : acuntOpnLince.trim();
    }

    public String getTaxregCard1() {
        return taxregCard1;
    }

    public void setTaxregCard1(String taxregCard1) {
        this.taxregCard1 = taxregCard1 == null ? null : taxregCard1.trim();
    }

    public String getTaxregCard2() {
        return taxregCard2;
    }

    public void setTaxregCard2(String taxregCard2) {
        this.taxregCard2 = taxregCard2 == null ? null : taxregCard2.trim();
    }

    public String getOrganCertificate() {
        return organCertificate;
    }

    public void setOrganCertificate(String organCertificate) {
        this.organCertificate = organCertificate == null ? null : organCertificate.trim();
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName == null ? null : corporateName.trim();
    }

    public String getCorporateIdentity() {
        return corporateIdentity;
    }

    public void setCorporateIdentity(String corporateIdentity) {
        this.corporateIdentity = corporateIdentity == null ? null : corporateIdentity.trim();
    }

    public String getCorporateTel() {
        return corporateTel;
    }

    public void setCorporateTel(String corporateTel) {
        this.corporateTel = corporateTel == null ? null : corporateTel.trim();
    }

    public String getCorporateMail() {
        return corporateMail;
    }

    public void setCorporateMail(String corporateMail) {
        this.corporateMail = corporateMail == null ? null : corporateMail.trim();
    }

    public String getBusPlaceCtf() {
        return busPlaceCtf;
    }

    public void setBusPlaceCtf(String busPlaceCtf) {
        this.busPlaceCtf = busPlaceCtf == null ? null : busPlaceCtf.trim();
    }

    public String getLoanCard() {
        return loanCard;
    }

    public void setLoanCard(String loanCard) {
        this.loanCard = loanCard == null ? null : loanCard.trim();
    }

    public String getOtherinfo1() {
        return otherinfo1;
    }

    public void setOtherinfo1(String otherinfo1) {
        this.otherinfo1 = otherinfo1 == null ? null : otherinfo1.trim();
    }

    public String getOtherinfo2() {
        return otherinfo2;
    }

    public void setOtherinfo2(String otherinfo2) {
        this.otherinfo2 = otherinfo2 == null ? null : otherinfo2.trim();
    }

    public String getOtherinfo3() {
        return otherinfo3;
    }

    public void setOtherinfo3(String otherinfo3) {
        this.otherinfo3 = otherinfo3 == null ? null : otherinfo3.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
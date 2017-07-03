package com.rongcapital.wallet.vo;

public class CompanyVo {

	private String companyName; // 企业名称 N
	private String busLince; // 营业执照 N
	private String userId; // 接入机构中设置的用户ID N
	private String userType; // 用户类型(1：商户 ) N
	private String constId; // 机构号 N
	private String productId; // 产品号 N
	private String userName; // 用户名称，及接入机构的用户名 N
	private String loginName; //登陆名
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
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	

}

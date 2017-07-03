package com.rongcapital.wallet.vo;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;

public class OrderInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Mapping("ORDER_ID")
    private String orderId;
    @Mapping("ORDER_TYPE_ID")
    private String orderTypeId;
    @Mapping("ORDER_DATE")
    private Date orderDate;
    @Mapping("ORDER_TIME")
    private Date orderTime;
    @Mapping("PROVIDER_ID")
    private String providerId;
    @Mapping("PRODUCT_ID")
    private String productId;
    @Mapping("ROOT_INST_CD")
    private String rootInstCd;
    @Mapping("USER_ID")
    private String userId;
    @Mapping("USER_ORDER_ID")
    private String userOrderId;
    @Mapping("USER_RELATE_ID")
    private String userRelateId;
    @Mapping("AMOUNT")
    private Long amount;
    @Mapping("REMARK")
    private String remark;
    @Mapping("ORDER_CONTROL")
    private String orderControl;
    @Mapping("RESP_CODE")
    private String respCode;
    @Mapping("STATUS_ID")
    private String statusId;
    @Mapping("CHECK_CODE")
    private Integer checkCode;
    @Mapping("GOODS_NAME")
    private String goodsName;
    @Mapping("GOODS_DETAIL")
    private String goodsDetail;
    @Mapping("GOODS_CNT")
    private Integer goodsCnt;
    @Mapping("UNIT_PRICE")
    private Long unitPrice;
    @Mapping("CREATED_TIME")
    private Date createdTime;
    @Mapping("UPDATED_TIME")
    private Date updatedTime;
    @Mapping("BANK_HEAD_NAME")
    private String bankHeadName;
    @Mapping("ACCOUNT_NUMBER")
    private String accountNumber;
    @Mapping("ACCOUNT_REAL_NAME")
    private String accountRealName;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountRealName() {
        return accountRealName;
    }

    public void setAccountRealName(String accountRealName) {
        this.accountRealName = accountRealName;
    }

    public String getBankHeadName() {
        return bankHeadName;
    }

    public void setBankHeadName(String bankHeadName) {
        this.bankHeadName = bankHeadName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderTypeId(String orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getOrderTypeId() {
        return this.orderTypeId;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getOrderTime() {
        return this.orderTime;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setRootInstCd(String rootInstCd) {
        this.rootInstCd = rootInstCd;
    }

    public String getRootInstCd() {
        return this.rootInstCd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserOrderId(String userOrderId) {
        this.userOrderId = userOrderId;
    }

    public String getUserOrderId() {
        return this.userOrderId;
    }

    public void setUserRelateId(String userRelateId) {
        this.userRelateId = userRelateId;
    }

    public String getUserRelateId() {
        return this.userRelateId;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setOrderControl(String orderControl) {
        this.orderControl = orderControl;
    }

    public String getOrderControl() {
        return this.orderControl;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespCode() {
        return this.respCode;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusId() {
        return this.statusId;
    }

    public Integer getCheckCode() {
        return this.checkCode;
    }

    public void setCheckCode(Integer checkCode) {
        this.checkCode = checkCode;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsDetail() {
        return this.goodsDetail;
    }

    public void setGoodsCnt(Integer goodsCnt) {
        this.goodsCnt = goodsCnt;
    }

    public Integer getGoodsCnt() {
        return this.goodsCnt;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getUnitPrice() {
        return this.unitPrice;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() {
        return this.updatedTime;
    }
}

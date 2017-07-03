package com.rongcapital.wallet.vo;

import java.io.Serializable;
import java.util.Date;

public class OrderInfoMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ORDER_ID; //订单ID
    private String ORDER_TYPE_ID; //业务类型ID
    private Date ORDER_DATE;  //订单日期
    private Date ORDER_TIME; //订单时间
    private String PROVIDER_ID; //授信提供方ID
    private String PRODUCT_ID;  //产品ID
    private String ROOT_INST_CD;  //管理机构代码
    private String USER_ID;  //用户ID
    private String USER_ORDER_ID; //用户订单流水号
    private String USER_RELATE_ID; //关联用户ID
    private Long AMOUNT;  //订单金额(分)
    private String REMARK; //订单备注
    private String ORDER_CONTROL; //订单控制
    private String RESP_CODE;  //应答码
    private String STATUS_ID; //订单状态,0失效,1生效
    private Integer CHECK_CODE; //是否校验对账，0 未对账 1 已对账
    private String GOODS_NAME;  //商品名字
    private String GOODS_DETAIL; //商品描述
    private Integer GOODS_CNT;  //商品数量
    private Long UNIT_PRICE; //商品单价
    private Date CREATED_TIME;  //创建时间
    private Date UPDATED_TIME;  //更新时间
    private String BANK_HEAD_NAME;  //开户行总行名称
    private String ACCOUNT_NUMBER;  //账号
    private String ACCOUNT_REAL_NAME; //账户真实名称

    public String getACCOUNT_REAL_NAME() {
        return ACCOUNT_REAL_NAME;
    }

    public void setACCOUNT_REAL_NAME(String aCCOUNT_REAL_NAME) {
        ACCOUNT_REAL_NAME = aCCOUNT_REAL_NAME;
    }

    public String getBANK_HEAD_NAME() {
        return BANK_HEAD_NAME;
    }

    public void setBANK_HEAD_NAME(String bANK_HEAD_NAME) {
        BANK_HEAD_NAME = bANK_HEAD_NAME;
    }

    public String getACCOUNT_NUMBER() {
        return ACCOUNT_NUMBER;
    }

    public void setACCOUNT_NUMBER(String aCCOUNT_NUMBER) {
        ACCOUNT_NUMBER = aCCOUNT_NUMBER;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String oRDER_ID) {
        ORDER_ID = oRDER_ID;
    }

    public String getORDER_TYPE_ID() {
        return ORDER_TYPE_ID;
    }

    public void setORDER_TYPE_ID(String oRDER_TYPE_ID) {
        ORDER_TYPE_ID = oRDER_TYPE_ID;
    }

    public Date getORDER_DATE() {
        return ORDER_DATE;
    }

    public void setORDER_DATE(Date oRDER_DATE) {
        ORDER_DATE = oRDER_DATE;
    }

    public Date getORDER_TIME() {
        return ORDER_TIME;
    }

    public void setORDER_TIME(Date oRDER_TIME) {
        ORDER_TIME = oRDER_TIME;
    }

    public String getPROVIDER_ID() {
        return PROVIDER_ID;
    }

    public void setPROVIDER_ID(String pROVIDER_ID) {
        PROVIDER_ID = pROVIDER_ID;
    }

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String pRODUCT_ID) {
        PRODUCT_ID = pRODUCT_ID;
    }

    public String getROOT_INST_CD() {
        return ROOT_INST_CD;
    }

    public void setROOT_INST_CD(String rOOT_INST_CD) {
        ROOT_INST_CD = rOOT_INST_CD;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String uSER_ID) {
        USER_ID = uSER_ID;
    }

    public String getUSER_ORDER_ID() {
        return USER_ORDER_ID;
    }

    public void setUSER_ORDER_ID(String uSER_ORDER_ID) {
        USER_ORDER_ID = uSER_ORDER_ID;
    }

    public String getUSER_RELATE_ID() {
        return USER_RELATE_ID;
    }

    public void setUSER_RELATE_ID(String uSER_RELATE_ID) {
        USER_RELATE_ID = uSER_RELATE_ID;
    }

    public Long getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(Long aMOUNT) {
        AMOUNT = aMOUNT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String rEMARK) {
        REMARK = rEMARK;
    }

    public String getORDER_CONTROL() {
        return ORDER_CONTROL;
    }

    public void setORDER_CONTROL(String oRDER_CONTROL) {
        ORDER_CONTROL = oRDER_CONTROL;
    }

    public String getRESP_CODE() {
        return RESP_CODE;
    }

    public void setRESP_CODE(String rESP_CODE) {
        RESP_CODE = rESP_CODE;
    }

    public String getSTATUS_ID() {
        return STATUS_ID;
    }

    public void setSTATUS_ID(String sTATUS_ID) {
        STATUS_ID = sTATUS_ID;
    }

    public Integer getCHECK_CODE() {
        return CHECK_CODE;
    }

    public void setCHECK_CODE(Integer cHECK_CODE) {
        CHECK_CODE = cHECK_CODE;
    }

    public String getGOODS_NAME() {
        return GOODS_NAME;
    }

    public void setGOODS_NAME(String gOODS_NAME) {
        GOODS_NAME = gOODS_NAME;
    }

    public String getGOODS_DETAIL() {
        return GOODS_DETAIL;
    }

    public void setGOODS_DETAIL(String gOODS_DETAIL) {
        GOODS_DETAIL = gOODS_DETAIL;
    }

    public Integer getGOODS_CNT() {
        return GOODS_CNT;
    }

    public void setGOODS_CNT(Integer gOODS_CNT) {
        GOODS_CNT = gOODS_CNT;
    }

    public Long getUNIT_PRICE() {
        return UNIT_PRICE;
    }

    public void setUNIT_PRICE(Long uNIT_PRICE) {
        UNIT_PRICE = uNIT_PRICE;
    }

    public Date getCREATED_TIME() {
        return CREATED_TIME;
    }

    public void setCREATED_TIME(Date cREATED_TIME) {
        CREATED_TIME = cREATED_TIME;
    }

    public Date getUPDATED_TIME() {
        return UPDATED_TIME;
    }

    public void setUPDATED_TIME(Date uPDATED_TIME) {
        UPDATED_TIME = uPDATED_TIME;
    }

}

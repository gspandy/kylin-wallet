package com.rongcapital.wallet.constants;

import java.util.HashMap;
import java.util.Map;

public class OrderCodeEnum {

    public final static Map<String, String> MAP = new HashMap<>();
    public final static Map<String, String> PAY_TYPE_MAP = new HashMap<>();

    static {
        MAP.put("1", "成功");
        MAP.put("4", "成功");
        MAP.put("0", "失败");
        MAP.put("5", "失败");
        MAP.put("8", "处理中");
        MAP.put("B1", "充值");
        MAP.put("3001", "白名单付款");
        MAP.put("B2", "提现");
        PAY_TYPE_MAP.put("1", "个人银行");
        PAY_TYPE_MAP.put("2", "企业银行");
        PAY_TYPE_MAP.put("3", "信用卡");
        PAY_TYPE_MAP.put("11", "个人银行");
        PAY_TYPE_MAP.put("12", "企业银行");
        PAY_TYPE_MAP.put("13", "信用卡");
        PAY_TYPE_MAP.put("21", "wap支付");
        PAY_TYPE_MAP.put("22", "认证支付");
        PAY_TYPE_MAP.put("23", "快捷wap支付");
        PAY_TYPE_MAP.put("24", "SDK支付");
        PAY_TYPE_MAP.put("27", "认证wap支付");
        PAY_TYPE_MAP.put("28", "微信支付");
        PAY_TYPE_MAP.put("29", "平安银行线下充值");
        PAY_TYPE_MAP.put("30", "微信扫码支付Url");
        PAY_TYPE_MAP.put("31", "微信扫码支付二维码");

    }
}

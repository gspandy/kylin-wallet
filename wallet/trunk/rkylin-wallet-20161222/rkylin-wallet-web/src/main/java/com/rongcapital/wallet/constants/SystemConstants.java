package com.rongcapital.wallet.constants;

public class SystemConstants {

    public static final String SYSTEM_REDIS_KEY = "wallet.";
    public static final String SYSTEM_REDIS_KEY_SMS = "wallet.sms.";
    public static final String SYSTEM_REDIS_KEY_TIPS = "wallet.tips.";

    public static String getKey(String key) {

        return SYSTEM_REDIS_KEY + key;
    }

    public static String getKeySMS(String key) {

        return SYSTEM_REDIS_KEY_SMS + key;
    }

    public static String getKeyTIPS(String key) {

        return SYSTEM_REDIS_KEY_TIPS + key;
    }

    public static final Integer LOGIN_TYPE_ENTER = 1;
    public static final Integer LOGIN_TYPE_OUT = 2;
}

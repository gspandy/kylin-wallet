package com.rongcapital.wallet.constants;

public class SystemConstants {

    public static final String SYSTEM_REDIS_KEY = "wallet.";
    

    public static String getKey(String key) {

        return SYSTEM_REDIS_KEY + key;
    }
}

package com.rongcapital.wallet.constants;

import com.rongcapital.wallet.util.ConfigUtil;

public class RopConstants {

    public static final String APP_KEY_DX_QY = ConfigUtil.getProperty("APP_KEY_DX_QY");
    public static final String APP_SECRET_DX_QY = ConfigUtil.getProperty("APP_SECRET_DX_QY");
    public static final String APP_KEY_DX = ConfigUtil.getProperty("APP_KEY_DX");
    public static final String APP_SECRET_DX = ConfigUtil.getProperty("APP_SECRET_DX");
    public static final String ROP_URL_DX = ConfigUtil.getProperty("ROP_URL_DX");

    // public static final String APP_KEY_Z = ConfigUtil.getProperty("APP_KEY_Z");
    // public static final String APP_SECRET_Z = ConfigUtil.getProperty("APP_SECRET_Z");
    // public static final String ROP_URL_Z = ConfigUtil.getProperty("ROP_URL_Z");

    // public static final String APP_KEY_S = ConfigUtil.getProperty("APP_KEY_S");
    // public static final String APP_SECRET_S = ConfigUtil.getProperty("APP_SECRET_S");
    // public static final String ROP_URL_S = ConfigUtil.getProperty("ROP_URL_S");

    public static final String APP_KEY = ConfigUtil.getProperty("APP_KEY");
    public static final String APP_SECRET = ConfigUtil.getProperty("APP_SECRET");
    public static final String ROP_URL = ConfigUtil.getProperty("ROP_URL");

    // public static final String APP_KEY_90 = ConfigUtil.getProperty("APP_KEY_90");
    // public static final String APP_SECRET_90 = ConfigUtil.getProperty("APP_SECRET_90");
    // public static final String ROP_URL_90 = ConfigUtil.getProperty("ROP_URL_90");

    public static final String RESULT_TYPE_JSON = "json";
    public static final String RESULT_TYPE_XML = "xml";

    public static void main(String[] args) {
        System.out.println(RopConstants.APP_KEY_DX);
    }
}

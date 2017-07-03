package com.rongcapital.wallet.constants;

import java.util.Map;

import com.google.common.collect.Maps;

public class SMSConstants {

    public static final String SMS_JSONORXML = "xml";
    public static final String SMS_CHANNEL_CODE = "BF175285-4821-4C41-9462-6EC00F84E57B";
    public static final String SMS_APPEND_CODE = "0014";
    public static final String SMS_TEMPLET = "SMS0";
    public static final String SMS_TEMPLET_ACTIVATION = "SMS1";
    public static final String SMS_TEMPLET_DOWNLOAD = "SMS2";

    public static final Map<String, String> map = Maps.newHashMap();
    static {
        map.put(SMS_TEMPLET, RopConstants.APP_KEY_DX + ";" + RopConstants.APP_SECRET_DX);
        map.put(SMS_TEMPLET_ACTIVATION, RopConstants.APP_KEY_DX_QY + ";" + RopConstants.APP_SECRET_DX_QY);
        map.put(SMS_TEMPLET_DOWNLOAD, RopConstants.APP_KEY_DX_QY + ";" + RopConstants.APP_SECRET_DX_QY);
    }
}

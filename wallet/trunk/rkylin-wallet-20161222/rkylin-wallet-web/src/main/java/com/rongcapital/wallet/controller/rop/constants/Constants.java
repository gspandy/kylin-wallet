package com.rongcapital.wallet.controller.rop.constants;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    
    public static final Map<String,String> mapSysKey=new HashMap<>();
    static{
        mapSysKey.put(Constants.SYS_PARAM_METHOD, "");
        mapSysKey.put(Constants.SYS_PARAM_FORMAT, "");
        mapSysKey.put(Constants.SYS_PARAM_SESSION, "");
        mapSysKey.put(Constants.SYS_PARAM_TIMESTAMP, "");
        mapSysKey.put(Constants.SYS_PARAM_APP_KEY, "");
        mapSysKey.put(Constants.SYS_PARAM_SIGN, "");
        
    }
	/** 日期格式 */
	// yyyy-MM-dd HH:mm:ss
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	// yyyy-MM-dd HH:mm:ss
	public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyy-MM-dd HH:mm:ss.SSS";

	/** 字符集 */
	// UTF-8
	public static final String CHARSET_UTF8 = "UTF-8";
	// GBK
	public static final String CHARSET_GBK = "GBK";

	/** 数据格式 */
	// XML
	public static final String DATA_PROTOCOL_TYPE_XML = "xml";
	// JSON
	public static final String DATA_PROTOCOL_TYPE_JSON = "json";
	
	/** 签名方式 */
	// MD5
	public static final String SIGN_TYPE_MD5 = "md5";
	
	
	/** 系统参数名 */
	// method
	public static final String SYS_PARAM_METHOD = "method";
	// format
	public static final String SYS_PARAM_FORMAT = "format";
	// session
	public static final String SYS_PARAM_SESSION = "session";
	// timestamp
	public static final String SYS_PARAM_TIMESTAMP = "timestamp";
	// app_key
	public static final String SYS_PARAM_APP_KEY = "app_key";
	// sign
	public static final String SYS_PARAM_SIGN = "sign";
}

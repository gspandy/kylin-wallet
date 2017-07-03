package com.rongcapital.wallet.vo;

import java.util.HashMap;
import java.util.Map;

import com.rongcapital.wallet.constants.VersionConstants;

public class ActionResult {

    /**
     * 是否成功
     */
    private boolean success;

    private String msg = "";

    private String code = "0";
    
   

    private Map<String, Object> container = new HashMap<String, Object>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 向ActionResult中添加自定义的结果
     */
    public void addResult(String key, Object value) {
        container.put(key, value);
    }

    public Map<String, Object> getData() {

        return this.container;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

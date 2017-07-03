package com.rongcapital.wallet.controller.rop.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseResponse extends Response implements Serializable{
	
	private String responsecode="200";
	private String message;
	
	
	 private Map<String, Object> container = new HashMap<String, Object>();
	
	public String getResponsecode() {
		return responsecode;
	}
	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
   
}

package com.rongcapital.wallet.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Settings {
    private static Settings instance;

    public static Settings getInstance() {
        return instance;
    }

    public Settings() {
        instance = this;
    }

    @Value("${common.ordertypeid}")
    private String ordertypeid;
    @Value("${common.notifyUrl}")
    private String notifyUrl;
    @Value("${common.key}")
    private String key;
    @Value("${common.url}")
    private String url;
    @Value("${common.pageUrl}")
    private String pageUrl;
    @Value("${common.redirectUrl}")
    private String redirectUrl;
    @Value("${common.assistantUrl}")
    private String assistantUrl;

    public String getOrdertypeid() {
        return ordertypeid;
    }

    public void setOrdertypeid(String ordertypeid) {
        this.ordertypeid = ordertypeid;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getAssistantUrl() {
        return assistantUrl;
    }

    public void setAssistantUrl(String assistantUrl) {
        this.assistantUrl = assistantUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
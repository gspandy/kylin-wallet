package com.rongcapital.wallet.vo;

/**
 * 手机客户端的相关参数 User: gaotianlin Date: 2011-2-14 Time: 16:24:49
 */
public class ClientInfo {
    private String client;// 客户端类型
    private String version;// 客户端版本
    private String token;// 机器唯一编码
    private String userId; //用户ID

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    
   
}

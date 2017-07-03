package com.rongcapital.wallet.api.service;



/**
 * token验证 Description:
 * 
 * @author: libi
 * @CreateDate: 2016年8月31日
 * @version: V1.0
 */
public interface TokenApiService {

    boolean validateToken(String tonken, String userId);

}

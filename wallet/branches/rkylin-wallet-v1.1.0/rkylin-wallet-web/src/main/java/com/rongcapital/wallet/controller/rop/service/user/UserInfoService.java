package com.rongcapital.wallet.controller.rop.service.user;

import java.util.Map;

import com.rongcapital.wallet.controller.rop.response.Response;

public interface UserInfoService {

    public Response login(Map<String, String[]> requestParams) throws Exception;
}

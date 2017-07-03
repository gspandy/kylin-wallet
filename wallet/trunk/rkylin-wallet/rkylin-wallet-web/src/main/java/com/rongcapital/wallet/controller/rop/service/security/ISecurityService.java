package com.rongcapital.wallet.controller.rop.service.security;

import java.util.Map;

import com.rongcapital.wallet.controller.rop.response.Response;



public interface ISecurityService {

	public Response verifyRequest(Map<String, String[]> requestParams);
}

package com.rongcapital.wallet.controller.rop.service.error;

import com.rongcapital.wallet.controller.rop.response.ErrorResponse;

public interface IErrorResponseService {
	
	public ErrorResponse getErrorResponse(String code);

	public ErrorResponse getErrorResponse(String code, String msg);

	public ErrorResponse getErrorResponse(String code, String msg, String subCode, String subMsg);

}

package com.rongcapital.wallet.controller.rop.service.error.impl;

import java.util.Properties;

import com.rongcapital.wallet.controller.rop.response.ErrorResponse;
import com.rongcapital.wallet.controller.rop.service.error.IErrorResponseService;


public class ErrorResponseServiceImpl implements IErrorResponseService {

	private Properties errorCodeProperties;

	public Properties getErrorCodeProperties() {
		return errorCodeProperties;
	}

	public void setErrorCodeProperties(Properties errorCodeProperties) {
		this.errorCodeProperties = errorCodeProperties;
	}

	
	public ErrorResponse getErrorResponse(String code) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);

		errorResponse.setCode(code);
		errorResponse.setMsg(errorCodeProperties.getProperty(code));

		return errorResponse;
	}

	@Override
	public ErrorResponse getErrorResponse(String code, String msg) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);
		
		errorResponse.setCode(code);
		errorResponse.setMsg(msg);
		
		return errorResponse;
	}

	
	public ErrorResponse getErrorResponse(String code, String msg,
			String subCode, String subMsg) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setCallResult(false);

		errorResponse.setCode(code);
		errorResponse.setMsg(msg);
		errorResponse.setSubCode(subCode);
		errorResponse.setSubMsg(subMsg);

		return errorResponse;
	}

}

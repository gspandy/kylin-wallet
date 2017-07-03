package com.rongcapital.wallet.util.exception;

/**
 * 账户异常类 Description:
 * 
 * @author: 马婉霞
 * @CreateDate: 2016年3月1日
 * @version: V1.0
 */
public class UserInfoException extends BaseException {

	private static final long serialVersionUID = 1L;

	public UserInfoException(String defineCode) {
		super(defineCode);
	}

	public UserInfoException(String defineCode, String defineMsg) {
		super(defineCode, defineMsg);
	}
	
	public UserInfoException(String defineCode, String defineMsg , String message) {
		super(defineCode, defineMsg , message);
	}
}

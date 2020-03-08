package com.vluee.png.shrfacade.interfaces.web;

import com.vluee.png.shrfacade.application.exception.ExceptionOutput;

public class BisResp {

	public static final String SUCCESS_CODE = "PNG-00000";

	private String code;
	private String message;

	public BisResp(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public BisResp() {

	}

	public static final BisResp fromException(ExceptionOutput exceptionOutput) {
		return new BisResp(exceptionOutput.getCode(), exceptionOutput.getMessage());
	}

	public static final BisResp okEntity() {
		BisResp br = new BisResp();
		br.setCode(SUCCESS_CODE);
		return br;
	}

	public static final BisResp okEntity(String message) {
		BisResp br = new BisResp();
		br.setCode(SUCCESS_CODE);
		br.setMessage(message);
		return br;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

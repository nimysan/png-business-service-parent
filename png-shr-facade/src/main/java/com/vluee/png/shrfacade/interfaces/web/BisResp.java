package com.vluee.png.shrfacade.interfaces.web;

public class BisResp {
	private String code;
	private String message;

	public static final BisResp okEntity() {
		BisResp br = new BisResp();
		br.setCode("SA00000");
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

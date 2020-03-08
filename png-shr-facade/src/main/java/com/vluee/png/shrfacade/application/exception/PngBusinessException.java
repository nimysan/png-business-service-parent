package com.vluee.png.shrfacade.application.exception;

public class PngBusinessException extends RuntimeException {

	public static String EC_NOT_PNG_EMPLOYEE = "PNG-ERROR-0004";
	public static String EC_ONLY_ONE_VCODE_WITHIN_TIME = "PNG-ERROR-0005";

	private static final long serialVersionUID = -886879840018982625L;

	public String errorCode;

	public PngBusinessException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public PngBusinessException(String errorCode) {
		this(errorCode, PngExceptionUtil.getMessageByCode(errorCode));
	}

	public PngBusinessException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
}

package com.vluee.png.shrfacade.application.exception;

public class PngBusinessException extends RuntimeException {

	public static String EC_SMS_CHANNEL_FAIL = "PNG-ERROR-0003";
	public static String EC_NOT_PNG_EMPLOYEE = "PNG-ERROR-0004";

	public static String EC_ONLY_ONE_VCODE_WITHIN_TIME = "PNG-ERROR-0005";

	public static String EC_VCODE_NOTEXIST = "PNG-ERROR-0010";
	public static String EC_VCODE_NOTMATCH = "PNG-ERROR-0011";
	public static String EC_VCODE_MOBILE_NOT_MATCH = "PNG-ERROR-0012";
	public static String EC_VCODE_EXPIRED = "PNG-ERROR-0013";

	public static String EC_HR_CALL_ERROR = "PNG-ERROR-0020";
	public static String EC_HR_MULTI_USERS_WITH_SAME_PHONE = "PNG-ERROR-0021";
	public static String EC_HR_NO_USER = "PNG-ERROR-0022";

	private static final long serialVersionUID = -886879840018982625L;

	public String errorCode;

	public PngBusinessException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public PngBusinessException(String errorCode) {
		this(errorCode, "Error[" + errorCode + "]");
	}

	public PngBusinessException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}

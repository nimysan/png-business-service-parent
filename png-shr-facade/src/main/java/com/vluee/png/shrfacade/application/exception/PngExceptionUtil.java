package com.vluee.png.shrfacade.application.exception;

public class PngExceptionUtil {

	public static String getMessageByCode(String errorCode) {
		return null;
	}

	public static void throwExceptionWithCode(String errorCode) throws PngBusinessException {
		throw new PngBusinessException(errorCode);
	}

}

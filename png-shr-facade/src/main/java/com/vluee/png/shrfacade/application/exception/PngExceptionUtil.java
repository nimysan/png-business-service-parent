package com.vluee.png.shrfacade.application.exception;

import lombok.extern.slf4j.Slf4j;

public class PngExceptionUtil {

	public static String getMessageByCode(String errorCode) {
		return null;
	}

	public static void throwExceptionWithCode(String errorCode) throws PngBusinessException {
		throw new PngBusinessException(errorCode);
	}

}

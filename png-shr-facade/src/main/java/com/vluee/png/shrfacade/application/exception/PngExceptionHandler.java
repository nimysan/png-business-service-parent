package com.vluee.png.shrfacade.application.exception;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PngExceptionHandler {

	@Resource
	private MessageSource messageSource;

	private Locale locale = Locale.getDefault();

	public synchronized void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getMessage(String key, String... args) {
		try {
			return this.messageSource.getMessage(key, args, this.locale);
		} catch (Exception e) {
			log.error("Failed to i18n", e);
			return key;
		}
	}

	public ExceptionOutput output(Exception e) {
		if (e instanceof PngBusinessException) {
			PngBusinessException pne = (PngBusinessException) e;
			return new ExceptionOutput(pne.getErrorCode(), pne.getMessage());
		}

		return new ExceptionOutput("PNG-ERROR-00001(通用错误码)", e.getMessage());
	}

	public void throwExceptionWithCode(String errorCode) throws PngBusinessException {
		String message = getMessage(errorCode);
		if (StringUtils.isBlank(message)) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("System error. Error code - [");
			stringBuilder.append(errorCode);
			stringBuilder.append("]");
			message = stringBuilder.toString();
		}
		throw new PngBusinessException(errorCode, message);
	}

}

package com.vluee.png.shrfacade.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ExceptionOutput {
	@Getter
	private String code;
	
	@Getter
	private String message;
}

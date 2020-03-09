package com.vluee.png.shrfacade.infrastructure.shr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PlatenoSSOResponse {
	private String statusCode;
	private String message;
	private String data;
}

package com.vluee.png.shrfacade.infrastructure.sso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class PlatenoSSOResponse {

	public PlatenoSSOResponse() {

	}

	private String statusCode;
	private String message;
	private String data;
}

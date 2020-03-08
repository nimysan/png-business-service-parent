package com.vluee.png.shrfacade.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class VcodeRequest {
	@Getter
	private String sessionIdentifier;
	@Getter
	private long requestTime;
	@Getter
	private String vocde;
	@Getter
	private String mobile;
}

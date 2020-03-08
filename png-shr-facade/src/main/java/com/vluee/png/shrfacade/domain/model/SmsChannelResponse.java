package com.vluee.png.shrfacade.domain.model;

import lombok.Getter;
import lombok.Setter;

public class SmsChannelResponse {

	public SmsChannelResponse(String traceId, String channel) {
		this.traceId = traceId;
		this.channel = channel;
	}

	@Getter
	private String traceId;

	@Getter
	private String channel;

	@Getter
	@Setter
	private long requestTime;

	@Getter
	@Setter
	private long responseTime;

}

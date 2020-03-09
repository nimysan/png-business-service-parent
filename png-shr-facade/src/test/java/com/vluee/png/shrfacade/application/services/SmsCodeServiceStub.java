package com.vluee.png.shrfacade.application.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.vluee.png.shrfacade.domain.model.SmsChannelResponse;
import com.vluee.png.shrfacade.domain.service.AbstractVcodeService;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("integration-test")
@Slf4j
public class SmsCodeServiceStub extends AbstractVcodeService {

	@Override
	protected SmsChannelResponse sendBySmsProvier(String sessionId, String mobile, String vcode) {
		log.info("send vcode with stub");
		return new SmsChannelResponse("123", "123");
	}

}

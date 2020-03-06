package com.vluee.png.shrfacade.infrastructure.sms;

import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.application.services.VcodeService;

@Service
public class SmsVcodeService implements VcodeService {

	@Override
	public boolean sendCode(String mobile) {
		return false;
	}

}

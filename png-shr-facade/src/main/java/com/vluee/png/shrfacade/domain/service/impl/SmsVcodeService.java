package com.vluee.png.shrfacade.domain.service.impl;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.SmsChannelResponse;
import com.vluee.png.shrfacade.domain.service.AbstractVcodeService;
import com.vluee.png.shrfacade.domain.service.SmsChannelService;

/**
 * TODO 真正发送短信验证码
 * 
 * @author SeanYe
 *
 */
@Service
@Profile("!integration-test")
public class SmsVcodeService extends AbstractVcodeService {

	@Autowired
	private SmsChannelService smsChannel;

	@Override
	protected SmsChannelResponse sendBySmsProvier(String sessionId, String mobile, String vcode) {
		smsChannel.sendVcode(sessionId, Arrays.array(mobile), vcode);
		return null;
	}

}

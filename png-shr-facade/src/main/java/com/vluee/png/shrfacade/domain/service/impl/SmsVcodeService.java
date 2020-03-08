package com.vluee.png.shrfacade.domain.service.impl;

import com.vluee.png.shrfacade.domain.model.SmsChannelResponse;
import com.vluee.png.shrfacade.domain.service.AbstractVcodeService;

/**
 * TODO 真正发送短信验证码
 * 
 * @author SeanYe
 *
 */
public class SmsVcodeService extends AbstractVcodeService {

	@Override
	protected SmsChannelResponse sendBySmsProvier(String mobile, String vcode) {
		return null;
	}

}

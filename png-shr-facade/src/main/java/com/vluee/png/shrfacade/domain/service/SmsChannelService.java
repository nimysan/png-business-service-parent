package com.vluee.png.shrfacade.domain.service;

/**
 *     短信渠道接口
 * @author SeanYe
 *
 */
public interface SmsChannelService {

	public void sendVcode(String sessionId, String[] phoneNumbers, String vcode);

}

package com.vluee.png.shrfacade.domain.service;

import java.util.concurrent.TimeUnit;

/**
 * 
 * 发送一个验证码并验证验证码有效性。
 * 
 * 每次发送都需要给定具体的会话ID。
 * 
 * @author SeanYe
 *
 */
public interface VcodeService {

	//验证码请求限制时间（在给定分钟内不允许再次发送验证）
	public static final long VCODE_REQUEST_DURATION = TimeUnit.MINUTES.toMillis(1);

	// 验证码有效期
	public static final long VCODE_EXPIRED_DURATION = TimeUnit.MINUTES.toMillis(10);

	/**
	 * 
	 * @param sessionIdentifier
	 * @param mobile
	 * @return
	 */
	public String sendCode(String sessionIdentifier, String mobile);

	public void validateVcode(String sessionIdentifier, String mobile, String vcode);

}

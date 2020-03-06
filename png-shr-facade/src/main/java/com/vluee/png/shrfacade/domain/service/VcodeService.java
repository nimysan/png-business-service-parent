package com.vluee.png.shrfacade.domain.service;

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

	public String sendCode(String sessionIdentifier, String mobile);

	public boolean validate(String sessionIdentifier, String vcode);

}

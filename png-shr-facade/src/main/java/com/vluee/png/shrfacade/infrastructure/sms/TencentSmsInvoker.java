package com.vluee.png.shrfacade.infrastructure.sms;

/**
 *  腾讯短信接口调調用
 * @author SeanYe
 *
 */
public interface TencentSmsInvoker {

	void send(String vcode);

}

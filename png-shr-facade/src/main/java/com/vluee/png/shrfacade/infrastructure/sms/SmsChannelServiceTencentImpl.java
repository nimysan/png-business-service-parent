package com.vluee.png.shrfacade.infrastructure.sms;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.vluee.png.shrfacade.domain.service.SmsChannelService;

import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯短信接口调調用
 * 
 * @author SeanYe
 *
 */
@Service
@Slf4j
public class SmsChannelServiceTencentImpl implements SmsChannelService {

	@Value("${png.sms.tencent.appkey}")
	private String appkey;

	@Value("${png.sms.tencent.appid}")
	private int appid;

	@Value("${png.sms.tencent.smsSign}")
	private String smsSign;

	@Value("${png.sms.tencent.templateId}")
	private int templateId;

	@PostConstruct
	public void info() {
		log.info("腾讯短信渠道接口 信息  appid [{}] 签名 [{}]  模板Id [{}]", appid, smsSign, templateId);
	}

	public void sendVcode(String sessionId, String[] phoneNumbers, String vcode) {
		try {
			String[] params = { vcode };
			SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
			SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0], templateId, params, smsSign, "",
					"");
			System.out.println(result);
		} catch (HTTPException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

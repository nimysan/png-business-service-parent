package com.vluee.png.shrfacade.domain.service.impl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.domain.model.VcodeRepository;
import com.vluee.png.shrfacade.domain.service.SmsService;
import com.vluee.png.shrfacade.domain.service.VcodeService;

@Service
public class SmsVcodeServiceImpl implements VcodeService {

	@Autowired
	private VcodeRepository vcodeRepository;

	@Autowired
	private SmsService smsService;

	private String nextCode() {
		int n = 6;
		StringBuilder code = new StringBuilder();
		Random ran = new Random();
		for (int i = 0; i < n; i++) {
			code.append(Integer.valueOf(ran.nextInt(10)).toString());
		}
		return code.toString();
	}

	@Override
	public String sendCode(String sessionIdentifier, String mobile) {
		if (vcodeRepository.get(sessionIdentifier) != null) {
			throw new PngBusinessException();// TODO 异常： 请不要重复发送短信验证码
		} else {
			String vcode = nextCode();
			smsService.send(vcode);
			vcodeRepository.store(sessionIdentifier, mobile, vcode);
			return vcode;
		}
	}

	@Override
	public boolean validate(String sessionIdentifier, String vcode) {
		String savedVcode = vcodeRepository.get(sessionIdentifier);
		return savedVcode != null && savedVcode.contentEquals(vcode);
	}

}

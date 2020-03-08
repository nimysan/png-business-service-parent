package com.vluee.png.shrfacade.domain.service.impl;

import static com.vluee.png.shrfacade.application.exception.PngBusinessException.EC_ONLY_ONE_VCODE_WITHIN_TIME;
import static com.vluee.png.shrfacade.application.exception.PngExceptionUtil.throwExceptionWithCode;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.domain.model.VcodeRequest;
import com.vluee.png.shrfacade.domain.model.VcodeRequestRepository;
import com.vluee.png.shrfacade.domain.service.VcodeService;

public abstract class AbstractVcodeService implements VcodeService {

	@Autowired
	private VcodeRequestRepository vcodeRepository;

	private String nextCode() {
		int n = 6;
		StringBuilder code = new StringBuilder();
		Random ran = new Random();
		for (int i = 0; i < n; i++) {
			code.append(Integer.valueOf(ran.nextInt(10)).toString());
		}
		return code.toString();
	}

	/**
	 *  调用真实的短信渠道发送发送验证码
	 * @param mobile
	 * @param vcode
	 * @return
	 */
	protected abstract String sendBySmsProvier(String mobile, String vcode);

	@Override
	public String sendCode(String sessionIdentifier, String mobile) {
		if (vcodeRepository.get(sessionIdentifier) != null) {
			throw new PngBusinessException(PngBusinessException.EC_NOT_PNG_EMPLOYEE);
		} else {
			String vcode = nextCode();
			sendBySmsProvier(mobile, vcode);
			vcodeRepository.store(new VcodeRequest(sessionIdentifier, new Date().getTime(), vcode, mobile));
			return vcode;
		}
	}

	@Override
	public boolean validate(String sessionIdentifier, String vcode) {
		String savedVcode = vcodeRepository.get(sessionIdentifier);
		return savedVcode != null && savedVcode.contentEquals(vcode);
	}

	@Override
	public void validateRequest(String sessionIdentifier, String mobile) {
		VcodeRequest latestRequest = vcodeRepository.getLatestRequest(sessionIdentifier);
		boolean isValidRequest = latestRequest == null || isInTimeRange(latestRequest.getRequestTime());
		if (!isValidRequest) {
			throwExceptionWithCode(EC_ONLY_ONE_VCODE_WITHIN_TIME);
		}
	}

	private boolean isInTimeRange(long lastRequestTime) {
		return (new Date().getTime() - lastRequestTime) > TimeUnit.MINUTES.toMillis(30);
	}

}

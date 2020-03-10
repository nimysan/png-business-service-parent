package com.vluee.png.shrfacade.domain.service;

import static com.vluee.png.shrfacade.application.exception.PngBusinessException.EC_ONLY_ONE_VCODE_WITHIN_TIME;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.vluee.png.shrfacade.PngConstants;
import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.application.exception.PngExceptionHandler;
import com.vluee.png.shrfacade.domain.model.SmsChannelResponse;
import com.vluee.png.shrfacade.domain.model.VcodeRequest;
import com.vluee.png.shrfacade.domain.model.VcodeRequestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractVcodeService implements VcodeService {

	@Autowired
	private VcodeRequestRepository vcodeRequestRepository;

	@Autowired
	private PngExceptionHandler exceptionHandler;

	/**
	 * @param exceptionHandler the exceptionHandler to set
	 */
	public void setExceptionHandler(PngExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

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
	 * 调用真实的短信渠道发送发送验证码
	 * 
	 * @param mobile
	 * @param vcode
	 * @return
	 */
	protected abstract SmsChannelResponse sendBySmsProvier(String sessionId, String mobile, String vcode);

	@Override
	public String sendCode(String sessionIdentifier, String mobile) {
		validateRequest(sessionIdentifier, mobile);
		String vcode = nextCode();
		SmsChannelResponse smsResponse = sendBySmsProvier(sessionIdentifier, mobile, vcode);// TODO 储存结果
		vcodeRequestRepository.store(sessionIdentifier,
				new VcodeRequest(sessionIdentifier, new Date().getTime(), vcode, mobile));
		return vcode;
	}

	@Override
	public void validateVcode(String sessionIdentifier, String mobile, String vcode) {
		VcodeRequest savedVcode = vcodeRequestRepository.get(sessionIdentifier);

		if (savedVcode == null) {
			throwExceptionWithCode(PngBusinessException.EC_VCODE_NOTEXIST);
		}

		if (isVcodeExpired(savedVcode)) {
			throwExceptionWithCode(PngBusinessException.EC_VCODE_EXPIRED);
		}

		if (!savedVcode.getMobile().contentEquals(mobile)) {
			throwExceptionWithCode(PngBusinessException.EC_VCODE_MOBILE_NOT_MATCH);
		}

		if (savedVcode.getVocde().contentEquals(vcode) && savedVcode.getMobile().contentEquals(mobile)) {
			return;
		} else {
			if (!savedVcode.getVocde().contentEquals(vcode)) {
				throwExceptionWithCode(PngBusinessException.EC_VCODE_NOTEXIST);
			}

			if (!savedVcode.getMobile().contentEquals(mobile)) {
				throwExceptionWithCode(PngBusinessException.EC_VCODE_MOBILE_NOT_MATCH);
			}
		}
	}

	private void throwExceptionWithCode(String errorCode) {
		exceptionHandler.throwExceptionWithCode(errorCode);
	}

	private boolean isVcodeExpired(VcodeRequest savedVcode) {
		return (new Date().getTime() - savedVcode.getRequestTime()) > PngConstants.VCODE_LIVE_TIME;
	}

	protected void validateRequest(String sessionIdentifier, String mobile) {
		VcodeRequest latestRequest = vcodeRequestRepository.get(sessionIdentifier);
		if (latestRequest == null) {
			return;
		}
		boolean isInTimeRange = isInTimeRange(latestRequest.getRequestTime());
		if (isInTimeRange) {
			throwExceptionWithCode(EC_ONLY_ONE_VCODE_WITHIN_TIME);
		}
	}

	private boolean isInTimeRange(long lastRequestTime) {
		return (new Date().getTime() - lastRequestTime) <= PngConstants.VCODE_REQUEST_LIVE_TIME;
	}

}

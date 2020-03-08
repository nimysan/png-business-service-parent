package com.vluee.png.shrfacade.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.domain.model.SmsChannelResponse;
import com.vluee.png.shrfacade.domain.model.VcodeRequest;
import com.vluee.png.shrfacade.domain.model.VcodeRequestRepository;

@ExtendWith(MockitoExtension.class)
class AbstractVcodeServiceTest {

	@Mock
	private VcodeRequestRepository vcodeRepository;

	@InjectMocks
	private VcodeService vcodeService = new AbstractVcodeService() {

		@Override
		protected SmsChannelResponse sendBySmsProvier(String mobile, String vcode) {
			return new SmsChannelResponse("Tencent", "traceid-xxx-xxx-xxx-xxx");
		}

	};

	@Test
	@DisplayName("验证用户第一次请求发送验证码")
	void verfiyValidateRequest() {
		String sessionIdentifier = "test";
		String mobile = "13512341234";
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(null);
		String vcode = vcodeService.sendCode(sessionIdentifier, mobile);
		verify(vcodeRepository).get(sessionIdentifier);
		assertThat(vcode).isNotBlank();
	}

	@Test
	@DisplayName("验证-x分钟内不允许再次发送验证码")
	void verfiyValidateRequestInTimes() {
		String sessionIdentifier = "test";
		String mobile = "13512341234";
		VcodeRequest before10secondsRequest = new VcodeRequest(sessionIdentifier,
				new Date().getTime() - VcodeService.VCODE_REQUEST_DURATION + 1000, "1234", mobile);
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(before10secondsRequest);
//		verify(vcodeRepository).getLatestRequest(sessionIdentidfier);
		assertThrows(PngBusinessException.class, () -> {
			vcodeService.sendCode(sessionIdentifier, mobile);
		});
	}

	@Test
	@DisplayName("验证-x分钟之後允许再次发送验证码")
	void verfiyValidateRequestOutTimes() {
		String sessionIdentifier = "test";
		String mobile = "13512341234";
		VcodeRequest before10secondsRequest = new VcodeRequest(sessionIdentifier,
				new Date().getTime() - VcodeService.VCODE_REQUEST_DURATION - 1000, "1234", mobile);
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(before10secondsRequest);
		vcodeService.sendCode(sessionIdentifier, mobile);
	}

	@Test
	@DisplayName("验证第一次发送验证码并使用正确的验证码验证")
	void verifyNormalVcode() {
		String sessionIdentifier = "test";
		String mobile = "13512341234";
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(null);
		String vcode = vcodeService.sendCode(sessionIdentifier, mobile);
		VcodeRequest vcodeHistory = new VcodeRequest(sessionIdentifier, new Date().getTime() - 1000 * 10, vcode,
				mobile);
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(vcodeHistory);
		vcodeService.validateVcode(sessionIdentifier, mobile, vcode);
	}

	@Test
	@DisplayName("验证-使用不匹配的手机号")
	void verifyNormalVcodeWithAnotherMobilePhone() {
		String sessionIdentifier = "test";
		String mobile = "13512341234";
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(null);
		String vcode = vcodeService.sendCode(sessionIdentifier, mobile);
		VcodeRequest vcodeHistory = new VcodeRequest(sessionIdentifier, new Date().getTime() - 1000 * 10, vcode,
				"13512341111");
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(vcodeHistory);
		assertThrows(PngBusinessException.class, () -> {
			vcodeService.validateVcode(sessionIdentifier, mobile, vcode);
		});
	}

	@Test
	@DisplayName("验证-验证码不匹配")
	void verifyVcodeNotMatch() {
		String sessionIdentifier = "test";
		String mobile = "13512341234";
		String vcode = "123456";
		VcodeRequest vcodeHistory = new VcodeRequest(sessionIdentifier, new Date().getTime() - 3, vcode, mobile);
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(vcodeHistory);
		assertThrows(PngBusinessException.class, () -> {
			vcodeService.validateVcode(sessionIdentifier, mobile, vcode + "diff");
		});
	}

	@Test
	@DisplayName("验证10分钟后验证码过期")
	void verifyExpiredVCode() {
		String sessionIdentifier = "test";
		String mobile = "13512341234";
		String vcode = "123456";
		VcodeRequest vcodeHistory = new VcodeRequest(sessionIdentifier,
				new Date().getTime() - VcodeService.VCODE_EXPIRED_DURATION - 2000, vcode, mobile);
		when(vcodeRepository.get(sessionIdentifier)).thenReturn(vcodeHistory);
		assertThrows(PngBusinessException.class, () -> {
			vcodeService.validateVcode(sessionIdentifier, mobile, vcode);
		});
	}

}

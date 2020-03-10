package com.vluee.png.shrfacade.domain.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.vluee.png.shrfacade.PngConstants;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOClient;
import com.vluee.png.shrfacade.infrastructure.sso.PlatenoSSOResponse;

@ExtendWith(MockitoExtension.class)
class HrServiceKingdeeShrImplTest {

	@InjectMocks
	private HrServiceKingdeeShrImpl kingdeeHrService = new HrServiceKingdeeShrImpl();

	@Mock
	private PlatenoSSOClient ssoClient;

	@Test
	@DisplayName("SSO用户不存在")
	void verifySSOReturn500() {
		String url = PngConstants.API_GET_USER_BY_MOBILE;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		String mobile = "12356";
		params.add("mobile", mobile);
		String name = "haris";
		when(ssoClient.sendApiRequest(url, params, HttpMethod.GET))
				.thenReturn(new PlatenoSSOResponse("500", "no user", null));
		assertThat(kingdeeHrService.getUserByMobile(mobile, name)).isNull();
		verify(ssoClient, times(1)).sendApiRequest(url, params, HttpMethod.GET);
	}

	@Test
	@DisplayName("SSO返回数据异常")
	void verifySSOReturnIsInvalid() {
		String url = PngConstants.API_GET_USER_BY_MOBILE;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		String mobile = "12356";
		params.add("mobile", mobile);
		String name = "haris";
		when(ssoClient.sendApiRequest(url, params, HttpMethod.GET))
				.thenReturn(new PlatenoSSOResponse(PngConstants.RETURN_CODE_OK, "user", "xxxxxx"));
		assertThat(kingdeeHrService.getUserByMobile(mobile, name)).isNull();
		verify(ssoClient, times(1)).sendApiRequest(url, params, HttpMethod.GET);
	}

}

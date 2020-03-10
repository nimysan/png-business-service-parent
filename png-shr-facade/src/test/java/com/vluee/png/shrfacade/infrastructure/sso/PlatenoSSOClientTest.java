package com.vluee.png.shrfacade.infrastructure.sso;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSONObject;
import com.vluee.png.shrfacade.PngConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlatenoSSOClientTest {

	private PlatenoSSOClient ssoClient = new TestStub("test", "test", "test");

	@Test
	void verifyTokenExpiredAndRetry() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("mobile", "123");
		PlatenoSSOClient spy = Mockito.spy(ssoClient);
		String url = "/expiredtest";
		spy.sendApiRequest(url, params, HttpMethod.GET);
		verify(spy, times(2)).sendApiRequestForString(url, params, HttpMethod.GET);
		verify(spy, times(1)).fetchToken();

		Mockito.clearInvocations(spy);
		spy.sendApiRequest("/normal", params, HttpMethod.GET);
		verify(spy, times(1)).sendApiRequestForString("/normal", params, HttpMethod.GET);
	}

	/**
	 * 为测试callForEntity行为的一个桩
	 * 
	 * @author SeanYe
	 *
	 */
	public static class TestStub extends PlatenoSSOClient {

		public TestStub(String appId, String appKey, String ssoUri) {
			super(appId, appKey, ssoUri);
		}

		@Override
		protected void fetchToken() {
			setToken("test");
		}

		@Override
		public ResponseEntity<String> sendApiRequestForString(String url, MultiValueMap<String, String> params,
				HttpMethod httpMethod) {
			if ("/expiredtest".contains(url)) {
				PlatenoSSOResponse data = PlatenoSSOResponse.builder().statusCode(PngConstants.SSO_EC_TOEKN_EXPIRED)
						.message("token expired").data("").build();
				log.info("test #{}#", data);
				String jsonString = JSONObject.toJSONString(data);
				log.info("test #{}#", jsonString);
				return ResponseEntity.ok(jsonString);
			}
			if ("/excepiontest".contentEquals(url)) {
				throw new RuntimeException("mock exception");
			}
			return ResponseEntity.ok(JSONObject.toJSONString(PlatenoSSOResponse.builder()
					.statusCode(PngConstants.SSO_RETURN_CODE_OK).message("forbat").data("").build()));
		}

	}

}

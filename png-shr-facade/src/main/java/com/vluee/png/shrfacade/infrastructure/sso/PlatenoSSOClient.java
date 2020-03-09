package com.vluee.png.shrfacade.infrastructure.sso;

import static com.vluee.png.shrfacade.infrastructure.sso.SSOConstants.EC_TOEKN_EXPIRED;
import static com.vluee.png.shrfacade.infrastructure.sso.SSOConstants.HEADER_PLATENO_SSO_AUTHENTICATE;
import static com.vluee.png.shrfacade.infrastructure.sso.SSOConstants.RETURN_CODE_OK;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("!integration-test")
public class PlatenoSSOClient {

	@Value("${png.sso.appid}")
	private String appId;

	@Value("${png.sso.appkey}")
	private String appKey;

	@Value("${png.sso.uri}")
	private String ssoUri;

	private RestTemplate restTemplate = new RestTemplate();

	public PlatenoSSOClient(String appId, String appKey, String ssoUri) {
		this.appId = appId;
		this.appKey = appKey;
		this.ssoUri = ssoUri;
		init();
	}

	private String token;

	@PostConstruct
	void init() {
		log.info("初始化SSO调用信息 appid-{},url-{} ", appId, ssoUri);
		fetchToken();
		if (StringUtils.isBlank(token)) {
			throw new RuntimeException("系统系统异常， SSO配置有误导致无法访问SSO相关服务");
		}
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	protected void fetchToken() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("loginName", appId);
		params.add("password", appKey);
		ResponseEntity<String> exchange = sendTokenRequset("/cxf/userManage/loginIn", params);
		PlatenoSSOResponse ssoResponse = JSONObject.parseObject(exchange.getBody(), PlatenoSSOResponse.class);
		if (ssoResponse.getStatusCode().contentEquals(RETURN_CODE_OK)) {
			token = ssoResponse.getData();
		}
	}

	public ResponseEntity<String> sendTokenRequset(String url, MultiValueMap<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		HttpMethod method = HttpMethod.POST;
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
		ResponseEntity<String> exchange = restTemplate.exchange(ssoUri + url, method, requestEntity, String.class);
		return exchange;
	}

	/**
	 * 如果因为token失效，该方法会尝试重新获取token并重试
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public PlatenoSSOResponse sendApiRequest(String url, MultiValueMap<String, String> params) {
		log.debug("SSO Request for {} with params: {}", url, params);
		ResponseEntity<String> sendSSORequest = sendApiRequestForString(url, params);
		PlatenoSSOResponse parseObject = JSONObject.parseObject(sendSSORequest.getBody(), PlatenoSSOResponse.class);
		/**
		 * 再次获取token并重试
		 */
		if (serverReportTokenExpired(parseObject)) {
			fetchToken();
			sendSSORequest = sendApiRequestForString(url, params);
			return JSONObject.parseObject(sendSSORequest.getBody(), PlatenoSSOResponse.class);
		}
		return parseObject;
	}

	public ResponseEntity<String> sendApiRequestForString(String url, MultiValueMap<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HEADER_PLATENO_SSO_AUTHENTICATE, token);
		HttpMethod method = HttpMethod.POST;
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
		ResponseEntity<String> sendSSORequest = restTemplate.exchange(ssoUri + url, method, requestEntity,
				String.class);
		return sendSSORequest;
	}

	private boolean serverReportTokenExpired(PlatenoSSOResponse ssoResponse) {
		return ssoResponse.getStatusCode().contentEquals(EC_TOEKN_EXPIRED);
	}
}

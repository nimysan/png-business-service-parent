package com.vluee.png.shrfacade.infrastructure.shr;

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
import com.vluee.png.shrfacade.domain.model.HrUser;

import lombok.extern.slf4j.Slf4j;

@Service
@Profile("dev")
@Slf4j
public class PlatenoSSOClient {

	@Value("${png.sso.appid}")
	private String appId;
	@Value("${png.sso.appkey}")
	private String appKey;
	@Value("${png.sso.uri}")
	private String ssoUri;

	private RestTemplate restTemplate = new RestTemplate();

	private String token;

	@PostConstruct
	public void init() {
		log.info("初始化SSO调用信息 appid-{},url-{} ", appId, ssoUri);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("loginName", appId);
		params.add("password", appKey);
		ResponseEntity<String> exchange = sendSSORequest("/cxf/userManage/loginIn", params);
		PlatenoSSOResponse ssoResponse = JSONObject.parseObject(exchange.getBody(), PlatenoSSOResponse.class);
		if (ssoResponse.getStatusCode().contentEquals("200")) {
			token = ssoResponse.getData();
		}
		if (StringUtils.isBlank(token)) {
			throw new RuntimeException("系统系统异常， SSO配置有误导致无法访问SSO相关服务");
		}
		log.info(" Response  {}", exchange);
	}

	private ResponseEntity<String> sendSSORequest(String url, MultiValueMap<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		HttpMethod method = HttpMethod.POST;
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
		ResponseEntity<String> exchange = restTemplate.exchange(ssoUri + url, method, requestEntity, String.class);
		return exchange;
	}

	public HrUser getUser(String mobile) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("mobile", mobile);
		ResponseEntity<String> sendSSORequest = sendSSORequest("/cxf/hrManage/getUserInfoListByMobile", params);
		JSONObject userJsonObj = JSONObject.parseObject(sendSSORequest.getBody());
		String userMobileFromHr = userJsonObj.getString("mobile");
		if (userMobileFromHr.contentEquals(mobile)) {
			return null; // TODO 如何提示
		}
		HrUser hrUser = new HrUser(userJsonObj.getString("userId"), mobile, userJsonObj.getString("name"));
		return hrUser;

	}
}

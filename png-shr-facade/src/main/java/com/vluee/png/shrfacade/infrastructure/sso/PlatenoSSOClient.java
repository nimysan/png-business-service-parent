package com.vluee.png.shrfacade.infrastructure.sso;

import static com.vluee.png.shrfacade.PngConstants.EC_TOEKN_EXPIRED;
import static com.vluee.png.shrfacade.PngConstants.HEADER_PLATENO_SSO_AUTHENTICATE;
import static com.vluee.png.shrfacade.PngConstants.RETURN_CODE_OK;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
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

	// 解决RestTemplate中文乱码问题
	public static RestTemplate getRestTemplate(Charset charset) {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
		for (HttpMessageConverter<?> httpMessageConverter : list) {
			if (httpMessageConverter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(charset);
				break;
			}
		}
		return restTemplate;
	}

	private RestTemplate restTemplate = getRestTemplate(Charset.forName("utf8"));

	public PlatenoSSOClient() {

	}

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
			throw new RuntimeException("无法正常获取到SSO访问TOKEN， SSO配置有误导致无法访问SSO相关服务");
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
		log.debug("fetch token resonse {}", exchange);
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
	public PlatenoSSOResponse sendApiRequest(String url, MultiValueMap<String, String> params, HttpMethod method) {
		log.debug("SSO Request for {} with params: {}", url, params);
		ResponseEntity<String> sendSSORequest = sendApiRequestForString(url, params, method);
		PlatenoSSOResponse parseObject = JSONObject.parseObject(sendSSORequest.getBody(), PlatenoSSOResponse.class);
		/**
		 * 再次获取token并重试
		 */
		if (serverReportTokenExpired(parseObject)) {
			fetchToken();
			sendSSORequest = sendApiRequestForString(url, params, method);
			return JSONObject.parseObject(sendSSORequest.getBody(), PlatenoSSOResponse.class);
		}
		return parseObject;
	}

	public ResponseEntity<String> sendApiRequestForString(String url, MultiValueMap<String, String> params,
			HttpMethod method) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HEADER_PLATENO_SSO_AUTHENTICATE, token);
		if (method.equals(HttpMethod.GET)) {
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
			List<NameValuePair> querys = new ArrayList<NameValuePair>(params.size());
			params.forEach((t, v) -> {
				NameValuePair nvp = new BasicNameValuePair(t, v.get(0));
				querys.add(nvp);
			});
			String longUrl = ssoUri + url + "?" + URLEncodedUtils.format(querys, Charset.forName("utf8"));
			log.debug("SSO url {}", longUrl);
			return restTemplate.exchange(longUrl, method, requestEntity, String.class);
		} else {
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
			return restTemplate.exchange(ssoUri + url, method, requestEntity, String.class, params);
		}
	}

	private boolean serverReportTokenExpired(PlatenoSSOResponse ssoResponse) {
		return ssoResponse.getStatusCode().contentEquals(EC_TOEKN_EXPIRED);
	}
}

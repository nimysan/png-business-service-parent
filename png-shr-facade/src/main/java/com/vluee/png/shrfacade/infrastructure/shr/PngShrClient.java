package com.vluee.png.shrfacade.infrastructure.shr;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kingdee.shr.api.Response;
import com.kingdee.shr.api.SHRClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PngShrClient {

	@Value("${png.shr.host}")
	private String shrUrl;

	private SHRClient shrClient;

	private synchronized SHRClient getOrCreateShrClient() {
		if (shrClient == null) {
			shrClient = new SHRClient();
		}
		return shrClient;
	}

	/**
	 * @param shrUrl the shrUrl to set
	 */
	public void setShrUrl(String shrUrl) {
		this.shrUrl = shrUrl;
	}

	public String getEhrData(String userId, String method, int currPage, int pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currPage", String.valueOf(currPage));
		map.put("pageSize", String.valueOf(pageSize));
		log.debug("===>  SHR Client call url={},userId={},method={}, map={}", shrUrl, userId, method, map);
		Response executeService = getOrCreateShrClient().executeService(userId, shrUrl, method, map);
		return executeService.getData().toString();
	}

}

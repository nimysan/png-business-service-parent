package com.vluee.png.shrfacade.infrastructure.vcode;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vluee.png.shrfacade.PngConstants;
import com.vluee.png.shrfacade.domain.model.VcodeRequest;
import com.vluee.png.shrfacade.domain.model.VcodeRequestRepository;

@Repository
public class InMemoryVcodeRequestRepository implements VcodeRequestRepository {
	Cache<String, VcodeRequest> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(PngConstants.VCODE_LIVE_TIME, TimeUnit.MILLISECONDS).build();

	public VcodeRequest get(String sessionId) {
		return cache.getIfPresent(sessionId);
	}

	public void store(String sessionId, VcodeRequest request) {
		cache.put(sessionId, request);
	}

}

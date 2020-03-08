package com.vluee.png.shrfacade.infrastructure.vcode;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vluee.png.shrfacade.domain.model.VcodeRequest;
import com.vluee.png.shrfacade.domain.model.VcodeRequestRepository;
import com.vluee.png.shrfacade.domain.service.VcodeService;

@Service
public class InMemoryVcodeRepository implements VcodeRequestRepository {

	Cache<String, VcodeRequest> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(VcodeService.VCODE_EXPIRED_DURATION, TimeUnit.MILLISECONDS).build();

	@Override
	public VcodeRequest get(String sessionIdentifier) {
		return cache.getIfPresent(sessionIdentifier);
	}

	@Override
	public void store(VcodeRequest request) {
		cache.put(request.getSessionIdentifier(), request);
	}

}

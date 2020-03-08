package com.vluee.png.shrfacade.infrastructure.vcode;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vluee.png.shrfacade.domain.model.VcodeRequest;
import com.vluee.png.shrfacade.domain.model.VcodeRequestRepository;

@Service
public class InMemoryVcodeRepository implements VcodeRequestRepository {

	Cache<String, String> cache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();

	@Override
	public VcodeRequest get(String sessionIdentifier) {
		return null;
	}

	@Override
	public void store(VcodeRequest request) {
		// TODO Auto-generated method stub

	}

}

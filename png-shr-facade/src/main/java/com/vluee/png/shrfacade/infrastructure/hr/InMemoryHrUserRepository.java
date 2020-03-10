package com.vluee.png.shrfacade.infrastructure.hr;

import static com.vluee.png.shrfacade.PngConstants.HR_USERS_CACHE_DURATION;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vluee.png.shrfacade.domain.model.hr.HrUser;
import com.vluee.png.shrfacade.domain.model.hr.HrUserRepository;

@Repository
public class InMemoryHrUserRepository implements HrUserRepository {

	Cache<String, HrUser> userCache = CacheBuilder.newBuilder()
			.expireAfterWrite(HR_USERS_CACHE_DURATION, TimeUnit.MILLISECONDS).build();

	@Override
	public HrUser getByMobile(String mobile) {
		return userCache.getIfPresent(mobile);
	}

	@Override
	public void store(String mobile, HrUser hrUser) {
		userCache.put(mobile, hrUser);
	}

}

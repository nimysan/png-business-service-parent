package com.vluee.png.shrfacade.infrastructure.hr;

import static com.vluee.png.shrfacade.PngConstants.HR_SALARY_CACHE_DURATION;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalaryRepository;

@Repository
public class InMemoryEmployeeMonthSalaryRepository implements EmployeeMonthSalaryRepository {
	Cache<String, EmployeeMonthSalary> salaryCache = CacheBuilder.newBuilder()
			.expireAfterWrite(HR_SALARY_CACHE_DURATION, TimeUnit.MILLISECONDS).build();

	@Override
	public void store(String userId, EmployeeMonthSalary value) {
		salaryCache.put(userId, value);
	}

	@Override
	public EmployeeMonthSalary get(String userId) {
		return salaryCache.getIfPresent(userId);
	}
}

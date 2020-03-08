package com.vluee.png.shrfacade.domain.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.service.HrService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("!dev")
public class HrServiceShrImpl implements HrService {

	@Override
	public EmployeeMonthSalary fetchSalary(String phone) {
		log.info("Get Salary real method");
		return null;
	}

	@Override
	public HrUser getUserByMobile(String mobile) {
		log.info("getUserByMobile real method");
		return null;
	}

}

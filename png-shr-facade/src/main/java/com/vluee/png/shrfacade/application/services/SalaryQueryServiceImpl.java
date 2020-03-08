package com.vluee.png.shrfacade.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.service.ShrService;
import com.vluee.png.shrfacade.domain.service.VcodeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SalaryQueryServiceImpl implements SalaryQueryService {

	@Autowired
	private VcodeService vcodeService;

	@Autowired
	private ShrService shrService;

	@Override
	public EmployeeMonthSalary getSalary(String sessionIdentifier, String mobile, String vcode) {

		shrService.validateUserByMobile(mobile);
		
		vcodeService.validate(sessionIdentifier, vcode);

		return shrService.fetchSalary(mobile);
	}

	@Override
	public String sendVcodeToUser(String sessionIdentifier, String mobile) {
		
		shrService.validateUserByMobile(mobile);
		
		vcodeService.validateRequest(sessionIdentifier, mobile);
		
		String vcode = vcodeService.sendCode(sessionIdentifier, mobile);

		if (log.isInfoEnabled()) {
			log.info("send vcode %s to mobile %s in session [%s]", vcode, mobile, sessionIdentifier);
		}

		return vcode;
	}


}

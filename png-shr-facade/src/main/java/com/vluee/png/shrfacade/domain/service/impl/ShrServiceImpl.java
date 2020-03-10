package com.vluee.png.shrfacade.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.PngConstants;
import com.vluee.png.shrfacade.domain.model.assembler.EmployeeMonthSalaryAssembler;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalaryRepository;
import com.vluee.png.shrfacade.domain.service.ShrService;
import com.vluee.png.shrfacade.infrastructure.shr.PngShrClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShrServiceImpl implements ShrService {
	@Autowired
	private PngShrClient pngShrClient;

	@Autowired
	private EmployeeMonthSalaryRepository salaryRepository;

	@Override
	public EmployeeMonthSalary fetchSalary(String userId) {

		EmployeeMonthSalary ifPresent = salaryRepository.get(userId);
		if (ifPresent != null) {
			return ifPresent;
		}
		try {
			String ehrData = pngShrClient.getEhrData(userId, PngConstants.SHR_API_GET_SALARY, 1, 10);
			EmployeeMonthSalary assembleFromShrResponse = EmployeeMonthSalaryAssembler.assembleFromShrResponse(ehrData);
			salaryRepository.store(userId, assembleFromShrResponse);
		} catch (Exception e) {
			log.error("Call shr failed ", e);
		}
		return null;

	}

}

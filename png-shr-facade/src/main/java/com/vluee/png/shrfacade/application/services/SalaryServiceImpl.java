package com.vluee.png.shrfacade.application.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.application.exception.PngBusinessException;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.service.ShrService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SalaryServiceImpl implements SalaryService {

	@Autowired
	private VcodeService vcodeService;

	@Autowired
	private ShrService shrService;

	@Autowired
	private PngDataCenterService ssoService;

	@Override
	public EmployeeMonthSalary getSalary(String mobile, String vcode) throws Exception {
		validateVcode(vcode);
		String employeeNumber = ssoService.getEmployeeNumberByMobile(mobile);
		if (isRealEmployeeNumber(employeeNumber)) {
			throw new PngBusinessException();
		} else {
			return shrService.fetchSalary(employeeNumber);
		}
	}

	private boolean isRealEmployeeNumber(String employeeNumber) {
		return StringUtils.isNotBlank(employeeNumber);
	}

	private void validateVcode(String vcode) {
		log.debug("Try to validate code as [%]", vcode);
	}

}

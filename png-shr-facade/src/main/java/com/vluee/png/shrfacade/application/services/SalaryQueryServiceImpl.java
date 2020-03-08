package com.vluee.png.shrfacade.application.services;

import static com.vluee.png.shrfacade.application.exception.PngBusinessException.EC_NOT_PNG_EMPLOYEE;
import static com.vluee.png.shrfacade.application.exception.PngExceptionUtil.throwExceptionWithCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.service.HrService;
import com.vluee.png.shrfacade.domain.service.VcodeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SalaryQueryServiceImpl implements SalaryQueryService {

	public SalaryQueryServiceImpl() {
		log.warn("---iamhere---");
	}

	@Autowired
	private VcodeService vcodeService;

	@Autowired
	private HrService shrService;

	@Override
	public EmployeeMonthSalary getSalary(String sessionIdentifier, String mobile, String vcode) {

		HrUser hrUser = shrService.getUserByMobile(mobile);
		if (hrUser == null) {
			throwExceptionWithCode(EC_NOT_PNG_EMPLOYEE);
		}

		vcodeService.validate(sessionIdentifier, vcode);

		return shrService.fetchSalary(hrUser.getUserId());
	}

	@Override
	public String sendVcodeToUser(String sessionIdentifier, String mobile) {

		HrUser hrUser = shrService.getUserByMobile(mobile);

		if (hrUser == null) {
			throwExceptionWithCode(EC_NOT_PNG_EMPLOYEE);
		}

		vcodeService.validateRequest(sessionIdentifier, mobile);

		String vcode = vcodeService.sendCode(sessionIdentifier, mobile);

		if (log.isInfoEnabled()) {
			log.info("send vcode %s to mobile %s in session [%s]", vcode, mobile, sessionIdentifier);
		}

		return vcode;
	}

}

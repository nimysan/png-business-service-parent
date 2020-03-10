package com.vluee.png.shrfacade.application.services;

import static com.vluee.png.shrfacade.application.exception.PngBusinessException.EC_NOT_PNG_EMPLOYEE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vluee.png.shrfacade.application.exception.PngExceptionHandler;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.HrUser;
import com.vluee.png.shrfacade.domain.service.HrService;
import com.vluee.png.shrfacade.domain.service.VcodeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SalaryQueryServiceImpl implements SalaryQueryService {

	@Autowired
	private PngExceptionHandler exceptionHandler;

	public SalaryQueryServiceImpl() {
		log.warn("---iamhere---");
	}

	/**
	 * @param exceptionHandler the exceptionHandler to set
	 */
	public void setExceptionHandler(PngExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Autowired
	private VcodeService vcodeService;

	@Autowired
	private HrService shrService;

	@Override
	public EmployeeMonthSalary getSalary(String sessionIdentifier, String mobile, String userName, String vcode) {

		HrUser hrUser = shrService.getUserByMobile(mobile, userName);
		if (hrUser == null) {
			exceptionHandler.throwExceptionWithCode(EC_NOT_PNG_EMPLOYEE);
		}
		log.info("validate vode with session id {} and mobile {} and vcode {}", sessionIdentifier, mobile, vcode);
		vcodeService.validateVcode(sessionIdentifier, mobile, vcode);
		return shrService.fetchSalary(hrUser.getUserId());
	}

	@Override
	public String sendVcodeToUser(String sessionIdentifier, String mobile, String userName) {
		log.info("Send vode with session id {} and mobile {}", sessionIdentifier, mobile);
		HrUser hrUser = shrService.getUserByMobile(mobile, userName);

		if (hrUser == null) {
			exceptionHandler.throwExceptionWithCode(EC_NOT_PNG_EMPLOYEE);
		}

		String vcode = vcodeService.sendCode(sessionIdentifier, mobile);

		log.info("send vcode {} to mobile {} in session [{}]", vcode, mobile, sessionIdentifier);

		return vcode;
	}

}
